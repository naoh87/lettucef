// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.StreamCommandsF
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


trait StreamCommands[F[_], K, V] extends CommandsDeps[F, K, V] with StreamCommandsF[F, K, V] {

  protected val underlying: RedisStreamAsyncCommands[K, V] with BaseRedisAsyncCommands[K, V]
  
  def xack(key: K, group: K, messageIds: String*): F[Long] =
    JF.toSync(underlying.xack(key, group, messageIds: _*)).map(Long2long)
  
  def xadd(key: K, body: Map[K, V]): F[String] =
    JF.toSync(underlying.xadd(key, body.asJava))
  
  def xadd(key: K, args: XAddArgs, body: Map[K, V]): F[String] =
    JF.toSync(underlying.xadd(key, args, body.asJava))
  
  def xautoclaim(key: K, args: XAutoClaimArgs[K]): F[ClaimedMessages[K, V]] =
    JF.toSync(underlying.xautoclaim(key, args)).map(ClaimedMessages.from)
  
  def xclaim(key: K, consumer: Consumer[K], minIdleTime: Long, messageIds: String*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xclaim(key, consumer, minIdleTime, messageIds: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xclaim(key: K, consumer: Consumer[K], args: XClaimArgs, messageIds: String*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xclaim(key, consumer, args, messageIds: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xdel(key: K, messageIds: String*): F[Long] =
    JF.toSync(underlying.xdel(key, messageIds: _*)).map(Long2long)
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K): F[String] =
    JF.toSync(underlying.xgroupCreate(streamOffset, group))
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K, args: XGroupCreateArgs): F[String] =
    JF.toSync(underlying.xgroupCreate(streamOffset, group, args))
  
  def xgroupCreateconsumer(key: K, consumer: Consumer[K]): F[Boolean] =
    JF.toSync(underlying.xgroupCreateconsumer(key, consumer)).map(Boolean2boolean)
  
  def xgroupDelconsumer(key: K, consumer: Consumer[K]): F[Long] =
    JF.toSync(underlying.xgroupDelconsumer(key, consumer)).map(Long2long)
  
  def xgroupDestroy(key: K, group: K): F[Boolean] =
    JF.toSync(underlying.xgroupDestroy(key, group)).map(Boolean2boolean)
  
  def xgroupSetid(streamOffset: StreamOffset[K], group: K): F[String] =
    JF.toSync(underlying.xgroupSetid(streamOffset, group))
  
  def xinfoStream(key: K): F[List[RedisData[V]]] =
    JF.toSync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.STREAM).addKey(key))).map(_.asList)
  
  def xinfoGroups(key: K): F[List[RedisData[V]]] =
    JF.toSync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.GROUPS).addKey(key))).map(_.asList)
  
  def xinfoConsumers(key: K, group: K): F[List[RedisData[V]]] =
    JF.toSync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.CONSUMERS).addKey(key).addKey(group))).map(_.asList)
  
  def xlen(key: K): F[Long] =
    JF.toSync(underlying.xlen(key)).map(Long2long)
  
  def xpending(key: K, group: K): F[PendingMessages] =
    JF.toSync(underlying.xpending(key, group)).map(PendingMessages.from)
  
  def xpending(key: K, group: K, range: RedisRange[String], limit: Limit): F[Seq[PendingMessage]] =
    JF.toSync(underlying.xpending(key, group, range.toJava, limit)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xpending(key: K, consumer: Consumer[K], range: RedisRange[String], limit: Limit): F[Seq[PendingMessage]] =
    JF.toSync(underlying.xpending(key, consumer, range.toJava, limit)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xpending(key: K, args: XPendingArgs[K]): F[Seq[PendingMessage]] =
    JF.toSync(underlying.xpending(key, args)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xrange(key: K, range: RedisRange[String]): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xrange(key, range.toJava)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrange(key: K, range: RedisRange[String], limit: Limit): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xrange(key, range.toJava, limit)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xread(streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xread(streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xread(args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xread(args, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xreadgroup(consumer: Consumer[K], streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xreadgroup(consumer, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xreadgroup(consumer: Consumer[K], args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xreadgroup(consumer, args, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrevrange(key: K, range: RedisRange[String]): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xrevrange(key, range.toJava)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrevrange(key: K, range: RedisRange[String], limit: Limit): F[Seq[StreamMessage[K, V]]] =
    JF.toSync(underlying.xrevrange(key, range.toJava, limit)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xtrim(key: K, count: Long): F[Long] =
    JF.toSync(underlying.xtrim(key, count)).map(Long2long)
  
  def xtrim(key: K, approximateTrimming: Boolean, count: Long): F[Long] =
    JF.toSync(underlying.xtrim(key, approximateTrimming, count)).map(Long2long)
  
  def xtrim(key: K, args: XTrimArgs): F[Long] =
    JF.toSync(underlying.xtrim(key, args)).map(Long2long)
  
}