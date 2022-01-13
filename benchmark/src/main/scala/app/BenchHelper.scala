package app

import cats.effect.Deferred
import cats.effect.IO
import cats.effect.Resource

object BenchHelper {
  def extractResource[A](res: Resource[IO, A]): IO[(A, IO[Unit])] = {
    for {
      da <- Deferred[IO, A]
      c <- res.use(da.complete(_) >> IO.never).start
      a <- da.get
    } yield a -> c.cancel
  }
}
