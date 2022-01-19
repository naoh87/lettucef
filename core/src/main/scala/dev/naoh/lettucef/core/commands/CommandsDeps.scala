package dev.naoh.lettucef.core.commands

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.util.ManualDispatchHelper

trait CommandsDeps[F[_], K, V] {
  implicit protected val _async: Async[F]

  protected val dispatchHelper: ManualDispatchHelper[K, V]
}
