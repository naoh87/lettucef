package dev.naoh.lettucef.core.sync

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import scala.reflect.ClassTag

trait SyncCallCommands[F[_], K, V] {
  implicit protected val _async: Async[F]
  implicit protected val _valueTag: ClassTag[V]
  implicit protected val _keyTag: ClassTag[K]

  protected val underlying: Any

  protected val dispatchHelper: ManualDispatchHelper[K, V]
}
