// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait HLLCommandsF[F[_], K, V] {

  def pfadd(key: K, values: V*): F[Long]
  
  def pfmerge(destkey: K, sourcekeys: K*): F[String]
  
  def pfcount(keys: K*): F[Long]
  
}
