package dev.naoh.lettucef.api.extras

import cats.effect.Resource
import cats.effect.kernel.Concurrent
import dev.naoh.lettucef.extras.GenResourcePool

case class ResourcePool(maxIdle: Int, minIdle: Int) {
  def make[F[_] : Concurrent, A](source: Resource[F, A]): Resource[F, Resource[F, A]] =
    GenResourcePool.create(this, source)
}

object ResourcePool {
  case object PoolLifecycleViolation extends RuntimeException("LifecycleViolation")
}