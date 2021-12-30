package io.lettucef.core.util

import io.lettuce.core.ScriptOutputType
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.output.CommandOutput
import io.lettuce.core.protocol.BaseRedisCommandBuilder
import io.lettuce.core.protocol.CommandArgs


final class ManualDispatchHelper[K, V](val redisCodec: RedisCodec[K, V]) extends BaseRedisCommandBuilder[K, V](redisCodec) {
  def createScriptOutput[A](tpe: ScriptOutputType): CommandOutput[K, V, A] = newScriptOutput(redisCodec, tpe)

  def createArgs(): CommandArgs[K, V] = new CommandArgs[K, V](redisCodec)
}