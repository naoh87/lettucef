package dev.naoh.lettucef.core.util

import java.nio.ByteBuffer
import dev.naoh.lettucef.core.models.RedisData
import io.lettuce.core.ScriptOutputType
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.output.CommandOutput
import io.lettuce.core.protocol.BaseRedisCommandBuilder
import io.lettuce.core.protocol.CommandArgs
import scala.collection.mutable
import scala.collection.mutable.ListBuffer


final class ManualDispatchHelper[K, V](val redisCodec: RedisCodec[K, V]) extends BaseRedisCommandBuilder[K, V](redisCodec) {
  def createScriptOutput[A](tpe: ScriptOutputType): CommandOutput[K, V, A] =
    newScriptOutput(redisCodec, tpe)

  def createRedisDataOutput(): CommandOutput[K, V, RedisData[V]] =
    new RedisDataOutput[K, V](codec)

  def createArgs(): CommandArgs[K, V] = new CommandArgs[K, V](redisCodec)
}

import RedisData._

final class RedisDataOutput[K, V](codec: RedisCodec[K, V]) extends CommandOutput[K, V, RedisData[V]](codec, RedisNull) {
  private[this] var current: ListBuffer[RedisData[V]] = _
  private[this] var stack: scala.collection.mutable.Stack[ListBuffer[RedisData[V]]] = _

  override def set(integer: Long): Unit =
    put(RedisInteger(integer))

  override def set(double: Double): Unit =
    put(RedisDouble(double))

  override def set(boolean: Boolean): Unit =
    put(RedisBoolean(boolean))

  override def setSingle(bytes: ByteBuffer): Unit =
    put(if (bytes eq null) RedisNull else RedisString(StringCodec.UTF8.decodeValue(bytes)))

  override def set(bytes: ByteBuffer): Unit =
    put(if (bytes eq null) RedisNull else RedisBulk(codec.decodeValue(bytes)))

  override def setError(error: String): Unit = {
    val e = RedisError(error)
    if (current eq null) {
      this.error = error
      output = e
    } else {
      current.addOne(e)
    }
  }

  override def setError(error: ByteBuffer): Unit =
    setError(decodeAscii(error))

  @inline private[this] def put(data: RedisData[V]): Unit =
    if (current eq null) {
      output = data
    } else {
      current.addOne(data)
    }

  override def complete(depth: Int): Unit =
    if (depth > 0) {
      val ary = RedisArray(current.result())
      if (stack.isEmpty) {
        //current = null
        //stack = null
        output = ary
      } else {
        current = stack.pop()
        current.addOne(ary)
      }
    }

  override def multi(count: Int): Unit = {
    if (stack eq null) {
      stack = mutable.Stack.empty
    } else {
      stack.push(current)
    }
    current = ListBuffer.empty
  }
}