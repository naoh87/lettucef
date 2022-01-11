package dev.naoh.lettucef.core

import cats.effect.Async
import cats.effect.Resource
import cats.effect.std.Dispatcher
import cats.syntax.functor._
import dev.naoh.lettucef.api.models.pubsub.PushedMessage
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection

class RedisPubSubF[F[_], K, V](
  underlying: StatefulRedisPubSubConnection[K, V]
)(implicit F: Async[F]) {
  def subscribe(channels: K*): F[Unit] =
    JavaFutureUtil.toSync(underlying.async().subscribe(channels: _*)).void

  def unsubscribe(channels: K*): F[Unit] =
    JavaFutureUtil.toSync(underlying.async().unsubscribe(channels: _*)).void

  def psubscribe(patterns: K*): F[Unit] =
    JavaFutureUtil.toSync(underlying.async().psubscribe(patterns: _*)).void

  def punsubscribe(patterns: K*): F[Unit] =
    JavaFutureUtil.toSync(underlying.async().punsubscribe(patterns: _*)).void

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toSync(underlying.closeAsync()).void

  def addListener(listener: RedisPubSubListener[K, V]): F[Unit] =
    F.delay(underlying.addListener(listener))

  def removeListener(listener: RedisPubSubListener[K, V]): F[Unit] =
    F.delay(underlying.removeListener(listener))

  def setListener(listener: RedisPubSubListener[K, V]): Resource[F, F[Unit]] =
    Resource.eval(Async[F].memoize(removeListener(listener)))
      .flatMap(remove => Resource.make(addListener(listener).as(remove))(identity))
}

object RedisPubSubF {

  def makeListener[F[_], K, V](
    f: PushedMessage[K, V] => F[Unit],
    dispatcher: Dispatcher[F]
  ): RedisPubSubListener[K, V] =
    new RedisPubSubListener[K, V] {
      override def message(channel: K, message: V): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.Message(channel, message)))

      override def subscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.Subscribed(channel, count)))

      override def unsubscribed(channel: K, count: Long): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.Unsubscribed(channel, count)))

      override def message(pattern: K, channel: K, message: V): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.PMessage(pattern, channel, message)))

      override def psubscribed(pattern: K, count: Long): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.PSubscribed(pattern, count)))

      override def punsubscribed(pattern: K, count: Long): Unit =
        dispatcher.unsafeRunSync(f(PushedMessage.PUnsubscribed(pattern, count)))
    }
}
