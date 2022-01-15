package dev.naoh.lettucef.api.extras

import cats.effect.Resource
import cats.effect.kernel.Concurrent
import dev.naoh.lettucef.extras.ResourcePoolFactory

case class ResourcePool(maxIdle: Int) {
  def make[F[_] : Concurrent, A](source: Resource[F, A]): Resource[F, Resource[F, A]] =
    ResourcePoolFactory.create(this, source)
}

object ResourcePool {
  case object PoolLifecycleViolation extends RuntimeException("LifecycleViolation")
}