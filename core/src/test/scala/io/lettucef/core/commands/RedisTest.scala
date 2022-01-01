package io.lettucef.core.commands

import java.nio.ByteBuffer
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.protocol.ProtocolVersion
import io.lettucef.core.RedisClientF
import io.lettucef.core.RedisClusterCommandsF

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
      .resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      .flatMap(_.connect(codec).map(_.async()))
      .use(f)
      .unsafeRunSync()(IORuntime.global)
}
