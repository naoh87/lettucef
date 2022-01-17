// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

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
import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._
import scala.util.chaining._


trait ServerCommandsF[F[_], K, V] {

  def bgrewriteaof(): F[String]
  
  def bgsave(): F[String]
  
  def clientCaching(enabled: Boolean): F[String]
  
  def clientGetname(): F[K]
  
  def clientGetredir(): F[Long]
  
  def clientId(): F[Long]
  
  def clientKill(addr: String): F[String]
  
  def clientKill(killArgs: KillArgs): F[Long]
  
  def clientList(): F[String]
  
  def clientPause(timeout: Long): F[String]
  
  def clientSetname(name: K): F[String]
  
  def clientTracking(args: TrackingArgs): F[String]
  
  def clientUnblock(id: Long, tpe: UnblockType): F[Long]
  
  def command(): F[List[RedisData[V]]]
  
  def commandCount(): F[Long]
  
  def commandInfo(commands: String*): F[List[RedisData[V]]]
  
  def configGet(parameter: String): F[Map[String, String]]
  
  def configResetstat(): F[String]
  
  def configRewrite(): F[String]
  
  def configSet(parameter: String, value: String): F[String]
  
  def dbsize(): F[Long]
  
  def debugCrashAndRecover(delay: Long): F[String]
  
  def debugHtstats(db: Int): F[String]
  
  def debugObject(key: K): F[String]
  
  def debugReload(): F[String]
  
  def debugRestart(delay: Long): F[String]
  
  def debugSdslen(key: K): F[String]
  
  def flushall(): F[String]
  
  def flushall(flushMode: FlushMode): F[String]
  
  def flushdb(): F[String]
  
  def flushdb(flushMode: FlushMode): F[String]
  
  def info(): F[String]
  
  def info(section: String): F[String]
  
  def lastsave(): F[Date]
  
  def memoryUsage(key: K): F[Long]
  
  def save(): F[String]
  
  def slaveof(host: String, port: Int): F[String]
  
  def slaveofNoOne(): F[String]
  
  def slowlogGet(): F[List[RedisData[V]]]
  
  def slowlogGet(count: Int): F[List[RedisData[V]]]
  
  def slowlogLen(): F[Long]
  
  def slowlogReset(): F[String]
  
  def time(): F[Seq[V]]
  
}