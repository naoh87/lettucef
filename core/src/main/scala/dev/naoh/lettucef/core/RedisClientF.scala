package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.functor._
import dev.naoh.lettucef.api.LettuceF.ShutdownConfig
import dev.naoh.lettucef.core.util.JavaFutureUtil
import io.lettuce.core.ReadFrom
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.masterreplica.MasterReplica
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection
import scala.jdk.CollectionConverters._


class RedisClientF[F[_]](underlying: RedisClient)(implicit F: Async[F]) {
  def connect[K, V](codec: RedisCodec[K, V]): Resource[F, RedisConnectionF[F, K, V]] =
    Resource.make(
      F.blocking(underlying.connect(codec)).map(new RedisConnectionF(_, codec)))(
      _.closeAsync())

  def connect[K, V](codec: RedisCodec[K, V], uri: RedisURI): Resource[F, RedisConnectionF[F, K, V]] =
    Resource.make(
      JavaFutureUtil.toSync(underlying.connectAsync(codec, uri)).map(new RedisConnectionF(_, codec)))(
      _.closeAsync())

  def connectPubSub[K, V](codec: RedisCodec[K, V]): Resource[F, RedisPubSubF[F, K, V]] =
    Resource.make(
      F.blocking(underlying.connectPubSub(codec))
        .map(new RedisPubSubF(_)))(
      _.closeAsync())

  def connectPubSub[K, V](codec: RedisCodec[K, V], uri: RedisURI): Resource[F, RedisPubSubF[F, K, V]] =
    Resource.make(
      JavaFutureUtil.toSync(underlying.connectPubSubAsync(codec, uri))
        .map(new RedisPubSubF(_)))(
      _.closeAsync())

  def connectSentinel[K, V](codec: RedisCodec[K, V], uri: RedisURI): Resource[F, RedisSentinelCommandsF[F, K, V]] =
    Resource.make(
      JavaFutureUtil.toSync(underlying.connectSentinelAsync(codec, uri))
        .map(new RedisSentinelCommandsF(_, codec)))(
      _.closeAsync())

  def connectMasterReplica[K, V](codec: RedisCodec[K, V], uris: Seq[RedisURI]): Resource[F, MasterReplicaRedisConnectionF[F, K, V]] =
    Resource.make(
      JavaFutureUtil.toSync(MasterReplica.connectAsync(underlying, codec, uris.asJava))
        .map(new MasterReplicaRedisConnectionF(_, codec)))(
      _.closeAsync())

  def shutdownAsync(config: ShutdownConfig): F[Unit] =
    shutdownAsync(config.quietPeriod, config.timeout, config.timeUnit)

  def shutdownAsync(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit): F[Unit] =
    JavaFutureUtil.toSync(underlying.shutdownAsync(quietPeriod, timeout, timeUnit)).void
}

class RedisConnectionF[F[_] : Async, K, V](
  underlying: StatefulRedisConnection[K, V],
  codec: RedisCodec[K, V]
) extends CommonConnectionF[F, K, V] {
  private[this] val _sync = new RedisSyncCommandsF[F, K, V](underlying.async(), codec)

  def sync(): RedisSyncCommandsF[F, K, V] = _sync

  private[this] val _async = new RedisAsyncCommandsF[F, K, V](underlying.async(), codec)

  def async(): RedisAsyncCommandsF[F, K, V] = _async

  def closeAsync(): F[Unit] =
    JavaFutureUtil.toSync(underlying.closeAsync()).void
}

class MasterReplicaRedisConnectionF[F[_] : Async, K, V](
  underlying: StatefulRedisMasterReplicaConnection[K, V],
  codec: RedisCodec[K, V]
) extends RedisConnectionF[F, K, V](underlying, codec) {
  def setReadFrom(readFrom: ReadFrom): F[Unit] =
    Async[F].delay(underlying.setReadFrom(readFrom))

  def getReadFrom: F[ReadFrom] =
    Async[F].delay(underlying.getReadFrom)
}
