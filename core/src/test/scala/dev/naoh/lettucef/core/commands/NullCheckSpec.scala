package dev.naoh.lettucef.core.commands

import cats.effect.IO
import dev.naoh.lettucef.core.commands.RedisTest.syntax._
import io.lettuce.core.GeoArgs
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class NullCheckSpec extends  AnyFreeSpec with Matchers {


  private val emptyKey = "empty".asKey

  def expectNone(io: IO[Option[_]]): IO[Unit] =
    io.map(_ shouldBe None)

  def notNull[R](io: IO[R]): IO[Unit] =
    io.map(Option(_) shouldBe a[Some[_]])

  "keys" in RedisTest.commands { r =>
    for {
      _ <- r.flushdb() >> IO.sleep(1.seconds)
      _ <- expectNone(r.dump(emptyKey))
      _ <- expectNone(r.objectEncoding(emptyKey))
      _ <- expectNone(r.geodist(emptyKey, "a".asValue, "b".asValue, GeoArgs.Unit.m))
      _ <- expectNone(r.hget(emptyKey, emptyKey))
      _ <- expectNone(r.hrandfield(emptyKey))
      _ <- notNull(r.rpop(emptyKey, 1))
      _ <- expectNone(r.bzpopmin(0.1, emptyKey))
      _ <- expectNone(r.blpop(0.1, emptyKey))
    } yield {

    }
  }

}
