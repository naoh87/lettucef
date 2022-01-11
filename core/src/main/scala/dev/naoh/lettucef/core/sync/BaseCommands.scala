// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait BaseCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V]
  
  def publish(channel: K, message: V): F[Long] =
    JF.toSync(underlying.publish(channel, message)).map(Long2long)
  
  def pubsubChannels(): F[Seq[K]] =
    JF.toSync(underlying.pubsubChannels()).map(_.asScala.toSeq)
  
  def pubsubChannels(channel: K): F[Seq[K]] =
    JF.toSync(underlying.pubsubChannels(channel)).map(_.asScala.toSeq)
  
  def pubsubNumsub(channels: K*): F[Map[K, Long]] =
    JF.toSync(underlying.pubsubNumsub(channels: _*)).map(_.asScala.view.mapValues(Long2long).toMap)
  
  def pubsubNumpat(): F[Long] =
    JF.toSync(underlying.pubsubNumpat()).map(Long2long)
  
  def echo(msg: V): F[V] =
    JF.toSync(underlying.echo(msg))
  
  def role(): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.role()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def ping(): F[String] =
    JF.toSync(underlying.ping())
  
  def readOnly(): F[String] =
    JF.toSync(underlying.readOnly())
  
  def readWrite(): F[String] =
    JF.toSync(underlying.readWrite())
  
  def quit(): F[String] =
    JF.toSync(underlying.quit())
  
  def waitForReplication(replicas: Int, timeout: Long): F[Long] =
    JF.toSync(underlying.waitForReplication(replicas, timeout)).map(Long2long)
  
}
