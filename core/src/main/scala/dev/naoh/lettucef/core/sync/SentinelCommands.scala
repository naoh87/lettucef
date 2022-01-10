// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import java.net.SocketAddress
import cats.syntax.functor._
import dev.naoh.lettucef.core.sync.SyncCallCommands
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.KillArgs
import io.lettuce.core.api.async._
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands
import scala.jdk.CollectionConverters._


trait SentinelCommands[F[_], K, V] extends SyncCallCommands[F, K, V] {

  protected val underlying: RedisSentinelAsyncCommands[K, V]
  
  def getMasterAddrByName(key: K): F[SocketAddress] =
    JF.toSync(underlying.getMasterAddrByName(key))
  
  def masters(): F[Seq[Map[K, V]]] =
    JF.toSync(underlying.masters()).map(_.asScala.toSeq.map(_.asScala.toMap))
  
  def master(key: K): F[Map[K, V]] =
    JF.toSync(underlying.master(key)).map(_.asScala.toMap)
  
  def slaves(key: K): F[Seq[Map[K, V]]] =
    JF.toSync(underlying.slaves(key)).map(_.asScala.toSeq.map(_.asScala.toMap))
  
  def reset(key: K): F[Long] =
    JF.toSync(underlying.reset(key)).map(Long2long)
  
  def failover(key: K): F[String] =
    JF.toSync(underlying.failover(key))
  
  def monitor(key: K, ip: String, port: Int, quorum: Int): F[String] =
    JF.toSync(underlying.monitor(key, ip, port, quorum))
  
  def set(key: K, option: String, value: V): F[String] =
    JF.toSync(underlying.set(key, option, value))
  
  def remove(key: K): F[String] =
    JF.toSync(underlying.remove(key))
  
  def clientGetname(): F[K] =
    JF.toSync(underlying.clientGetname())
  
  def clientSetname(name: K): F[String] =
    JF.toSync(underlying.clientSetname(name))
  
  def clientKill(addr: String): F[String] =
    JF.toSync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[Long] =
    JF.toSync(underlying.clientKill(killArgs)).map(Long2long)
  
  def clientPause(timeout: Long): F[String] =
    JF.toSync(underlying.clientPause(timeout))
  
  def clientList(): F[String] =
    JF.toSync(underlying.clientList())
  
  def info(): F[String] =
    JF.toSync(underlying.info())
  
  def info(section: String): F[String] =
    JF.toSync(underlying.info(section))
  
  def ping(): F[String] =
    JF.toSync(underlying.ping())
  
}
