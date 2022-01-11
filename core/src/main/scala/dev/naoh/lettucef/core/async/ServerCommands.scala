// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import java.util.Date
import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.FlushMode
import io.lettuce.core.KillArgs
import io.lettuce.core.TrackingArgs
import io.lettuce.core.UnblockType
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait ServerCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisServerAsyncCommands[K, V]
  
  def bgrewriteaof(): F[F[String]] =
    JF.toAsync(underlying.bgrewriteaof())
  
  def bgsave(): F[F[String]] =
    JF.toAsync(underlying.bgsave())
  
  def clientCaching(enabled: Boolean): F[F[String]] =
    JF.toAsync(underlying.clientCaching(enabled))
  
  def clientGetname(): F[F[K]] =
    JF.toAsync(underlying.clientGetname())
  
  def clientGetredir(): F[F[Long]] =
    JF.toAsync(underlying.clientGetredir()).map(_.map(Long2long))
  
  def clientId(): F[F[Long]] =
    JF.toAsync(underlying.clientId()).map(_.map(Long2long))
  
  def clientKill(addr: String): F[F[String]] =
    JF.toAsync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[F[Long]] =
    JF.toAsync(underlying.clientKill(killArgs)).map(_.map(Long2long))
  
  def clientList(): F[F[String]] =
    JF.toAsync(underlying.clientList())
  
  def clientPause(timeout: Long): F[F[String]] =
    JF.toAsync(underlying.clientPause(timeout))
  
  def clientSetname(name: K): F[F[String]] =
    JF.toAsync(underlying.clientSetname(name))
  
  def clientTracking(args: TrackingArgs): F[F[String]] =
    JF.toAsync(underlying.clientTracking(args))
  
  def clientUnblock(id: Long, tpe: UnblockType): F[F[Long]] =
    JF.toAsync(underlying.clientUnblock(id, tpe)).map(_.map(Long2long))
  
  def command(): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.command()).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def commandCount(): F[F[Long]] =
    JF.toAsync(underlying.commandCount()).map(_.map(Long2long))
  
  def commandInfo(commands: String*): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.commandInfo(commands: _*)).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def configGet(parameter: String): F[F[Map[String, String]]] =
    JF.toAsync(underlying.configGet(parameter)).map(_.map(_.asScala.toMap))
  
  def configResetstat(): F[F[String]] =
    JF.toAsync(underlying.configResetstat())
  
  def configRewrite(): F[F[String]] =
    JF.toAsync(underlying.configRewrite())
  
  def configSet(parameter: String, value: String): F[F[String]] =
    JF.toAsync(underlying.configSet(parameter, value))
  
  def dbsize(): F[F[Long]] =
    JF.toAsync(underlying.dbsize()).map(_.map(Long2long))
  
  def debugCrashAndRecover(delay: Long): F[F[String]] =
    JF.toAsync(underlying.debugCrashAndRecover(delay))
  
  def debugHtstats(db: Int): F[F[String]] =
    JF.toAsync(underlying.debugHtstats(db))
  
  def debugObject(key: K): F[F[String]] =
    JF.toAsync(underlying.debugObject(key))
  
  def debugReload(): F[F[String]] =
    JF.toAsync(underlying.debugReload())
  
  def debugRestart(delay: Long): F[F[String]] =
    JF.toAsync(underlying.debugRestart(delay))
  
  def debugSdslen(key: K): F[F[String]] =
    JF.toAsync(underlying.debugSdslen(key))
  
  def flushall(): F[F[String]] =
    JF.toAsync(underlying.flushall())
  
  def flushall(flushMode: FlushMode): F[F[String]] =
    JF.toAsync(underlying.flushall(flushMode))
  
  def flushdb(): F[F[String]] =
    JF.toAsync(underlying.flushdb())
  
  def flushdb(flushMode: FlushMode): F[F[String]] =
    JF.toAsync(underlying.flushdb(flushMode))
  
  def info(): F[F[String]] =
    JF.toAsync(underlying.info())
  
  def info(section: String): F[F[String]] =
    JF.toAsync(underlying.info(section))
  
  def lastsave(): F[F[Date]] =
    JF.toAsync(underlying.lastsave())
  
  def memoryUsage(key: K): F[F[Long]] =
    JF.toAsync(underlying.memoryUsage(key)).map(_.map(Long2long))
  
  def save(): F[F[String]] =
    JF.toAsync(underlying.save())
  
  def slaveof(host: String, port: Int): F[F[String]] =
    JF.toAsync(underlying.slaveof(host, port))
  
  def slaveofNoOne(): F[F[String]] =
    JF.toAsync(underlying.slaveofNoOne())
  
  def slowlogGet(): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.slowlogGet()).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def slowlogGet(count: Int): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.slowlogGet(count)).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def slowlogLen(): F[F[Long]] =
    JF.toAsync(underlying.slowlogLen()).map(_.map(Long2long))
  
  def slowlogReset(): F[F[String]] =
    JF.toAsync(underlying.slowlogReset())
  
  def time(): F[F[Seq[V]]] =
    JF.toAsync(underlying.time()).map(_.map(_.asScala.toSeq))
  
}
