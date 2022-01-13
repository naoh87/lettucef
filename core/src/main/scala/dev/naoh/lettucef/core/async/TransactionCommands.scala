// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait TransactionCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisTransactionalAsyncCommands[K, V]
  
  def discard(): F[F[String]] =
    JF.toAsync(underlying.discard())
  
  def exec(): F[F[Boolean]] =
    JF.toAsync(underlying.exec()).map(_.map(!_.wasDiscarded()))
  
  def multi(): F[F[String]] =
    JF.toAsync(underlying.multi())
  
  def watch(keys: K*): F[F[String]] =
    JF.toAsync(underlying.watch(keys: _*))
  
  def unwatch(): F[F[String]] =
    JF.toAsync(underlying.unwatch())
  
}
