// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.commands

import java.util.Date
import cats.syntax.functor._
import dev.naoh.lettucef.core.models._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.FlushMode
import io.lettuce.core.KillArgs
import io.lettuce.core.TrackingArgs
import io.lettuce.core.UnblockType
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait ServerCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisServerAsyncCommands[K, V]
  
  def bgrewriteaof(): F[String] =
    JF.toAsync(underlying.bgrewriteaof())
  
  def bgsave(): F[String] =
    JF.toAsync(underlying.bgsave())
  
  def clientCaching(enabled: Boolean): F[String] =
    JF.toAsync(underlying.clientCaching(enabled))
  
  def clientGetname(): F[K] =
    JF.toAsync(underlying.clientGetname())
  
  def clientGetredir(): F[Long] =
    JF.toAsync(underlying.clientGetredir()).map(Long2long)
  
  def clientId(): F[Long] =
    JF.toAsync(underlying.clientId()).map(Long2long)
  
  def clientKill(addr: String): F[String] =
    JF.toAsync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[Long] =
    JF.toAsync(underlying.clientKill(killArgs)).map(Long2long)
  
  def clientList(): F[String] =
    JF.toAsync(underlying.clientList())
  
  def clientPause(timeout: Long): F[String] =
    JF.toAsync(underlying.clientPause(timeout))
  
  def clientSetname(name: K): F[String] =
    JF.toAsync(underlying.clientSetname(name))
  
  def clientTracking(args: TrackingArgs): F[String] =
    JF.toAsync(underlying.clientTracking(args))
  
  def clientUnblock(id: Long, tpe: UnblockType): F[Long] =
    JF.toAsync(underlying.clientUnblock(id, tpe)).map(Long2long)
  
  def command(): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.command()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def commandCount(): F[Long] =
    JF.toAsync(underlying.commandCount()).map(Long2long)
  
  def commandInfo(commands: String*): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.commandInfo(commands: _*)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def configGet(parameter: String): F[Map[String, String]] =
    JF.toAsync(underlying.configGet(parameter)).map(_.asScala.toMap)
  
  def configResetstat(): F[String] =
    JF.toAsync(underlying.configResetstat())
  
  def configRewrite(): F[String] =
    JF.toAsync(underlying.configRewrite())
  
  def configSet(parameter: String, value: String): F[String] =
    JF.toAsync(underlying.configSet(parameter, value))
  
  def dbsize(): F[Long] =
    JF.toAsync(underlying.dbsize()).map(Long2long)
  
  def debugCrashAndRecover(delay: Long): F[String] =
    JF.toAsync(underlying.debugCrashAndRecover(delay))
  
  def debugHtstats(db: Int): F[String] =
    JF.toAsync(underlying.debugHtstats(db))
  
  def debugObject(key: K): F[String] =
    JF.toAsync(underlying.debugObject(key))
  
  def debugReload(): F[String] =
    JF.toAsync(underlying.debugReload())
  
  def debugRestart(delay: Long): F[String] =
    JF.toAsync(underlying.debugRestart(delay))
  
  def debugSdslen(key: K): F[String] =
    JF.toAsync(underlying.debugSdslen(key))
  
  def flushall(): F[String] =
    JF.toAsync(underlying.flushall())
  
  def flushall(flushMode: FlushMode): F[String] =
    JF.toAsync(underlying.flushall(flushMode))
  
  def flushdb(): F[String] =
    JF.toAsync(underlying.flushdb())
  
  def flushdb(flushMode: FlushMode): F[String] =
    JF.toAsync(underlying.flushdb(flushMode))
  
  def info(): F[String] =
    JF.toAsync(underlying.info())
  
  def info(section: String): F[String] =
    JF.toAsync(underlying.info(section))
  
  def lastsave(): F[Date] =
    JF.toAsync(underlying.lastsave())
  
  def memoryUsage(key: K): F[Long] =
    JF.toAsync(underlying.memoryUsage(key)).map(Long2long)
  
  def save(): F[String] =
    JF.toAsync(underlying.save())
  
  def slaveof(host: String, port: Int): F[String] =
    JF.toAsync(underlying.slaveof(host, port))
  
  def slaveofNoOne(): F[String] =
    JF.toAsync(underlying.slaveofNoOne())
  
  def slowlogGet(): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.slowlogGet()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def slowlogGet(count: Int): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.slowlogGet(count)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def slowlogLen(): F[Long] =
    JF.toAsync(underlying.slowlogLen()).map(Long2long)
  
  def slowlogReset(): F[String] =
    JF.toAsync(underlying.slowlogReset())
  
  def time(): F[Seq[V]] =
    JF.toAsync(underlying.time()).map(_.asScala.toSeq)
  
}
