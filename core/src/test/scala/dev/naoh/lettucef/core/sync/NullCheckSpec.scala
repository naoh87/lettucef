package dev.naoh.lettucef.core.sync

import cats.effect.IO
import dev.naoh.lettucef.core.sync.RedisTest.syntax._
import io.lettuce.core.GeoArgs
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class NullCheckSpec extends  AnyFreeSpec with Matchers {


  private val emptyKey = "empty".asKey

  def expectNone(io: IO[Option[_]]): IO[Unit] =
    io.map(_ shouldBe None)

  def notNull[R](io: IO[R]): IO[Option[R]] =
    io.map { s =>
      Option(s) shouldBe a[Some[_]]
      Option(s)
    }

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
      _ <- expectNone(r.zpopmin(emptyKey))
      _ <- notNull(r.getrange(emptyKey, 3, 3))
      _ <- expectNone(r.zrandmember(emptyKey))
      _ <- expectNone(r.zrank(emptyKey, "hoge".asValue))
      _ <- expectNone(r.zrevrank(emptyKey, "hoge".asValue))
      _ <- expectNone(r.zscore(emptyKey, "hoge".asValue))
      ret <- r.zmscore(emptyKey, "hoge".asValue, "piyo".asValue)
      _ = ret shouldBe List(None, None)
    } yield {

    }
  }

}
