// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.models._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait BaseCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: BaseRedisAsyncCommands[K, V]
  
  def publish(channel: K, message: V): F[F[Long]] =
    JF.toAsync(underlying.publish(channel, message)).map(_.map(Long2long))
  
  def pubsubChannels(): F[F[Seq[K]]] =
    JF.toAsync(underlying.pubsubChannels()).map(_.map(_.asScala.toSeq))
  
  def pubsubChannels(channel: K): F[F[Seq[K]]] =
    JF.toAsync(underlying.pubsubChannels(channel)).map(_.map(_.asScala.toSeq))
  
  def pubsubNumsub(channels: K*): F[F[Map[K, Long]]] =
    JF.toAsync(underlying.pubsubNumsub(channels: _*)).map(_.map(_.asScala.view.mapValues(Long2long).toMap))
  
  def pubsubNumpat(): F[F[Long]] =
    JF.toAsync(underlying.pubsubNumpat()).map(_.map(Long2long))
  
  def echo(msg: V): F[F[V]] =
    JF.toAsync(underlying.echo(msg))
  
  def role(): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.role()).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def ping(): F[F[String]] =
    JF.toAsync(underlying.ping())
  
  def readOnly(): F[F[String]] =
    JF.toAsync(underlying.readOnly())
  
  def readWrite(): F[F[String]] =
    JF.toAsync(underlying.readWrite())
  
  def quit(): F[F[String]] =
    JF.toAsync(underlying.quit())
  
  def waitForReplication(replicas: Int, timeout: Long): F[F[Long]] =
    JF.toAsync(underlying.waitForReplication(replicas, timeout)).map(_.map(Long2long))
  
}
