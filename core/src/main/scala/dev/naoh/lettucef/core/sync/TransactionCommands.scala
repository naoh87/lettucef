// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait TransactionCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisTransactionalAsyncCommands[K, V]
  
  def discard(): F[String] =
    JF.toSync(underlying.discard())
  
  def exec(): F[Boolean] =
    JF.toSync(underlying.exec()).map(!_.wasDiscarded())
  
  def multi(): F[String] =
    JF.toSync(underlying.multi())
  
  def watch(keys: K*): F[String] =
    JF.toSync(underlying.watch(keys: _*))
  
  def unwatch(): F[String] =
    JF.toSync(underlying.unwatch())
  
}
