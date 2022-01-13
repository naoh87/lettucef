package dev.naoh.lettucef.core.util

import java.util.concurrent.CompletionException
import java.util.concurrent.CompletionStage
import cats.effect.Async
import cats.syntax.functor._

object JavaFutureUtil {
  def toSync[F[_] : Async, A](fut: => CompletionStage[A]): F[A] =
    Async[F].async_(cb => fut.whenComplete((a, e) =>
      e match {
        case null => cb(Right(a))
        case ex: CompletionException if ex.getCause ne null => cb(Left(ex))
        case ex => cb(Left(ex))
      }))

  def toAsync[F[_] : Async, A](fut: => CompletionStage[A]): F[F[A]] =
    Async[F].delay(fut).map(cs => Async[F].async_(cb => cs.whenComplete((a, e) =>
      e match {
        case null => cb(Right(a))
        case ex: CompletionException if ex.getCause ne null => cb(Left(ex))
        case ex => cb(Left(ex))
      })))
}
