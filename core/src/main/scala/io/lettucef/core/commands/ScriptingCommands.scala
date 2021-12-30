package io.lettucef.core.commands

import cats.syntax.functor._
import io.lettuce.core.FlushMode
import io.lettuce.core.api.async.BaseRedisAsyncCommands
import io.lettuce.core.api.async.RedisScriptingAsyncCommands
import io.lettuce.core.protocol.CommandType
import io.lettucef.core.models.RedisData
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._

trait ScriptingCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V] with RedisScriptingAsyncCommands[K, V]

  /**
   * Eval Lua Script
   *
   * Data type conversion is slightly odd. see[https://redis.io/commands/eval]
   * Lettuce/Redis cant recognize "Redis status reply" or "Redis bulk reply" now (lettuce-core:6.1.5, redis: 6.2.1).
   */
  def eval(script: String, keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVAL,
      dispatchHelper.createRedisDataOutput(),
      dispatchHelper.createArgs().add(script).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    ))

  /**
   * Eval Lua Script
   *
   * Data type conversion is slightly odd. see[https://redis.io/commands/eval]
   * Lettuce/Redis cant recognize "Redis status reply" or "Redis bulk reply" now (lettuce-core:6.1.5, redis: 6.2.1).
   */
  def eval(script: Array[Byte], keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVAL,
      dispatchHelper.createRedisDataOutput(),
      dispatchHelper.createArgs().add(script).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    ))

  def evalsha(digest: String, keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVALSHA,
      dispatchHelper.createRedisDataOutput(),
      dispatchHelper.createArgs().add(digest).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    ))

  def scriptExists(digests: String*): F[Seq[Boolean]] =
    JF.toAsync(underlying.scriptExists(digests: _*)).map(_.asScala.toSeq.map(Boolean2boolean))

  def scriptFlush(): F[String] =
    JF.toAsync(underlying.scriptFlush())

  def scriptFlush(flushMode: FlushMode): F[String] =
    JF.toAsync(underlying.scriptFlush(flushMode))

  def scriptKill(): F[String] =
    JF.toAsync(underlying.scriptKill())

  def scriptLoad(script: String): F[String] =
    JF.toAsync(underlying.scriptLoad(script))

  def scriptLoad(script: Array[Byte]): F[String] =
    JF.toAsync(underlying.scriptLoad(script))
}
