package dev.naoh.lettucef.api.commands

import dev.naoh.lettucef.api.models.RedisData

trait EvalScriptingCommandsF[F[_], K, V] {

  /**
   * Eval Lua Script
   *
   * Data type conversion is slightly odd. see[https://redis.io/commands/eval]
   * Lettuce/Redis cant handle "Redis status reply" or "Redis bulk reply" now (lettuce-core:6.1.5, redis: 6.2.1).
   */
  def eval(script: String, keys: Seq[K], values: Seq[V]): F[RedisData[V]]

  /**
   * Eval Lua Script
   *
   * Data type conversion is slightly odd. see[https://redis.io/commands/eval]
   * Lettuce/Redis cant handle "Redis status reply" or "Redis bulk reply" now (lettuce-core:6.1.5, redis: 6.2.1).
   */
  def eval(script: Array[Byte], keys: Seq[K], values: Seq[V]): F[RedisData[V]]

  def evalsha(digest: String, keys: Seq[K], values: Seq[V]): F[RedisData[V]]
}