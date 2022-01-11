// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait HLLCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisHLLAsyncCommands[K, V]
  
  def pfadd(key: K, values: V*): F[F[Long]] =
    JF.toAsync(underlying.pfadd(key, values: _*)).map(_.map(Long2long))
  
  def pfmerge(destkey: K, sourcekeys: K*): F[F[String]] =
    JF.toAsync(underlying.pfmerge(destkey, sourcekeys: _*))
  
  def pfcount(keys: K*): F[F[Long]] =
    JF.toAsync(underlying.pfcount(keys: _*)).map(_.map(Long2long))
  
}
