// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.LettuceValueConverter
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait HLLCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisHLLAsyncCommands[K, V]
  
  def pfadd(key: K, values: V*): F[Long] =
    JF.toAsync(underlying.pfadd(key, values: _*)).map(Long2long)
  
  def pfmerge(destkey: K, sourcekeys: K*): F[String] =
    JF.toAsync(underlying.pfmerge(destkey, sourcekeys: _*))
  
  def pfcount(keys: K*): F[Long] =
    JF.toAsync(underlying.pfcount(keys: _*)).map(Long2long)
  
}
