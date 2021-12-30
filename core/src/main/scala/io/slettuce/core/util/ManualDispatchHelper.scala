package io.slettuce.core.util

import io.lettuce.core.ScriptOutputType
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.output.CommandOutput
import io.lettuce.core.protocol.BaseRedisCommandBuilder
import io.lettuce.core.protocol.CommandArgs


final class ManualDispatchHelper[K, V](codec: RedisCodec[K, V]) extends BaseRedisCommandBuilder[K, V](codec) {
  def createScriptOutput[A](tpe: ScriptOutputType): CommandOutput[K, V, A] = newScriptOutput(codec, tpe)

  def createArgs(): CommandArgs[K, V] = new CommandArgs[K, V](codec)
}