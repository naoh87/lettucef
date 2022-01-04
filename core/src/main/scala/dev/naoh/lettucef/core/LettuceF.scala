package dev.naoh.lettucef.core

import java.util.concurrent.TimeUnit
import cats.effect.Sync
import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.flatMap._
import io.lettuce.core.cluster.RedisClusterClient

object LettuceF {
  case class ShutdownConfig(quietPeriod: Long, timeout: Long, timeUnit: TimeUnit)

  def resource[F[_] : Async](
    make: => RedisClusterClient,
    shutdownConfig: ShutdownConfig = ShutdownConfig(0, 2, TimeUnit.SECONDS)
  ): Resource[F, RedisClusterClientF[F]] =
    Resource
      .make(
        Sync[F]
          .delay(new RedisClusterClientF[F](make))
          .flatTap(_.getPartition)
      )(_.shutdownAsync(shutdownConfig))
}


