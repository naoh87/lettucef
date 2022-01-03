package dev.naoh.lettucef.core.commands

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.RedisFuture
import scala.reflect.ClassTag

trait AsyncCallCommands[F[_], K, V] {
  implicit protected val _async: Async[F]
  implicit protected val _valueTag: ClassTag[V]
  implicit protected val _keyTag: ClassTag[K]

  protected val underlying: Any

  protected val dispatchHelper: ManualDispatchHelper[K, V]

  protected def call[R](f: underlying.type => RedisFuture[R]): F[R] =
    JF.blocking(f(underlying))
}
