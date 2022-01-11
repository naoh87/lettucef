// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

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
  
  def bgrewriteaof(): F[String] =
    JF.toSync(underlying.bgrewriteaof())
  
  def bgsave(): F[String] =
    JF.toSync(underlying.bgsave())
  
  def clientCaching(enabled: Boolean): F[String] =
    JF.toSync(underlying.clientCaching(enabled))
  
  def clientGetname(): F[K] =
    JF.toSync(underlying.clientGetname())
  
  def clientGetredir(): F[Long] =
    JF.toSync(underlying.clientGetredir()).map(Long2long)
  
  def clientId(): F[Long] =
    JF.toSync(underlying.clientId()).map(Long2long)
  
  def clientKill(addr: String): F[String] =
    JF.toSync(underlying.clientKill(addr))
  
  def clientKill(killArgs: KillArgs): F[Long] =
    JF.toSync(underlying.clientKill(killArgs)).map(Long2long)
  
  def clientList(): F[String] =
    JF.toSync(underlying.clientList())
  
  def clientPause(timeout: Long): F[String] =
    JF.toSync(underlying.clientPause(timeout))
  
  def clientSetname(name: K): F[String] =
    JF.toSync(underlying.clientSetname(name))
  
  def clientTracking(args: TrackingArgs): F[String] =
    JF.toSync(underlying.clientTracking(args))
  
  def clientUnblock(id: Long, tpe: UnblockType): F[Long] =
    JF.toSync(underlying.clientUnblock(id, tpe)).map(Long2long)
  
  def command(): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.command()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def commandCount(): F[Long] =
    JF.toSync(underlying.commandCount()).map(Long2long)
  
  def commandInfo(commands: String*): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.commandInfo(commands: _*)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def configGet(parameter: String): F[Map[String, String]] =
    JF.toSync(underlying.configGet(parameter)).map(_.asScala.toMap)
  
  def configResetstat(): F[String] =
    JF.toSync(underlying.configResetstat())
  
  def configRewrite(): F[String] =
    JF.toSync(underlying.configRewrite())
  
  def configSet(parameter: String, value: String): F[String] =
    JF.toSync(underlying.configSet(parameter, value))
  
  def dbsize(): F[Long] =
    JF.toSync(underlying.dbsize()).map(Long2long)
  
  def debugCrashAndRecover(delay: Long): F[String] =
    JF.toSync(underlying.debugCrashAndRecover(delay))
  
  def debugHtstats(db: Int): F[String] =
    JF.toSync(underlying.debugHtstats(db))
  
  def debugObject(key: K): F[String] =
    JF.toSync(underlying.debugObject(key))
  
  def debugReload(): F[String] =
    JF.toSync(underlying.debugReload())
  
  def debugRestart(delay: Long): F[String] =
    JF.toSync(underlying.debugRestart(delay))
  
  def debugSdslen(key: K): F[String] =
    JF.toSync(underlying.debugSdslen(key))
  
  def flushall(): F[String] =
    JF.toSync(underlying.flushall())
  
  def flushall(flushMode: FlushMode): F[String] =
    JF.toSync(underlying.flushall(flushMode))
  
  def flushdb(): F[String] =
    JF.toSync(underlying.flushdb())
  
  def flushdb(flushMode: FlushMode): F[String] =
    JF.toSync(underlying.flushdb(flushMode))
  
  def info(): F[String] =
    JF.toSync(underlying.info())
  
  def info(section: String): F[String] =
    JF.toSync(underlying.info(section))
  
  def lastsave(): F[Date] =
    JF.toSync(underlying.lastsave())
  
  def memoryUsage(key: K): F[Long] =
    JF.toSync(underlying.memoryUsage(key)).map(Long2long)
  
  def save(): F[String] =
    JF.toSync(underlying.save())
  
  def slaveof(host: String, port: Int): F[String] =
    JF.toSync(underlying.slaveof(host, port))
  
  def slaveofNoOne(): F[String] =
    JF.toSync(underlying.slaveofNoOne())
  
  def slowlogGet(): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.slowlogGet()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def slowlogGet(count: Int): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.slowlogGet(count)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def slowlogLen(): F[Long] =
    JF.toSync(underlying.slowlogLen()).map(Long2long)
  
  def slowlogReset(): F[String] =
    JF.toSync(underlying.slowlogReset())
  
  def time(): F[Seq[V]] =
    JF.toSync(underlying.time()).map(_.asScala.toSeq)
  
}
