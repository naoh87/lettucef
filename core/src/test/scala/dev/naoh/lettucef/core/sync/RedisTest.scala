package dev.naoh.lettucef.core.sync

import java.nio.ByteBuffer
import cats.effect.IO
import cats.effect.Resource
import cats.effect.unsafe.IORuntime
import dev.naoh.lettucef.api.Commands
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.core.RedisPubSubF
import io.lettuce.core.RedisClient
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec

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

  type CommandF = Commands.CommonSyncCommandsF[IO, RedisKey, RedisValue]
  type PubSubF = RedisPubSubF[IO, RedisKey, RedisValue]

  def commands[R](f: CommandF => IO[R]): R =
    LettuceF
      .client[IO](RedisClient.create("redis://127.0.0.1:6379"))
      .flatMap(_.connect(codec).map(_.sync()))
      .use(f)
      .unsafeRunSync()(IORuntime.global)

  def runWith[R](f: (Resource[IO, CommandF], Resource[IO, PubSubF]) => Resource[IO, IO[R]]): R =
    LettuceF
      .client[IO](RedisClient.create("redis://127.0.0.1:6379"))
      .use(c => f(c.connect(codec).map(_.sync()), c.connectPubSub(codec)).use(identity))
      .unsafeRunSync()(IORuntime.global)
}
