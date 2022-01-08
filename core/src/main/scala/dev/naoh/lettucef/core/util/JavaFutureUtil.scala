package dev.naoh.lettucef.core.util

import java.util.concurrent.CompletionStage
import cats.effect.Async
import cats.syntax.functor._

object JavaFutureUtil {
  def toAsync[F[_] : Async, A](fut: => CompletionStage[A]): F[A] =
    Async[F].async_(cb => fut.handle((a, e) => if (e eq null) cb(Right(a)) else cb(Left(e))))

  def blocking[F[_], A](fut: => CompletionStage[A])(implicit F: Async[F]): F[A] =
    F.async(cb => F.blocking {
      fut.handle((a, e) => if (e eq null) cb(Right(a)) else cb(Left(e)))
      None
    })
}
