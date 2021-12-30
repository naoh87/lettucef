package io.slettuce.core.commands

import cats.syntax.functor._
import io.lettuce.core.FlushMode
import io.lettuce.core.ScriptOutputType
import io.lettuce.core.api.async.BaseRedisAsyncCommands
import io.lettuce.core.api.async.RedisScriptingAsyncCommands
import io.lettuce.core.protocol.CommandType
import io.slettuce.core.models.RedisData
import io.slettuce.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._

trait ScriptingCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V] with RedisScriptingAsyncCommands[K, V]

  def eval(script: String, tpe: ScriptOutputType, keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVAL,
      dispatchHelper.createScriptOutput[Any](tpe),
      dispatchHelper.createArgs().add(script).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    )).map(RedisData.from[V])

  def eval(script: Array[Byte], tpe: ScriptOutputType, keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVAL,
      dispatchHelper.createScriptOutput[Any](tpe),
      dispatchHelper.createArgs().add(script).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    )).map(RedisData.from[V])

  def evalsha(digest: String, tpe: ScriptOutputType, keys: Seq[K], values: Seq[V]): F[RedisData[V]] =
    JF.toAsync(underlying.dispatch(
      CommandType.EVALSHA,
      dispatchHelper.createScriptOutput[Any](tpe),
      dispatchHelper.createArgs().add(digest).add(keys.length).addKeys(keys.asJava).addValues(values.asJava),
    )).map(RedisData.from[V])

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
