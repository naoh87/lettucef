package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.core.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.ReadFrom
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.cluster.models.partitions.Partitions
import io.lettuce.core.codec.RedisCodec
import scala.reflect.ClassTag

class RedisClusterClientF[F[_]](underlying: RedisClusterClient)(implicit F: Async[F]) {
  def connect[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, RedisClusterConnectionF[F, K, V]] =
    Resource.make(connectUnsafe(codec))(_.closeAsync())

  def connectUnsafe[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[RedisClusterConnectionF[F, K, V]] =
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

  def stream(): RedisStreamCommandsF[F, K, V] =
    new RedisStreamCommandsF(async())

  def getConnection(nodeId: String): F[RedisConnectionF[F, K, V]] =
    JavaFutureUtil.toAsync(underlying.getConnectionAsync(nodeId))
      .map(new RedisConnectionF(_, codec))

  def getConnection(host: String, port: Int): F[RedisConnectionF[F, K, V]] =
    JavaFutureUtil.toAsync(underlying.getConnectionAsync(host, port))
      .map(new RedisConnectionF(_, codec))

  def setReadFrom(readFrom: ReadFrom): F[Unit] =
    Async[F].delay(underlying.setReadFrom(readFrom))

  def getReadFrom: F[ReadFrom] =
    Async[F].delay(underlying.getReadFrom)

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toAsync(underlying.closeAsync()).void
}

