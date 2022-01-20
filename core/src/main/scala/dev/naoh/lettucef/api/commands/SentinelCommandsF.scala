// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import java.net.SocketAddress
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.KillArgs
import io.lettuce.core.api.async._
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands
import scala.jdk.CollectionConverters._


trait SentinelCommandsF[F[_], K, V] {

  def getMasterAddrByName(key: K): F[SocketAddress]
  
  def masters(): F[Seq[Map[K, V]]]
  
  def master(key: K): F[Map[K, V]]
  
  def slaves(key: K): F[Seq[Map[K, V]]]
  
  def reset(key: K): F[Long]
  
  def failover(key: K): F[String]
  
  def monitor(key: K, ip: String, port: Int, quorum: Int): F[String]
  
  def set(key: K, option: String, value: V): F[String]
  
  def remove(key: K): F[String]
  
  def clientGetname(): F[K]
  
  def clientSetname(name: K): F[String]
  
  def clientKill(addr: String): F[String]
  
  def clientKill(killArgs: KillArgs): F[Long]
  
  def clientPause(timeout: Long): F[String]
  
  def clientList(): F[String]
  
  def info(): F[String]
  
  def info(section: String): F[String]
  
  def ping(): F[String]
  
}
