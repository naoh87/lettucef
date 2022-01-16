// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.FlushMode
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait ScriptingCommandsF[F[_], K, V] {

  def scriptExists(digests: String*): F[Seq[Boolean]]
  
  def scriptFlush(): F[String]
  
  def scriptFlush(flushMode: FlushMode): F[String]
  
  def scriptKill(): F[String]
  
  def scriptLoad(script: String): F[String]
  
  def scriptLoad(script: Array[Byte]): F[String]
  
}
