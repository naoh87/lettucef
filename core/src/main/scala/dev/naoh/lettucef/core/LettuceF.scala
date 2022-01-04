package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.flatMap._
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.resource.ClientResources

object LettuceF {
  case class ShutdownConfig(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit)

  object ShutdownConfig {
    val default: ShutdownConfig = ShutdownConfig(0, 2, TimeUnit.SECONDS)
  }

  def resource[F[_] : Async](
    client: => RedisClusterClient,
    shutdownConfig: ShutdownConfig = ShutdownConfig.default
  ): Resource[F, RedisClusterClientF[F]] =
    Resource
      .make(
        Sync[F]
          .delay(new RedisClusterClientF[F](client))
          .flatTap(_.getPartition))(
        _.shutdownAsync(shutdownConfig))

  def resource[F[_] : Async](
    uri: RedisURI,
    clientResources: Option[ClientResources],
    shutdownConfig: ShutdownConfig
  ): Resource[F, RedisClientF[F]] =
    Resource
      .make(
        Sync[F].delay(
          new RedisClientF[F](
            clientResources.map(res => RedisClient.create(res, uri)).getOrElse(RedisClient.create(uri)),
            uri)))(
        _.shutdownAsync(shutdownConfig))
}


