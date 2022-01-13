package dev.naoh.lettucef.api

import java.util.concurrent.TimeUnit
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.flatMap._
import dev.naoh.lettucef.core.RedisClientF
import dev.naoh.lettucef.core.RedisClusterClientF
import io.lettuce.core.RedisClient
import io.lettuce.core.cluster.RedisClusterClient

object LettuceF {
  case class ShutdownConfig(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit)

  object ShutdownConfig {
    val default: ShutdownConfig = ShutdownConfig(0, 2, TimeUnit.SECONDS)
  }

  def cluster[F[_] : Async](
    client: => RedisClusterClient,
    shutdownConfig: ShutdownConfig = ShutdownConfig.default
  ): Resource[F, RedisClusterClientF[F]] =
    Resource
      .make(
        Sync[F]
          .delay(new RedisClusterClientF[F](client))
          .flatTap(_.getPartition))(
        _.shutdownAsync(shutdownConfig))

  def client[F[_] : Async](
    client: => RedisClient,
    shutdownConfig: ShutdownConfig = ShutdownConfig.default
  ): Resource[F, RedisClientF[F]] =
    Resource
      .make(
        Sync[F].delay(new RedisClientF[F](client)))(
        _.shutdownAsync(shutdownConfig))
}


