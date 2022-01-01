package io.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.cluster.models.partitions.Partitions
import io.lettuce.core.codec.RedisCodec
import io.lettucef.core.RedisClientF.ShutdownConfig
import io.lettucef.core.util.JavaFutureUtil
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
  def connect[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisClusterConnectionF[F, K, V]] =
    Resource.make(connectRaw(codec))(_.closeAsync())

  def connectRaw[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[RedisClusterConnectionF[F, K, V]] =
    JavaFutureUtil.toAsync(underlying.connectAsync(codec))
      .map(c => new RedisClusterConnectionF(c, codec))

  def connectPubSub[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisPubSubF[F, K, V]] =
    RedisPubSubF.create(JavaFutureUtil.toAsync(underlying.connectPubSubAsync(codec)).map(locally))

  def getPartition: F[Partitions] =
    F.blocking(underlying.getPartitions)

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toAsync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

class RedisClusterConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisClusterConnection[K, V],
  codec: RedisCodec[K, V]
) {
  def async(): RedisClusterCommandsF[F, K, V] =
    new RedisClusterCommandsF[F, K, V](underlying.async(), codec)

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toAsync(underlying.closeAsync()).void
}

