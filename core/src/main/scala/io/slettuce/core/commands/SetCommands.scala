// Code generated. DO NOT EDIT
package io.slettuce.core.commands

import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.StreamScanCursor
import io.lettuce.core.ValueScanCursor
import io.lettuce.core.output.ValueStreamingChannel
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.slettuce.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait SetCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisSetAsyncCommands[K, V]
  
  def sadd(key: K, members: V*): F[Long] =
    JF.toAsync(underlying.sadd(key, members: _*)).map(Long2long)
  
  def scard(key: K): F[Long] =
    JF.toAsync(underlying.scard(key)).map(Long2long)
  
  def sdiff(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sdiff(keys: _*)).map(_.asScala.toSet)
  
  def sdiff(channel: ValueStreamingChannel[V], keys: K*): F[Long] =
    JF.toAsync(underlying.sdiff(channel, keys: _*)).map(Long2long)
  
  def sdiffstore(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.sdiffstore(destination, keys: _*)).map(Long2long)
  
  def sinter(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sinter(keys: _*)).map(_.asScala.toSet)
  
  def sinter(channel: ValueStreamingChannel[V], keys: K*): F[Long] =
    JF.toAsync(underlying.sinter(channel, keys: _*)).map(Long2long)
  
  def sinterstore(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.sinterstore(destination, keys: _*)).map(Long2long)
  
  def sismember(key: K, member: V): F[Boolean] =
    JF.toAsync(underlying.sismember(key, member)).map(Boolean2boolean)
  
  def smembers(key: K): F[Set[V]] =
    JF.toAsync(underlying.smembers(key)).map(_.asScala.toSet)
  
  def smembers(channel: ValueStreamingChannel[V], key: K): F[Long] =
    JF.toAsync(underlying.smembers(channel, key)).map(Long2long)
  
  def smismember(key: K, members: V*): F[Seq[Boolean]] =
    JF.toAsync(underlying.smismember(key, members: _*)).map(_.asScala.toSeq.map(Boolean2boolean))
  
  def smove(source: K, destination: K, member: V): F[Boolean] =
    JF.toAsync(underlying.smove(source, destination, member)).map(Boolean2boolean)
  
  def spop(key: K): F[V] =
    JF.toAsync(underlying.spop(key))
  
  def spop(key: K, count: Long): F[Set[V]] =
    JF.toAsync(underlying.spop(key, count)).map(_.asScala.toSet)
  
  def srandmember(key: K): F[V] =
    JF.toAsync(underlying.srandmember(key))
  
  def srandmember(key: K, count: Long): F[Seq[V]] =
    JF.toAsync(underlying.srandmember(key, count)).map(_.asScala.toSeq)
  
  def srandmember(channel: ValueStreamingChannel[V], key: K, count: Long): F[Long] =
    JF.toAsync(underlying.srandmember(channel, key, count)).map(Long2long)
  
  def srem(key: K, members: V*): F[Long] =
    JF.toAsync(underlying.srem(key, members: _*)).map(Long2long)
  
  def sunion(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sunion(keys: _*)).map(_.asScala.toSet)
  
  def sunion(channel: ValueStreamingChannel[V], keys: K*): F[Long] =
    JF.toAsync(underlying.sunion(channel, keys: _*)).map(Long2long)
  
  def sunionstore(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.sunionstore(destination, keys: _*)).map(Long2long)
  
  def sscan(key: K): F[ValueScanCursor[V]] =
    JF.toAsync(underlying.sscan(key))
  
  def sscan(key: K, scanArgs: ScanArgs): F[ValueScanCursor[V]] =
    JF.toAsync(underlying.sscan(key, scanArgs))
  
  def sscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[ValueScanCursor[V]] =
    JF.toAsync(underlying.sscan(key, scanCursor, scanArgs))
  
  def sscan(key: K, scanCursor: ScanCursor): F[ValueScanCursor[V]] =
    JF.toAsync(underlying.sscan(key, scanCursor))
  
  def sscan(channel: ValueStreamingChannel[V], key: K): F[StreamScanCursor] =
    JF.toAsync(underlying.sscan(channel, key))
  
  def sscan(channel: ValueStreamingChannel[V], key: K, scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.sscan(channel, key, scanArgs))
  
  def sscan(channel: ValueStreamingChannel[V], key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.sscan(channel, key, scanCursor, scanArgs))
  
  def sscan(channel: ValueStreamingChannel[V], key: K, scanCursor: ScanCursor): F[StreamScanCursor] =
    JF.toAsync(underlying.sscan(channel, key, scanCursor))
  
}

