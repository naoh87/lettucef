package io.slettuce.core.util

import java.util.concurrent.CompletionStage
import cats.effect.Async

object JavaFutureUtil {
  def toAsync[F[_] : Async, A](fut: => CompletionStage[A]): F[A] =
    Async[F].async_(cb => fut.handle((a, e) => if (e eq null) cb(Right(a)) else cb(Left(e))))
}
