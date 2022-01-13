package app

import cats.effect.IO
import cats.syntax.parallel._

object BenchTask {

  val P = 50
  val R = 200

  def parallelTask[A](task: IO[A], repeat: Int): IO[Unit] =
    List.fill(P)(task.replicateA_(repeat)).parSequence_
}
