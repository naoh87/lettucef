package io.slettuce.core.commands

import cats.effect.kernel.Async
import io.lettuce.core.RedisFuture
import io.slettuce.core.util.ManualDispatchHelper
import io.slettuce.core.util.{JavaFutureUtil => JF}
import scala.reflect.ClassTag

trait AsyncCallCommands[F[_], K, V] {
  implicit protected val _async: Async[F]
  implicit protected val _valueTag: ClassTag[V]
  implicit protected val _keyTag: ClassTag[K]

  protected val underlying: Any

  protected val dispatchHelper: ManualDispatchHelper[K, V]

  protected def call[R](f: underlying.type => RedisFuture[R]): F[R] =
    JF.toAsync(f(underlying))
}
