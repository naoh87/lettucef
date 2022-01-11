// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.FlushMode
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait ScriptingCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisScriptingAsyncCommands[K, V]
  
  def scriptExists(digests: String*): F[F[Seq[Boolean]]] =
    JF.toAsync(underlying.scriptExists(digests: _*)).map(_.map(_.asScala.toSeq.map(Boolean2boolean)))
  
  def scriptFlush(): F[F[String]] =
    JF.toAsync(underlying.scriptFlush())
  
  def scriptFlush(flushMode: FlushMode): F[F[String]] =
    JF.toAsync(underlying.scriptFlush(flushMode))
  
  def scriptKill(): F[F[String]] =
    JF.toAsync(underlying.scriptKill())
  
  def scriptLoad(script: String): F[F[String]] =
    JF.toAsync(underlying.scriptLoad(script))
  
  def scriptLoad(script: Array[Byte]): F[F[String]] =
    JF.toAsync(underlying.scriptLoad(script))
  
}
