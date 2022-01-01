package io.lettucef.core

import cats.effect.Async
import cats.effect.Deferred
import cats.effect.Resource
import cats.effect.std.Dispatcher
import cats.syntax.flatMap._
import cats.syntax.functor._
import fs2._
import fs2.concurrent.Channel
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import io.lettucef.core.models.pubsub.PushedMessage
import io.lettucef.core.util.JavaFutureUtil
import scala.jdk.CollectionConverters._

class RedisPubSubF[F[_], K, V](
  underlying: StatefulRedisPubSubConnection[K, V],
  shutdown: Deferred[F, Boolean]
)(implicit F: Async[F]) {
  def subscribe(channels: K*): F[Unit] =
    JavaFutureUtil.toAsync(underlying.async().subscribe(channels: _*)).void

  def unsubscribe(channels: K*): F[Unit] =
    JavaFutureUtil.toAsync(underlying.async().unsubscribe(channels: _*)).void

  def pubsubChannels(): F[Seq[K]] =
    JavaFutureUtil.toAsync(underlying.async().pubsubChannels()).map(_.asScala.toSeq)

  def pubsubNumsub(channels: K*): F[Map[K, Long]] =
    JavaFutureUtil.toAsync(underlying.async().pubsubNumsub(channels: _*)).map(_.asScala.view.mapValues(Long2long).toMap)

  def closeAsync(): F[Unit] =
    F.guarantee(JavaFutureUtil.toAsync(underlying.closeAsync()).void, shutdown.complete(true).void)

  /**
   * Start Listen published stream
   *
   * You should pull Stream or cause OOM error
   */
  def startListen(): Resource[F, Stream[F, PushedMessage[K, V]]] =
    for {
      ch <- Resource.make(Channel.unbounded[F, PushedMessage[K, V]])(_.close.void) //This may cause OOM
      _ <- F.background(shutdown.get >> ch.close)
      dispatcher <- Dispatcher[F]
      remove <- registerListener(RedisPubSubF.makeListener(ch, dispatcher))
    } yield ch.stream.onFinalize(remove)

  def listen(): Stream[F, PushedMessage[K, V]] =
    Stream.resource(startListen()).flatten

  private def registerListener(listener: RedisPubSubListener[K, V]): Resource[F, F[Unit]] =
    Resource.make(
      F.delay(underlying.addListener(listener))
        .as(F.delay(underlying.removeListener(listener))))(identity)
}

object RedisPubSubF {

  def create[F[_] : Async, K, V](connect: F[StatefulRedisPubSubConnection[K, V]]): Resource[F, RedisPubSubF[F, K, V]] =
    for {
      d <- Resource.eval(Deferred[F, Boolean])
      pubsub <- Resource.make(connect.map(c => new RedisPubSubF(c, d)))(_.closeAsync())
    } yield pubsub

  /**
   * make Lettuce PubSubListener to send PushedMessage
   *
   * TODO: implement overflow strategy
   */
  private def makeListener[F[_], K, V](ch: Channel[F, PushedMessage[K, V]], d: Dispatcher[F]): RedisPubSubListener[K, V] =
    new RedisPubSubListener[K, V] {
      override def message(channel: K, message: V): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.Message(channel, message)))

      override def subscribed(channel: K, count: Long): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.Subscribed(channel, count)))

      override def unsubscribed(channel: K, count: Long): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.Unsubscribed(channel, count)))

      override def message(pattern: K, channel: K, message: V): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.PMessage(pattern, channel, message)))

      override def psubscribed(pattern: K, count: Long): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.PSubscribed(pattern, count)))

      override def punsubscribed(pattern: K, count: Long): Unit =
        d.unsafeRunSync(ch.send(PushedMessage.PUnsubscribed(pattern, count)))
    }
}
