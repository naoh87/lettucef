package dev.naoh.lettucef.streams

import cats.effect.Async
import cats.effect.Resource
import cats.effect.kernel.Concurrent
import cats.effect.std.Dispatcher
import cats.syntax.flatMap._
import cats.syntax.functor._
import dev.naoh.lettucef.core.RedisClientF.ConnectionResource2
import dev.naoh.lettucef.core.RedisClusterClientF.ConnectionResource1
import dev.naoh.lettucef.core.RedisPubSubF
import dev.naoh.lettucef.api.models.pubsub.PushedMessage
import dev.naoh.lettucef.streams.ManagedPubSubF.VoidListener
import dev.naoh.lettucef.streams.ManagedPubSubF.State
import fs2._
import fs2.concurrent.Channel
import fs2.concurrent.SignallingRef
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.pubsub.RedisPubSubListener

class ManagedPubSubF[F[_] : Async, K, V](
  underlying: RedisPubSubF[F, K, V],
  val dispatcher: Dispatcher[F],
  eState: SignallingRef[F, State[K]],
  pState: SignallingRef[F, State[K]],
) {
  def subscribeAwait(channels: K*): Resource[F, Stream[F, PushedMessage.Message[K, V]]] = {
    val target = channels.toSet
    ManagedPubSubF.stream(
      target, eState, emitSubscribe, emitUnsubscribe,
      startPush(ManagedPubSubF.messageSender[F, K, V](target, _, dispatcher)))
  }

  def subscribe(channel: K*): Stream[F, PushedMessage.Message[K, V]] =
    Stream.resource(subscribeAwait(channel: _*)).flatten

  def psubscribeAwait(patterns: K*): Resource[F, Stream[F, PushedMessage.PMessage[K, V]]] = {
    val target = patterns.toSet
    ManagedPubSubF.stream(
      target, pState, emitPSubscribe, emitPUnsubscribe,
      startPush(ManagedPubSubF.pmessageSender[F, K, V](target, _, dispatcher)))
  }

  def psubscribe(channel: K*): Stream[F, PushedMessage.PMessage[K, V]] =
    Stream.resource(psubscribeAwait(channel: _*)).flatten

  def addListener(listener: RedisPubSubListener[K, V]): F[Unit] =
    underlying.addListener(listener)

  def removeListener(listener: RedisPubSubListener[K, V]): F[Unit] =
    underlying.removeListener(listener)

  def setListener(listener: RedisPubSubListener[K, V]): Resource[F, F[Unit]] =
    underlying.setListener(listener)

  private def startPush[O](make: Channel[F, O] => RedisPubSubListener[K, V]): Resource[F, Stream[F, O]] =
    for {
      ch <- Resource.make(Channel.unbounded[F, O])(_.close.void)
      remove <- setListener(make(ch))
    } yield ch.stream.onFinalize(remove)

  private def init(): Resource[F, ManagedPubSubF[F, K, V]] =
    for {
      l <- Resource.eval(Async[F].delay(stateHandler()))
      _ <- setListener(l)
    } yield this

  private def stateHandler(): RedisPubSubListener[K, V] =
    new VoidListener[K, V] {
      override def subscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(
          eState.modify(State.subscribed(channel)).flatMap {
            case true => Async[F].unit
            case false => emitUnsubscribe(channel :: Nil)
          })

      override def unsubscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(
          eState.modify(State.unsubscribed(channel)).flatMap {
            case true => Async[F].unit
            case false => emitSubscribe(channel :: Nil)
          })

      override def psubscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(
          pState.modify(State.subscribed(channel)).flatMap {
            case true => Async[F].unit
            case false => emitPUnsubscribe(channel :: Nil)
          })

      override def punsubscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(
          pState.modify(State.unsubscribed(channel)).flatMap {
            case true => Async[F].unit
            case false => emitPSubscribe(channel :: Nil)
          })
    }

  private def emitSubscribe(ch: Seq[K]): F[Unit] = underlying.subscribe(ch: _*)

  private def emitUnsubscribe(ch: Seq[K]): F[Unit] = underlying.unsubscribe(ch: _*)

  private def emitPSubscribe(ch: Seq[K]): F[Unit] = underlying.psubscribe(ch: _*)

  private def emitPUnsubscribe(ch: Seq[K]): F[Unit] = underlying.punsubscribe(ch: _*)

}

object ManagedPubSubF {
  def create[F[_] : Async, K, V](
    underlying: Resource[F, RedisPubSubF[F, K, V]],
    d: Dispatcher[F]
  ): Resource[F, ManagedPubSubF[F, K, V]] = {
    for {
      u <- underlying
      s1 <- Resource.eval(SignallingRef.of(State.zero[K]))
      s2 <- Resource.eval(SignallingRef.of(State.zero[K]))
      as <- new ManagedPubSubF[F, K, V](u, d, s1, s2).init()
    } yield as
  }


  private def messageSender[F[_], K, V](
    target: Set[K],
    ch: Channel[F, PushedMessage.Message[K, V]],
    d: Dispatcher[F]
  ): RedisPubSubListener[K, V] = new VoidListener[K, V] {
    override def message(channel: K, message: V): Unit =
      if (target.contains(channel)) {
        d.unsafeRunSync(ch.send(PushedMessage.Message(channel, message)))
      }
  }

