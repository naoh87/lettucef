// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait BaseCommandsF[F[_], K, V] {

  def publish(channel: K, message: V): F[Long]
  
  def pubsubChannels(): F[Seq[K]]
  
  def pubsubChannels(channel: K): F[Seq[K]]
  
  def pubsubNumsub(channels: K*): F[Map[K, Long]]
  
  def pubsubNumpat(): F[Long]
  
  def echo(msg: V): F[V]
  
  def role(): F[List[RedisData[V]]]
  
  def ping(): F[String]
  
  def readOnly(): F[String]
  
  def readWrite(): F[String]
  
  def quit(): F[String]
  
  def waitForReplication(replicas: Int, timeout: Long): F[Long]
  
}
