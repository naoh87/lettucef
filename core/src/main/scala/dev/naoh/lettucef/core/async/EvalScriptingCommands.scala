package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.models.RedisData
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async.BaseRedisAsyncCommands
import io.lettuce.core.api.async.RedisScriptingAsyncCommands
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._

trait EvalScriptingCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V] with RedisScriptingAsyncCommands[K, V]

  protected val dispatchHelper: ManualDispatchHelper[K, V]

  /**
   * Eval Lua Script
   *
   * Data type conversion is slightly odd. see[https://redis.io/commands/eval]
   * Lettuce/Redis cant recognize "Redis status reply" or "Redis bulk reply" now (lettuce-core:6.1.5, redis: 6.2.1).
   */
  def eval(script: String, keys: Seq[K], values: Seq[V]): F[F[RedisData[V]]] =
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
  def eval(script: Array[Byte], keys: Seq[K], values: Seq[V]): F[F[RedisData[V]]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVAL,
      dispatchHelper.createRedisDataOutput(),
      dispatchHelper.createArgs().add(script).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    ))

  def evalsha(digest: String, keys: Seq[K], values: Seq[V]): F[F[RedisData[V]]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVALSHA,
      dispatchHelper.createRedisDataOutput(),
      dispatchHelper.createArgs().add(digest).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    ))
}