  private def pmessageSender[F[_], K, V](
    target: Set[K],
    ch: Channel[F, PushedMessage.PMessage[K, V]],
    d: Dispatcher[F]
  ): RedisPubSubListener[K, V] = new VoidListener[K, V] {
    override def message(pattern: K, channel: K, message: V): Unit = {
      if (target.contains(pattern)) {
        d.unsafeRunSync(ch.send(PushedMessage.PMessage(pattern, channel, message)))
      }
    }
  }

  private def stream[F[_] : Async, K, O](
    target: Set[K],
    state: SignallingRef[F, State[K]],
    up: Seq[K] => F[Unit],
    down: Seq[K] => F[Unit],
    startPush: Resource[F, Stream[F, O]]
  ): Resource[F, Stream[F, O]] = {
    def update1(
      f: State[K] => (State[K], Seq[K]),
      g: Seq[K] => F[Unit]
    ): F[Unit] =
      state.modify(f).flatMap {
        case ks if ks.isEmpty => Async[F].unit
        case ks => g(ks)
      }

    for {
      done <- Resource.eval(Async[F].memoize(update1(State.unsubscribe1(target), down).void))
      _ <- Resource.make(update1(State.subscribe1(target), up) >> await(target, state))(_ => done)
      s <- startPush
    } yield s.onFinalize(done)
  }

  private def await[F[_] : Concurrent, K](
    target: Set[K],
    state: SignallingRef[F, State[K]]
  ): F[Unit] =
    state.discrete
      .takeWhile(current => !target.forall(k => current.get(k).exists(_._1 == State.Subscribed)))
      .compile.drain

  type State[K] = Map[K, (Int, Int)]

  object State {
    def zero[K]: State[K] = Map.empty

    val Subscribing = 1
    val Subscribed = 2
    val Unsubscribing = 3


    private val up = (Subscribing, 1)

    def subscribe1[K](ch: Set[K]): State[K] => (State[K], Seq[K]) = { now =>
      var toUp = List.empty[K]
      var next = now
      ch.foreach { k =>
        next = now.get(k) match {
          case Some((s, c)) =>
            next.updated(k, (s, c + 1))
          case None =>
            toUp = k :: toUp
            next.updated(k, up)
        }
      }
      (next, toUp)
    }

    def unsubscribe1[K](ch: Set[K]): State[K] => (State[K], Seq[K]) = { now =>
      var toDown = List.empty[K]
      var next = now
      ch.foreach { k =>
        next = now.get(k) match {
          case Some(v) =>
            v match {
              case (Subscribed, 1) =>
                toDown = k :: toDown
                next.updated(k, (Unsubscribing, 0))
              case (s, n) =>
                next.updated(k, (s, n - 1))
            }
          case _ => //unexpected
            sys.error("unexpected state in unsubscribe1")
        }
      }
      (next, toDown)
    }

    def subscribed[K](ch: K): State[K] => (State[K], Boolean) = { now =>
      now.get(ch) match {
        case Some((Subscribing, c)) =>
          if (c > 0) {
            (now.updated(ch, (Subscribed, c)), true)
          } else {
            (now.updated(ch, (Unsubscribing, 0)), false)
          }
        case _ => //unexpected
          sys.error(s"unexpected state in subscribed")
      }
    }

    def unsubscribed[K](ch: K): State[K] => (State[K], Boolean) = { now =>
      now.get(ch) match {
        case Some((Unsubscribing, c)) =>
          if (c > 0) {
            (now.updated(ch, (Subscribing, c)), false)
          } else {
            (now.removed(ch), true)
          }
        case _ => //unexpected
          sys.error(s"unexpected state in unsubscribed")
      }
    }
  }

  trait VoidListener[K, V] extends RedisPubSubListener[K, V] {
    override def message(channel: K, message: V): Unit = ()

    override def message(pattern: K, channel: K, message: V): Unit = ()

    override def subscribed(channel: K, count: Long): Unit = ()

    override def psubscribed(pattern: K, count: Long): Unit = ()

    override def unsubscribed(channel: K, count: Long): Unit = ()

    override def punsubscribed(pattern: K, count: Long): Unit = ()
  }

}

trait AutoSubscriberApiOps {
  implicit class ManagedPubSubOps2[F[_] : Async](val base: ConnectionResource2[F, RedisURI, RedisPubSubF]) {
    def stream[K, V](codec: RedisCodec[K, V], uri: RedisURI): Resource[F, ManagedPubSubF[F, K, V]] =
      Dispatcher[F].flatMap(d => stream(codec, uri, d))

    def stream[K, V](codec: RedisCodec[K, V], uri: RedisURI, d: Dispatcher[F]): Resource[F, ManagedPubSubF[F, K, V]] =
      ManagedPubSubF.create(base(codec, uri), d)
  }

  implicit class ManagedPubSubOps1[F[_] : Async](val base: ConnectionResource1[F, RedisPubSubF]) {
    def stream[K, V](codec: RedisCodec[K, V]): Resource[F, ManagedPubSubF[F, K, V]] =
      Dispatcher[F].flatMap(d => stream(codec, d))

    def stream[K, V](codec: RedisCodec[K, V], d: Dispatcher[F]): Resource[F, ManagedPubSubF[F, K, V]] =
      ManagedPubSubF.create(base(codec), d)
  }
}