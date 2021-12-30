// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import cats.syntax.functor._
import io.lettuce.core.Consumer
import io.lettuce.core.Limit
import io.lettuce.core.Range
import io.lettuce.core.XAddArgs
import io.lettuce.core.XAutoClaimArgs
import io.lettuce.core.XClaimArgs
import io.lettuce.core.XGroupCreateArgs
import io.lettuce.core.XPendingArgs
import io.lettuce.core.XReadArgs
import io.lettuce.core.XReadArgs.StreamOffset
import io.lettuce.core.XTrimArgs
import io.lettuce.core.api.async._
import io.lettucef.core.models._
import io.lettucef.core.models.stream._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait StreamCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisStreamAsyncCommands[K, V]
  
  def xack(key: K, group: K, messageIds: String*): F[Long] =
    JF.toAsync(underlying.xack(key, group, messageIds: _*)).map(Long2long)
  
  def xadd(key: K, body: Map[K, V]): F[String] =
    JF.toAsync(underlying.xadd(key, body.asJava))
  
  def xadd(key: K, args: XAddArgs, body: Map[K, V]): F[String] =
    JF.toAsync(underlying.xadd(key, args, body.asJava))
  
  def xautoclaim(key: K, args: XAutoClaimArgs[K]): F[ClaimedMessages[K, V]] =
    JF.toAsync(underlying.xautoclaim(key, args)).map(ClaimedMessages.from)
  
  def xclaim(key: K, consumer: Consumer[K], minIdleTime: Long, messageIds: String*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xclaim(key, consumer, minIdleTime, messageIds: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xclaim(key: K, consumer: Consumer[K], args: XClaimArgs, messageIds: String*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xclaim(key, consumer, args, messageIds: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xdel(key: K, messageIds: String*): F[Long] =
    JF.toAsync(underlying.xdel(key, messageIds: _*)).map(Long2long)
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K): F[String] =
    JF.toAsync(underlying.xgroupCreate(streamOffset, group))
  
  def xgroupCreate(streamOffset: StreamOffset[K], group: K, args: XGroupCreateArgs): F[String] =
    JF.toAsync(underlying.xgroupCreate(streamOffset, group, args))
  
  def xgroupCreateconsumer(key: K, consumer: Consumer[K]): F[Boolean] =
    JF.toAsync(underlying.xgroupCreateconsumer(key, consumer)).map(Boolean2boolean)
  
  def xgroupDelconsumer(key: K, consumer: Consumer[K]): F[Long] =
    JF.toAsync(underlying.xgroupDelconsumer(key, consumer)).map(Long2long)
  
  def xgroupDestroy(key: K, group: K): F[Boolean] =
    JF.toAsync(underlying.xgroupDestroy(key, group)).map(Boolean2boolean)
  
  def xgroupSetid(streamOffset: StreamOffset[K], group: K): F[String] =
    JF.toAsync(underlying.xgroupSetid(streamOffset, group))
  
  def xinfoStream(key: K): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.xinfoStream(key)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def xinfoGroups(key: K): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.xinfoGroups(key)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def xinfoConsumers(key: K, group: K): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.xinfoConsumers(key, group)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def xlen(key: K): F[Long] =
    JF.toAsync(underlying.xlen(key)).map(Long2long)
  
  def xpending(key: K, group: K): F[PendingMessages] =
    JF.toAsync(underlying.xpending(key, group)).map(PendingMessages.from)
  
  def xpending(key: K, group: K, range: Range[String], limit: Limit): F[Seq[PendingMessage]] =
    JF.toAsync(underlying.xpending(key, group, range, limit)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xpending(key: K, consumer: Consumer[K], range: Range[String], limit: Limit): F[Seq[PendingMessage]] =
    JF.toAsync(underlying.xpending(key, consumer, range, limit)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xpending(key: K, args: XPendingArgs[K]): F[Seq[PendingMessage]] =
    JF.toAsync(underlying.xpending(key, args)).map(_.asScala.toSeq.map(PendingMessage.from))
  
  def xrange(key: K, range: Range[String]): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xrange(key, range)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrange(key: K, range: Range[String], limit: Limit): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xrange(key, range, limit)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xread(streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xread(streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xread(args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xread(args, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xreadgroup(consumer: Consumer[K], streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xreadgroup(consumer, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xreadgroup(consumer: Consumer[K], args: XReadArgs, streams: StreamOffset[K]*): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xreadgroup(consumer, args, streams: _*)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrevrange(key: K, range: Range[String]): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xrevrange(key, range)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xrevrange(key: K, range: Range[String], limit: Limit): F[Seq[StreamMessage[K, V]]] =
    JF.toAsync(underlying.xrevrange(key, range, limit)).map(_.asScala.toSeq.map(StreamMessage.from))
  
  def xtrim(key: K, count: Long): F[Long] =
    JF.toAsync(underlying.xtrim(key, count)).map(Long2long)
  
  def xtrim(key: K, approximateTrimming: Boolean, count: Long): F[Long] =
    JF.toAsync(underlying.xtrim(key, approximateTrimming, count)).map(Long2long)
  
  def xtrim(key: K, args: XTrimArgs): F[Long] =
    JF.toAsync(underlying.xtrim(key, args)).map(Long2long)
  
}

