package io.lettucef.core.commands

import java.nio.ByteBuffer
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import io.lettuce.core.SetArgs
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.protocol.ProtocolVersion
import io.lettucef.core.RedisClientF
import io.lettucef.core.RedisClusterCommandsF
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


