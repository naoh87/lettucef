package io.lettucef.core.commands

import java.nio.ByteBuffer
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import io.lettuce.core.Range.Boundary
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.protocol.ProtocolVersion
import io.lettucef.core.RedisClientF
import io.lettucef.core.RedisClusterCommandsF
import io.lettucef.core.models.RedisData.RedisArray
import io.lettucef.core.models.RedisRange
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class StreamCommandsSpec extends AnyFreeSpec with Matchers {

  import RedisTest.syntax._

  "aa" in RedisTest.commands { c =>
    for {
      _ <- c.flushdb() >> IO.sleep(1.seconds)
      _ <- c.xadd("key".asKey, Map("c".asKey -> "1".asValue))
      _ <- c.xadd("key".asKey, Map("c".asKey -> "2".asValue))
      r <- c.xinfoStream("key".asKey)
    } yield {
      println(RedisArray(r.toList))
    }
  }

  "bb" in RedisTest.commands { c =>
    val script = """return {false, {true}, {{"F", "fuga"}, 123}}"""
    for {
      r <- c.eval(script, Nil, Nil)
      _ <- c.zadd("fuga".asKey, (1 to 5).map(i => i.toDouble -> i.toString.asValue): _*)
      fuga <- c.zrangebyscore("fuga".asKey, RedisRange.inclusive(3d).openEnd)
      g <- c.evalsha(c.digest(script), Nil, Nil)
    } yield {
      println(fuga)
      r shouldBe g
      println("result: " + r)
    }
  }
}

object RedisTest {
  val codec: RedisCodec[RedisKey, RedisValue] =
    new RedisCodec[RedisKey, RedisValue] {
      override def decodeKey(bytes: ByteBuffer): RedisKey =
        RedisKey(StringCodec.UTF8.decodeKey(bytes))

      override def decodeValue(bytes: ByteBuffer): RedisValue =
        RedisValue(StringCodec.UTF8.decodeValue(bytes))

      override def encodeKey(key: RedisKey): ByteBuffer =
        StringCodec.UTF8.encodeKey(key.key)

      override def encodeValue(value: RedisValue): ByteBuffer =
        StringCodec.UTF8.encodeValue(value.value)
    }

  case class RedisKey(key: String)

  case class RedisValue(value: String) {
    override def toString: String = value
  }

  object syntax {
    implicit class StringOps(expr: String) {
      def asKey: RedisKey = RedisKey(expr)

      def asValue: RedisValue = RedisValue(expr)
    }
  }

  import scala.util.chaining._

  def commands[R](f: RedisClusterCommandsF[IO, RedisKey, RedisValue] => IO[R]): R =
    RedisClientF
      .resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000").tap(_.setOptions(ClusterClientOptions.builder().protocolVersion(ProtocolVersion.RESP3).build())))
      .flatMap(_.connect(codec).map(_.async()))
      .use(f)
      .unsafeRunSync()(IORuntime.global)
}
