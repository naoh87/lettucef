package dev.naoh.lettucef.core.commands

import cats.effect.IO
import io.lettuce.core.SetArgs
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class AsyncCommandsSpec extends AnyFreeSpec with Matchers {

  import RedisTest.syntax._

  "get set" in RedisTest.commands { c =>
    for {
      _ <- c.flushdb() >> IO.sleep(1.seconds)
      get1 <- c.get("key".asKey)
      set1 <- c.set("key".asKey, "hoge".asValue, SetArgs.Builder.nx())
      get2 <- c.get("key".asKey)
      set2 <- c.set("key".asKey, "fuga".asValue, SetArgs.Builder.nx())
    } yield {
      get1 shouldBe None
      set1 shouldBe Some("OK")
      get2 shouldBe Some("hoge".asValue)
      set2 shouldBe None
    }
  }
}


