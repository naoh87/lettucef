// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.commands

import cats.syntax.functor._
import dev.naoh.lettucef.core.models.transaction._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait TransactionalCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisTransactionalAsyncCommands[K, V]
  
  def discard(): F[String] =
    JF.toAsync(underlying.discard())
  
  def exec(): F[TransactionResult[V]] =
    JF.toAsync(underlying.exec()).map(TransactionResult.from[V])
  
  def multi(): F[String] =
    JF.toAsync(underlying.multi())
  
  def watch(keys: K*): F[String] =
    JF.toAsync(underlying.watch(keys: _*))
  
  def unwatch(): F[String] =
    JF.toAsync(underlying.unwatch())
  
}
