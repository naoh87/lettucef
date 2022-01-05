package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.Functor
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.core.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.RedisClientF.ConnectionResource
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


class RedisClientF[F[_]](underlying: RedisClient)(implicit F: Async[F]) {
  def connect[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], redisURI: RedisURI): ConnectionResource[F, RedisConnectionF[F, K, V]] =
    ConnectionResource.make(
      JavaFutureUtil.toAsync(underlying.connectAsync(codec, redisURI)).map(c => new RedisConnectionF(c, codec))
    )(_.closeAsync())

  def connectPubSub[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], redisURI: RedisURI): ConnectionResource[F, RedisPubSubF[F, K, V]] =
    ConnectionResource.make(
      RedisPubSubF.createUnsafe(JavaFutureUtil.toAsync(underlying.connectPubSubAsync(codec, redisURI)).map(locally))
    )(_.closeAsync())

  def connectSentinel[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], redisURI: RedisURI): ConnectionResource[F, RedisSentinelCommandsF[F, K, V]] =
    ConnectionResource.make(
      JavaFutureUtil.toAsync(underlying.connectSentinelAsync(codec, redisURI))
        .map(new RedisSentinelCommandsF(_, codec))
    )(_.closeAsync())

  def connectMasterReplica[K: ClassTag, V: ClassTag](codec: RedisCodec[K, V], redisURI: Seq[RedisURI]): ConnectionResource[F, MasterReplicaRedisConnectionF[F, K, V]] =
    ConnectionResource.make(
      JavaFutureUtil.toAsync(MasterReplica.connectAsync(underlying, codec, redisURI.asJava))
        .map(new MasterReplicaRedisConnectionF(_, codec))
    )(_.closeAsync())


  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toAsync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

object RedisClientF {
  class ConnectionResource[F[_] : Functor, A](acquire: F[A], release: A => F[Unit]) {
    def resource: Resource[F, A] = Resource.make(acquire)(release)

    def unsafe: F[A] = acquire

    def withRelease: F[(A, F[Unit])] = acquire.map(a => (a, release(a)))
  }

  object ConnectionResource {
    def make[F[_] : Functor, A](acquire: F[A])(release: A => F[Unit]) =
      new ConnectionResource(acquire, release)
  }
}

class RedisConnectionF[F[_] : Async, K: ClassTag, V: ClassTag](
  underlying: StatefulRedisConnection[K, V],
  codec: RedisCodec[K, V]
) {
  def async(): RedisCommandsF[F, K, V] =
    new RedisCommandsF[F, K, V](underlying.async(), codec)

  def stream(): RedisStreamCommandsF[F, K, V] =
    new RedisStreamCommandsF(async())

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toAsync(underlying.closeAsync()).void
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
