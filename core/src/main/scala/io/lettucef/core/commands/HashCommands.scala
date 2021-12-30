// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import io.lettuce.core.KeyValue
import io.lettuce.core.MapScanCursor
import io.lettuce.core.RedisFuture
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.StreamScanCursor
import io.lettuce.core.output.KeyStreamingChannel
import io.lettuce.core.output.KeyValueStreamingChannel
import io.lettuce.core.output.ValueStreamingChannel
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait HashCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisHashAsyncCommands[K, V]
  
  def hdel(key: K, fields: K*): F[Long] =
    JF.toAsync(underlying.hdel(key, fields: _*)).map(Long2long)
  
  def hexists(key: K, field: K): F[Boolean] =
    JF.toAsync(underlying.hexists(key, field)).map(Boolean2boolean)
  
  def hget(key: K, field: K): F[V] =
    JF.toAsync(underlying.hget(key, field))
  
  def hincrby(key: K, field: K, amount: Long): F[Long] =
    JF.toAsync(underlying.hincrby(key, field, amount)).map(Long2long)
  
  def hincrbyfloat(key: K, field: K, amount: Double): F[Double] =
    JF.toAsync(underlying.hincrbyfloat(key, field, amount)).map(Double2double)
  
  def hgetall(key: K): F[Map[K, V]] =
    JF.toAsync(underlying.hgetall(key)).map(_.asScala.toMap)
  
  def hgetall(channel: KeyValueStreamingChannel[K, V], key: K): F[Long] =
    JF.toAsync(underlying.hgetall(channel, key)).map(Long2long)
  
  def hkeys(key: K): F[Seq[K]] =
    JF.toAsync(underlying.hkeys(key)).map(_.asScala.toSeq)
  
  def hkeys(channel: KeyStreamingChannel[K], key: K): F[Long] =
    JF.toAsync(underlying.hkeys(channel, key)).map(Long2long)
  
  def hlen(key: K): F[Long] =
    JF.toAsync(underlying.hlen(key)).map(Long2long)
  
  def hmget(key: K, fields: K*): F[Seq[KeyValue[K, V]]] =
    JF.toAsync(underlying.hmget(key, fields: _*)).map(_.asScala.toSeq)
  
  def hmget(channel: KeyValueStreamingChannel[K, V], key: K, fields: K*): F[Long] =
    JF.toAsync(underlying.hmget(channel, key, fields: _*)).map(Long2long)
  
  def hmset(key: K, map: Map[K, V]): F[String] =
    JF.toAsync(underlying.hmset(key, map.asJava))
  
  def hrandfield(key: K): F[K] =
    JF.toAsync(underlying.hrandfield(key))
  
  def hrandfield(key: K, count: Long): F[Seq[K]] =
    JF.toAsync(underlying.hrandfield(key, count)).map(_.asScala.toSeq)
  
  def hrandfieldWithvalues(key: K): F[KeyValue[K, V]] =
    JF.toAsync(underlying.hrandfieldWithvalues(key))
  
  def hrandfieldWithvalues(key: K, count: Long): F[Seq[KeyValue[K, V]]] =
    JF.toAsync(underlying.hrandfieldWithvalues(key, count)).map(_.asScala.toSeq)
  
  def hscan(key: K): F[MapScanCursor[K, V]] =
    JF.toAsync(underlying.hscan(key))
  
  def hscan(key: K, scanArgs: ScanArgs): F[MapScanCursor[K, V]] =
    JF.toAsync(underlying.hscan(key, scanArgs))
  
  def hscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[MapScanCursor[K, V]] =
    JF.toAsync(underlying.hscan(key, scanCursor, scanArgs))
  
  def hscan(key: K, scanCursor: ScanCursor): F[MapScanCursor[K, V]] =
    JF.toAsync(underlying.hscan(key, scanCursor))
  
  def hscan(channel: KeyValueStreamingChannel[K, V], key: K): F[StreamScanCursor] =
    JF.toAsync(underlying.hscan(channel, key))
  
  def hscan(channel: KeyValueStreamingChannel[K, V], key: K, scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.hscan(channel, key, scanArgs))
  
  def hscan(channel: KeyValueStreamingChannel[K, V], key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.hscan(channel, key, scanCursor, scanArgs))
  
  def hscan(channel: KeyValueStreamingChannel[K, V], key: K, scanCursor: ScanCursor): F[StreamScanCursor] =
    JF.toAsync(underlying.hscan(channel, key, scanCursor))
  
  def hset(key: K, field: K, value: V): F[Boolean] =
    JF.toAsync(underlying.hset(key, field, value)).map(Boolean2boolean)
  
  def hset(key: K, map: Map[K, V]): F[Long] =
    JF.toAsync(underlying.hset(key, map.asJava)).map(Long2long)
  
  def hsetnx(key: K, field: K, value: V): F[Boolean] =
    JF.toAsync(underlying.hsetnx(key, field, value)).map(Boolean2boolean)
  
  def hstrlen(key: K, field: K): F[Long] =
    JF.toAsync(underlying.hstrlen(key, field)).map(Long2long)
  
  def hvals(key: K): F[Seq[V]] =
    JF.toAsync(underlying.hvals(key)).map(_.asScala.toSeq)
  
  def hvals(channel: ValueStreamingChannel[V], key: K): F[Long] =
    JF.toAsync(underlying.hvals(channel, key)).map(Long2long)
  
}

