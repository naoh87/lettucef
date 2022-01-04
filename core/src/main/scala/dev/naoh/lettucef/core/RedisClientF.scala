package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.core.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec
import scala.reflect.ClassTag

class RedisClientF[F[_]](underlying: RedisClient, uri: RedisURI)(implicit F: Async[F]) {
  def connect[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisConnectionF[F, K, V]] =
    Resource.make(connectUnsafe(codec))(_.closeAsync())

  def connectUnsafe[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[RedisConnectionF[F, K, V]] =
    JavaFutureUtil.toAsync(underlying.connectAsync(codec, uri))
      .map(c => new RedisConnectionF(c, codec))

  def connectPubSub[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisPubSubF[F, K, V]] =
    RedisPubSubF.create(JavaFutureUtil.toAsync(underlying.connectPubSubAsync(codec, uri)).map(locally))

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toAsync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

class RedisConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisConnection[K, V],
  codec: RedisCodec[K, V]
) {
  def async(): RedisCommandsF[F, K, V] =
    new RedisCommandsF[F, K, V](underlying.async(), codec)

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toAsync(underlying.closeAsync()).void
}
