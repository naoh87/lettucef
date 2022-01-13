package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.Functor
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.api.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.RedisClientF.ConnectionResource2
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.ReadFrom
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.masterreplica.MasterReplica
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection
import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag
import scala.util.chaining._


class RedisClientF[F[_]](underlying: RedisClient)(implicit F: Async[F]) {
  val connect: ConnectionResource2[F, RedisURI, RedisConnectionF] =
    new ConnectionResource2[F, RedisURI, RedisConnectionF] {
      override def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: RedisURI): F[(RedisConnectionF[F, K, V], F[Unit])] =
        JavaFutureUtil.toSync(underlying.connectAsync(codec, uri))
          .map(new RedisConnectionF(_, codec).pipe(c => c -> c.closeAsync()))
    }

  val connectPubSub: ConnectionResource2[F, RedisURI, RedisPubSubF] =
    new ConnectionResource2[F, RedisURI, RedisPubSubF] {
      override def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: RedisURI): F[(RedisPubSubF[F, K, V], F[Unit])] =
        JavaFutureUtil.toSync(underlying.connectPubSubAsync(codec, uri))
          .map(new RedisPubSubF(_).pipe(c => c -> c.closeAsync()))
    }

  val connectSentinel: ConnectionResource2[F, RedisURI, RedisSentinelCommandsF] =
    new ConnectionResource2[F, RedisURI, RedisSentinelCommandsF] {
      override def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: RedisURI): F[(RedisSentinelCommandsF[F, K, V], F[Unit])] =
        JavaFutureUtil.toSync(underlying.connectSentinelAsync(codec, uri))
          .map(new RedisSentinelCommandsF(_, codec).pipe(c => c -> c.closeAsync()))
    }

  val connectMasterReplica: ConnectionResource2[F, Seq[RedisURI], MasterReplicaRedisConnectionF] =
    new ConnectionResource2[F, Seq[RedisURI], MasterReplicaRedisConnectionF] {
      override def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: Seq[RedisURI]): F[(MasterReplicaRedisConnectionF[F, K, V], F[Unit])] =
        JavaFutureUtil.toSync(MasterReplica.connectAsync(underlying, codec, uri.asJava))
          .map(new MasterReplicaRedisConnectionF(_, codec).pipe(c => c -> c.closeAsync()))
    }

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toSync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

object RedisClientF {

  abstract class ConnectionResource2[F[_] : Functor, A, R[_[_], _, _]] {
    def allocate[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: A): F[(R[F, K, V], F[Unit])]

    def apply[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: A): Resource[F, R[F, K, V]] =
      Resource.make(allocate(codec, uri))(_._2).map(_._1)

    def unsafe[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], uri: A): F[R[F, K, V]] =
      allocate(codec, uri).map(_._1)
  }
}

class RedisConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisConnection[K, V],
  codec: RedisCodec[K, V]
) {
  private[this] val _sync = new RedisSyncCommandsF[F, K, V](underlying.async(), codec)

  def sync(): RedisSyncCommandsF[F, K, V] = _sync

  private[this] val _async = new RedisAsyncCommandsF[F, K, V](underlying.async(), codec)

  def async(): RedisAsyncCommandsF[F, K, V] = _async

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toSync(underlying.closeAsync()).void
}

class MasterReplicaRedisConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisMasterReplicaConnection[K, V],
  codec: RedisCodec[K, V]
) extends RedisConnectionF[F, K, V](underlying, codec) {
  def setReadFrom(readFrom: ReadFrom): F[Unit] =
    Async[F].delay(underlying.setReadFrom(readFrom))

  def getReadFrom: F[ReadFrom] =
    Async[F].delay(underlying.getReadFrom)
}
