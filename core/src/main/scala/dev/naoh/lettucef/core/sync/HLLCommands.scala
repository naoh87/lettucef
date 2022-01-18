// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.HLLCommandsF
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._


trait HLLCommands[F[_], K, V] extends CommandsDeps[F, K, V] with HLLCommandsF[F, K, V] {

  protected val underlying: RedisHLLAsyncCommands[K, V]
  
  def pfadd(key: K, values: V*): F[Long] =
    JF.toSync(underlying.pfadd(key, values: _*)).map(Long2long)
  
  def pfmerge(destkey: K, sourcekeys: K*): F[String] =
    JF.toSync(underlying.pfmerge(destkey, sourcekeys: _*))
  
  def pfcount(keys: K*): F[Long] =
    JF.toSync(underlying.pfcount(keys: _*)).map(Long2long)
  
}
