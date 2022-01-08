// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.commands

import java.net.SocketAddress
import cats.syntax.functor._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.KillArgs
import io.lettuce.core.api.async._
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands
import scala.jdk.CollectionConverters._


trait SentinelCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisSentinelAsyncCommands[K, V]
  
  def getMasterAddrByName(key: K): F[SocketAddress] =
    JF.toAsync(underlying.getMasterAddrByName(key))
  
  def masters(): F[Seq[Map[K, V]]] =
    JF.toAsync(underlying.masters()).map(_.asScala.toSeq.map(_.asScala.toMap))
  
  def master(key: K): F[Map[K, V]] =
    JF.toAsync(underlying.master(key)).map(_.asScala.toMap)
  
  def slaves(key: K): F[Seq[Map[K, V]]] =
    JF.toAsync(underlying.slaves(key)).map(_.asScala.toSeq.map(_.asScala.toMap))
  
  def reset(key: K): F[Long] =
    JF.toAsync(underlying.reset(key)).map(Long2long)
  
  def failover(key: K): F[String] =
    JF.toAsync(underlying.failover(key))
  
  def monitor(key: K, ip: String, port: Int, quorum: Int): F[String] =
    JF.toAsync(underlying.monitor(key, ip, port, quorum))
  
  def set(key: K, option: String, value: V): F[String] =
    JF.toAsync(underlying.set(key, option, value))
  
  def remove(key: K): F[String] =
    JF.toAsync(underlying.remove(key))
  
  def clientGetname(): F[K] =
    JF.toAsync(underlying.clientGetname())
  
  def clientSetname(name: K): F[String] =
    JF.toAsync(underlying.clientSetname(name))
  
  def clientKill(addr: String): F[String] =
    JF.toAsync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[Long] =
    JF.toAsync(underlying.clientKill(killArgs)).map(Long2long)
  
  def clientPause(timeout: Long): F[String] =
    JF.toAsync(underlying.clientPause(timeout))
  
  def clientList(): F[String] =
    JF.toAsync(underlying.clientList())
  
  def info(): F[String] =
    JF.toAsync(underlying.info())
  
  def info(section: String): F[String] =
    JF.toAsync(underlying.info(section))
  
  def ping(): F[String] =
    JF.toAsync(underlying.ping())
  
}
