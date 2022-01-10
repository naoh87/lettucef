package dev.naoh.lettucef.core.util

import java.util.concurrent.CompletionStage
import cats.effect.Async
import cats.syntax.functor._

object JavaFutureUtil {
  def toSync[F[_] : Async, A](fut: => CompletionStage[A]): F[A] =
    Async[F].async_(cb => fut.whenComplete((a, e) => if (e eq null) cb(Right(a)) else cb(Left(e))))

  def toAsync[F[_] : Async, A](fut: => CompletionStage[A]): F[F[A]] =
    Async[F].delay(fut).map(cs => Async[F].async_(cb => cs.whenComplete((a, e) => if (e eq null) cb(Right(a)) else cb(Left(e)))))
}
