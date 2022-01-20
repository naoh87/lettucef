// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.SentinelCommandsF
import dev.naoh.lettucef.api.Commands
import java.net.SocketAddress
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.KillArgs
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands
import scala.jdk.CollectionConverters._


trait SentinelCommands[F[_], K, V] extends CommandsDeps[F, K, V] with SentinelCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisSentinelAsyncCommands[K, V]
  
  def getMasterAddrByName(key: K): F[F[SocketAddress]] =
    JF.toAsync(underlying.getMasterAddrByName(key))
  
  def masters(): F[F[Seq[Map[K, V]]]] =
    JF.toAsync(underlying.masters()).map(_.map(_.asScala.toSeq.map(_.asScala.toMap)))
  
  def master(key: K): F[F[Map[K, V]]] =
    JF.toAsync(underlying.master(key)).map(_.map(_.asScala.toMap))
  
  def slaves(key: K): F[F[Seq[Map[K, V]]]] =
    JF.toAsync(underlying.slaves(key)).map(_.map(_.asScala.toSeq.map(_.asScala.toMap)))
  
  def reset(key: K): F[F[Long]] =
    JF.toAsync(underlying.reset(key)).map(_.map(Long2long))
  
  def failover(key: K): F[F[String]] =
    JF.toAsync(underlying.failover(key))
  
  def monitor(key: K, ip: String, port: Int, quorum: Int): F[F[String]] =
    JF.toAsync(underlying.monitor(key, ip, port, quorum))
  
  def set(key: K, option: String, value: V): F[F[String]] =
    JF.toAsync(underlying.set(key, option, value))
  
  def remove(key: K): F[F[String]] =
    JF.toAsync(underlying.remove(key))
  
  def clientGetname(): F[F[K]] =
    JF.toAsync(underlying.clientGetname())
  
  def clientSetname(name: K): F[F[String]] =
    JF.toAsync(underlying.clientSetname(name))
  
  def clientKill(addr: String): F[F[String]] =
    JF.toAsync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[F[Long]] =
    JF.toAsync(underlying.clientKill(killArgs)).map(_.map(Long2long))
  
  def clientPause(timeout: Long): F[F[String]] =
    JF.toAsync(underlying.clientPause(timeout))
  
  def clientList(): F[F[String]] =
    JF.toAsync(underlying.clientList())
  
  def info(): F[F[String]] =
    JF.toAsync(underlying.info())
  
  def info(section: String): F[F[String]] =
    JF.toAsync(underlying.info(section))
  
  def ping(): F[F[String]] =
    JF.toAsync(underlying.ping())
  
}
