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
import cats.syntax.flatMap._
import io.lettuce.core.cluster.models.partitions.Partitions
import io.slettuce.core.RedisClientF.ShutdownConfig
import scala.reflect.ClassTag

object RedisClientF {
  case class ShutdownConfig(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit)

  def resource[F[_] : Async](
    make: => RedisClusterClient,
    shutdownConfig: ShutdownConfig = ShutdownConfig(0, 2, TimeUnit.SECONDS)
  ): Resource[F, RedisClientF[F]] =
    Resource
      .make(
        Sync[F]
          .delay(new RedisClientF[F](make))
          .flatTap(_.getPartition)
      )(_.shutdownAsync(shutdownConfig))
}

class RedisClientF[F[_]](underlying: RedisClusterClient)(implicit F: Async[F]) {
  def connect[K, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisClusterConnectionF[F, K, V]] =
    Resource.make(connectRaw(codec))(_.closeAsync())

  def connectRaw[K, V: ClassTag](codec: RedisCodec[K, V]): F[RedisClusterConnectionF[F, K, V]] =
    JavaFutureUtil.toAsync(underlying.connectAsync(codec))
      .map(c => new RedisClusterConnectionF(c))

  def getPartition: F[Partitions] =
    F.blocking(underlying.getPartitions)

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toAsync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

class RedisClusterConnectionF[F[_] : Async, K, V: ClassTag](underlying: StatefulRedisClusterConnection[K, V]) {
  def async(): RedisClusterCommandsF[F, K, V] =
    new RedisClusterCommandsF[F, K, V](underlying.async())

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toAsync(underlying.closeAsync()).void
}

