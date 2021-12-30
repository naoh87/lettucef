package io.slettuce.core

import java.util.concurrent.TimeUnit
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.codec.RedisCodec
import io.slettuce.core.util.JavaFutureUtil
import cats.syntax.functor._
import scala.reflect.ClassTag

object RedisClientF {
  case class ShutdownConfig(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit)

  def resource[F[_] : Async](
    make: => RedisClusterClient,
    shutdown: ShutdownConfig = ShutdownConfig(0, 2, TimeUnit.SECONDS)
  ): Resource[F, RedisClientF[F]] =
    Resource
      .make(Sync[F].delay(make))(c =>
        JavaFutureUtil.toAsync(
          c.shutdownAsync(shutdown.quietPeriod, shutdown.timeout, shutdown.timeUnit)).void)
      .map(c => new RedisClientF[F](c))
}

class RedisClientF[F[_]](underlying: RedisClusterClient)(implicit F: Async[F]) {
  def connect[K, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisClusterConnectionF[F, K, V]] =
    Resource.suspend(
      F.blocking(underlying.getPartitions)
        .map(_ =>
          Resource
            .make(
              JavaFutureUtil.toAsync(underlying.connectAsync(codec)))(c =>
              JavaFutureUtil.toAsync(c.closeAsync()).void)
            .map(c => new RedisClusterConnectionF[F, K, V](c))))
}

class RedisClusterConnectionF[F[_] : Async, K, V: ClassTag](underlying: StatefulRedisClusterConnection[K, V]) {
  def async(): RedisClusterCommandsF[F, K, V] =
    new RedisClusterCommandsF[F, K, V](underlying.async())
}

