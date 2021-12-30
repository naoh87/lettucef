// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import io.lettucef.core.models._
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait BaseCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V]
  
  def publish(channel: K, message: V): F[Long] =
    JF.toAsync(underlying.publish(channel, message)).map(Long2long)
  
  def pubsubChannels(): F[Seq[K]] =
    JF.toAsync(underlying.pubsubChannels()).map(_.asScala.toSeq)
  
  def pubsubChannels(channel: K): F[Seq[K]] =
    JF.toAsync(underlying.pubsubChannels(channel)).map(_.asScala.toSeq)
  
  def pubsubNumsub(channels: K*): F[Map[K, Long]] =
    JF.toAsync(underlying.pubsubNumsub(channels: _*)).map(_.asScala.view.mapValues(Long2long).toMap)
  
  def pubsubNumpat(): F[Long] =
    JF.toAsync(underlying.pubsubNumpat()).map(Long2long)
  
  def echo(msg: V): F[V] =
    JF.toAsync(underlying.echo(msg))
  
  def role(): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.role()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def ping(): F[String] =
    JF.toAsync(underlying.ping())
  
  def readOnly(): F[String] =
    JF.toAsync(underlying.readOnly())
  
  def readWrite(): F[String] =
    JF.toAsync(underlying.readWrite())
  
  def quit(): F[String] =
    JF.toAsync(underlying.quit())
  
  def waitForReplication(replicas: Int, timeout: Long): F[Long] =
    JF.toAsync(underlying.waitForReplication(replicas, timeout)).map(Long2long)
  
}

