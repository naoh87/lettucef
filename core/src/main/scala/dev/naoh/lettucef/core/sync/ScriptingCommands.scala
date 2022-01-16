// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.ScriptingCommandsF
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.FlushMode
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait ScriptingCommands[F[_], K, V] extends CommandsDeps[F, K, V] with ScriptingCommandsF[F, K, V] {

  protected val underlying: RedisScriptingAsyncCommands[K, V]
  
  def scriptExists(digests: String*): F[Seq[Boolean]] =
    JF.toSync(underlying.scriptExists(digests: _*)).map(_.asScala.toSeq.map(Boolean2boolean))
  
  def scriptFlush(): F[String] =
    JF.toSync(underlying.scriptFlush())
  
  def scriptFlush(flushMode: FlushMode): F[String] =
    JF.toSync(underlying.scriptFlush(flushMode))
  
  def scriptKill(): F[String] =
    JF.toSync(underlying.scriptKill())
  
  def scriptLoad(script: String): F[String] =
    JF.toSync(underlying.scriptLoad(script))
  
  def scriptLoad(script: Array[Byte]): F[String] =
    JF.toSync(underlying.scriptLoad(script))
  
}
