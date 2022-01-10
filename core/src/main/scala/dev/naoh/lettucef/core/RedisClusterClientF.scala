package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.Functor
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.api.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.RedisClusterClientF.ConnectionResource1
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.ReadFrom
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.cluster.models.partitions.Partitions
import io.lettuce.core.codec.RedisCodec
import scala.reflect.ClassTag
import scala.util.chaining._

class RedisClusterClientF[F[_]](underlying: RedisClusterClient)(implicit F: Async[F]) {

  val connect: ConnectionResource1[F, RedisClusterConnectionF] =
    new ConnectionResource1[F, RedisClusterConnectionF] {
      override protected def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[(RedisClusterConnectionF[F, K, V], F[Unit])] =
        JavaFutureUtil.toSync(underlying.connectAsync(codec))
          .map(new RedisClusterConnectionF(_, codec).pipe(c => c -> c.closeAsync()))
    }

  val connectPubSub: ConnectionResource1[F, RedisPubSubF] =
    new ConnectionResource1[F, RedisPubSubF] {
      override protected def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[(RedisPubSubF[F, K, V], F[Unit])] =
        RedisPubSubF.createUnsafe[F, K, V](JavaFutureUtil.toSync(underlying.connectPubSubAsync(codec)).map(locally))
          .map(r => r -> r.closeAsync())
    }

  def getPartition: F[Partitions] =
    F.blocking(underlying.getPartitions)

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toSync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

object RedisClusterClientF {
  abstract class ConnectionResource1[F[_] : Functor, R[_[_], _, _]] {
    protected def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[(R[F, K, V], F[Unit])]

    def apply[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): Resource[F, R[F, K, V]] =
      Resource.make(allocate(codec))(_._2).map(_._1)

    def unsafe[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V]): F[R[F, K, V]] =
      allocate(codec).map(_._1)
  }

}

class RedisClusterConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisClusterConnection[K, V],
  codec: RedisCodec[K, V]
) {
  def sync(): RedisClusterSyncCommandsF[F, K, V] =
    new RedisClusterSyncCommandsF[F, K, V](underlying.async(), codec)

  def getConnection(nodeId: String): F[RedisConnectionF[F, K, V]] =
    JavaFutureUtil.toSync(underlying.getConnectionAsync(nodeId))
      .map(new RedisConnectionF(_, codec))

  def getConnection(host: String, port: Int): F[RedisConnectionF[F, K, V]] =
    JavaFutureUtil.toSync(underlying.getConnectionAsync(host, port))
      .map(new RedisConnectionF(_, codec))

  def setReadFrom(readFrom: ReadFrom): F[Unit] =
    Async[F].delay(underlying.setReadFrom(readFrom))

  def getReadFrom: F[ReadFrom] =
    Async[F].delay(underlying.getReadFrom)

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toSync(underlying.closeAsync()).void
}

