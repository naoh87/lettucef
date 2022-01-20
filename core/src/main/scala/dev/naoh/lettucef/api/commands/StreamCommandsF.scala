// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.api.models.stream._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.Consumer
import io.lettuce.core.Limit
import io.lettuce.core.XAddArgs
import io.lettuce.core.XAutoClaimArgs
import io.lettuce.core.XClaimArgs
import io.lettuce.core.XGroupCreateArgs
import io.lettuce.core.XPendingArgs
import io.lettuce.core.XReadArgs
import io.lettuce.core.XReadArgs.StreamOffset
import io.lettuce.core.XTrimArgs
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait StreamCommandsF[F[_], K, V] {

  def xack(key: K, group: K, messageIds: String*): F[Long]
  
  def xadd(key: K, body: Map[K, V]): F[String]
  
  def xadd(key: K, args: XAddArgs, body: Map[K, V]): F[String]
  
  def xautoclaim(key: K, args: XAutoClaimArgs[K]): F[ClaimedMessages[K, V]]
  
  def xclaim(key: K, consumer: Consumer[K], minIdleTime: Long, messageIds: String*): F[Seq[StreamMessage[K, V]]]
  
  def xclaim(key: K, consumer: Consumer[K], args: XClaimArgs, messageIds: String*): F[Seq[StreamMessage[K, V]]]
  
  def xdel(key: K, messageIds: String*): F[Long]
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K): F[String]
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K, args: XGroupCreateArgs): F[String]
  
  def xgroupCreateconsumer(key: K, consumer: Consumer[K]): F[Boolean]
  
  def xgroupDelconsumer(key: K, consumer: Consumer[K]): F[Long]
  
  def xgroupDestroy(key: K, group: K): F[Boolean]
  
  def xgroupSetid(streamOffset: StreamOffset[K], group: K): F[String]
  
  def xinfoStream(key: K): F[List[RedisData[V]]]
  
  def xinfoGroups(key: K): F[List[RedisData[V]]]
  
  def xinfoConsumers(key: K, group: K): F[List[RedisData[V]]]
  
  def xlen(key: K): F[Long]
  
  def xpending(key: K, group: K): F[PendingMessages]
  
  def xpending(key: K, group: K, range: RedisRange[String], limit: Limit): F[Seq[PendingMessage]]
  
  def xpending(key: K, consumer: Consumer[K], range: RedisRange[String], limit: Limit): F[Seq[PendingMessage]]
  
  def xpending(key: K, args: XPendingArgs[K]): F[Seq[PendingMessage]]
  
  def xrange(key: K, range: RedisRange[String]): F[Seq[StreamMessage[K, V]]]
  
  def xrange(key: K, range: RedisRange[String], limit: Limit): F[Seq[StreamMessage[K, V]]]
  
  def xread(streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]]
  
  def xread(args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]]
  
  def xreadgroup(consumer: Consumer[K], streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]]
  
  def xreadgroup(consumer: Consumer[K], args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]]
  
  def xrevrange(key: K, range: RedisRange[String]): F[Seq[StreamMessage[K, V]]]
  
  def xrevrange(key: K, range: RedisRange[String], limit: Limit): F[Seq[StreamMessage[K, V]]]
  
  def xtrim(key: K, count: Long): F[Long]
  
  def xtrim(key: K, approximateTrimming: Boolean, count: Long): F[Long]
  
  def xtrim(key: K, args: XTrimArgs): F[Long]
  
}
