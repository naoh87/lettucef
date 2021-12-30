package io.slettuce.core.commands

import java.nio.ByteBuffer
import cats.effect.IO
import cats.effect.Resource
import cats.effect.unsafe.IORuntime
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.slettuce.core.RedisClientF
import io.slettuce.core.RedisClusterCommandsF
import io.slettuce.core.models.RedisData.RedisString
import org.scalatest
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
      println(r)
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

  case class RedisValue(value: String)

  object syntax {
    implicit class StringOps(expr: String) {
      def asKey: RedisKey = RedisKey(expr)
      def asValue: RedisValue = RedisValue(expr)
    }
  }

  def commands[R](f: RedisClusterCommandsF[IO, RedisKey, RedisValue] => IO[R]): R =
    RedisClientF
      .resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      .flatMap(_.connect(codec).map(_.async()))
      .use(f)
      .unsafeRunSync()(IORuntime.global)
}
