package dev.naoh.lettucef.core.commands

import cats.effect.IO
import dev.naoh.lettucef.core.commands.RedisTest.syntax._
import io.lettuce.core.GeoArgs
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class NullCheckSpec extends  AnyFreeSpec with Matchers {


  private val emptyKey = "empty".asKey

  def checkNone(io: IO[Option[_]]): IO[Unit] =
    io.map(_ shouldBe None)

  def notNull[R](io: IO[R]): IO[Unit] =
    io.map(Option(_) shouldBe a[Some[_]])

  "keys" in RedisTest.commands { r =>
    for {
      _ <- r.flushdb() >> IO.sleep(1.seconds)
      _ <- checkNone(r.dump(emptyKey))
      _ <- checkNone(r.objectEncoding(emptyKey))
      _ <- checkNone(r.geodist(emptyKey, "a".asValue, "b".asValue, GeoArgs.Unit.m))
      _ <- checkNone(r.hget(emptyKey, emptyKey))
      _ <- checkNone(r.hrandfield(emptyKey))
      _ <- notNull(r.rpop(emptyKey, 1))
      _ <- notNull(r.bzpopmin(0.1, emptyKey))
    } yield {

    }
  }

}
