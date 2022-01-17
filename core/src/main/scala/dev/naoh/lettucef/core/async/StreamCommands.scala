// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.StreamCommandsF
import dev.naoh.lettucef.api.Commands
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


trait StreamCommands[F[_], K, V] extends CommandsDeps[F, K, V] with StreamCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisStreamAsyncCommands[K, V] with BaseRedisAsyncCommands[K, V]
  
  def xack(key: K, group: K, messageIds: String*): F[F[Long]] =
    JF.toAsync(underlying.xack(key, group, messageIds: _*)).map(_.map(Long2long))
  
  def xadd(key: K, body: Map[K, V]): F[F[String]] =
    JF.toAsync(underlying.xadd(key, body.asJava))
  
  def xadd(key: K, args: XAddArgs, body: Map[K, V]): F[F[String]] =
    JF.toAsync(underlying.xadd(key, args, body.asJava))
  
  def xautoclaim(key: K, args: XAutoClaimArgs[K]): F[F[ClaimedMessages[K, V]]] =
    JF.toAsync(underlying.xautoclaim(key, args)).map(_.map(ClaimedMessages.from))
  
  def xclaim(key: K, consumer: Consumer[K], minIdleTime: Long, messageIds: String*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xclaim(key, consumer, minIdleTime, messageIds: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xclaim(key: K, consumer: Consumer[K], args: XClaimArgs, messageIds: String*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xclaim(key, consumer, args, messageIds: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xdel(key: K, messageIds: String*): F[F[Long]] =
    JF.toAsync(underlying.xdel(key, messageIds: _*)).map(_.map(Long2long))
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K): F[F[String]] =
    JF.toAsync(underlying.xgroupCreate(streamOffset, group))
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K, args: XGroupCreateArgs): F[F[String]] =
    JF.toAsync(underlying.xgroupCreate(streamOffset, group, args))
  
  def xgroupCreateconsumer(key: K, consumer: Consumer[K]): F[F[Boolean]] =
    JF.toAsync(underlying.xgroupCreateconsumer(key, consumer)).map(_.map(Boolean2boolean))
  
  def xgroupDelconsumer(key: K, consumer: Consumer[K]): F[F[Long]] =
    JF.toAsync(underlying.xgroupDelconsumer(key, consumer)).map(_.map(Long2long))
  
  def xgroupDestroy(key: K, group: K): F[F[Boolean]] =
    JF.toAsync(underlying.xgroupDestroy(key, group)).map(_.map(Boolean2boolean))
  
  def xgroupSetid(streamOffset: StreamOffset[K], group: K): F[F[String]] =
    JF.toAsync(underlying.xgroupSetid(streamOffset, group))
  
  def xinfoStream(key: K): F[F[List[RedisData[V]]]] =
    JF.toAsync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.STREAM).addKey(key))).map(_.map(_.asList))
  
  def xinfoGroups(key: K): F[F[List[RedisData[V]]]] =
    JF.toAsync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.GROUPS).addKey(key))).map(_.map(_.asList))
  
  def xinfoConsumers(key: K, group: K): F[F[List[RedisData[V]]]] =
    JF.toAsync(underlying.dispatch(CommandType.XINFO, dispatchHelper.createRedisDataOutput(), dispatchHelper.createArgs().add(CommandKeyword.CONSUMERS).addKey(key).addKey(group))).map(_.map(_.asList))
  
  def xlen(key: K): F[F[Long]] =
    JF.toAsync(underlying.xlen(key)).map(_.map(Long2long))
  
  def xpending(key: K, group: K): F[F[PendingMessages]] =
    JF.toAsync(underlying.xpending(key, group)).map(_.map(PendingMessages.from))
  
  def xpending(key: K, group: K, range: RedisRange[String], limit: Limit): F[F[Seq[PendingMessage]]] =
    JF.toAsync(underlying.xpending(key, group, range.toJava, limit)).map(_.map(_.asScala.toSeq.map(PendingMessage.from)))
  
  def xpending(key: K, consumer: Consumer[K], range: RedisRange[String], limit: Limit): F[F[Seq[PendingMessage]]] =
    JF.toAsync(underlying.xpending(key, consumer, range.toJava, limit)).map(_.map(_.asScala.toSeq.map(PendingMessage.from)))
  
  def xpending(key: K, args: XPendingArgs[K]): F[F[Seq[PendingMessage]]] =
    JF.toAsync(underlying.xpending(key, args)).map(_.map(_.asScala.toSeq.map(PendingMessage.from)))
  
  def xrange(key: K, range: RedisRange[String]): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xrange(key, range.toJava)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xrange(key: K, range: RedisRange[String], limit: Limit): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xrange(key, range.toJava, limit)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xread(streams: StreamOffset[K]*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xread(streams: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xread(args: XReadArgs, streams: StreamOffset[K]*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xread(args, streams: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xreadgroup(consumer: Consumer[K], streams: StreamOffset[K]*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xreadgroup(consumer, streams: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xreadgroup(consumer: Consumer[K], args: XReadArgs, streams: StreamOffset[K]*): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xreadgroup(consumer, args, streams: _*)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xrevrange(key: K, range: RedisRange[String]): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xrevrange(key, range.toJava)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xrevrange(key: K, range: RedisRange[String], limit: Limit): F[F[Seq[StreamMessage[K, V]]]] =
    JF.toAsync(underlying.xrevrange(key, range.toJava, limit)).map(_.map(_.asScala.toSeq.map(StreamMessage.from)))
  
  def xtrim(key: K, count: Long): F[F[Long]] =
    JF.toAsync(underlying.xtrim(key, count)).map(_.map(Long2long))
  
  def xtrim(key: K, approximateTrimming: Boolean, count: Long): F[F[Long]] =
    JF.toAsync(underlying.xtrim(key, approximateTrimming, count)).map(_.map(Long2long))
  
  def xtrim(key: K, args: XTrimArgs): F[F[Long]] =
    JF.toAsync(underlying.xtrim(key, args)).map(_.map(Long2long))
  
}