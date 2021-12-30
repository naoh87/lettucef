// Code generated. DO NOT EDIT
package io.slettuce.core.commands

import io.lettuce.core.KeyValue
import io.lettuce.core.LMoveArgs
import io.lettuce.core.LPosArgs
import io.lettuce.core.RedisFuture
import io.lettuce.core.output.ValueStreamingChannel
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.slettuce.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait ListCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisListAsyncCommands[K, V]
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Long): F[V] =
    JF.toAsync(underlying.blmove(source, destination, args, timeout))
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Double): F[V] =
    JF.toAsync(underlying.blmove(source, destination, args, timeout))
  
  def blpop(timeout: Long, keys: K*): F[KeyValue[K, V]] =
    JF.toAsync(underlying.blpop(timeout, keys: _*))
  
  def blpop(timeout: Double, keys: K*): F[KeyValue[K, V]] =
    JF.toAsync(underlying.blpop(timeout, keys: _*))
  
  def brpop(timeout: Long, keys: K*): F[KeyValue[K, V]] =
    JF.toAsync(underlying.brpop(timeout, keys: _*))
  
  def brpop(timeout: Double, keys: K*): F[KeyValue[K, V]] =
    JF.toAsync(underlying.brpop(timeout, keys: _*))
  
  def brpoplpush(timeout: Long, source: K, destination: K): F[V] =
    JF.toAsync(underlying.brpoplpush(timeout, source, destination))
  
  def brpoplpush(timeout: Double, source: K, destination: K): F[V] =
    JF.toAsync(underlying.brpoplpush(timeout, source, destination))
  
  def lindex(key: K, index: Long): F[V] =
    JF.toAsync(underlying.lindex(key, index))
  
  def linsert(key: K, before: Boolean, pivot: V, value: V): F[Long] =
    JF.toAsync(underlying.linsert(key, before, pivot, value)).map(Long2long)
  
  def llen(key: K): F[Long] =
    JF.toAsync(underlying.llen(key)).map(Long2long)
  
  def lmove(source: K, destination: K, args: LMoveArgs): F[V] =
    JF.toAsync(underlying.lmove(source, destination, args))
  
  def lpop(key: K): F[V] =
    JF.toAsync(underlying.lpop(key))
  
  def lpop(key: K, count: Long): F[Seq[V]] =
    JF.toAsync(underlying.lpop(key, count)).map(_.asScala.toSeq)
  
  def lpos(key: K, value: V): F[Long] =
    JF.toAsync(underlying.lpos(key, value)).map(Long2long)
  
  def lpos(key: K, value: V, args: LPosArgs): F[Long] =
    JF.toAsync(underlying.lpos(key, value, args)).map(Long2long)
  
  def lpos(key: K, value: V, count: Int): F[Seq[Long]] =
    JF.toAsync(underlying.lpos(key, value, count)).map(_.asScala.toSeq.map(Long2long))
  
  def lpos(key: K, value: V, count: Int, args: LPosArgs): F[Seq[Long]] =
    JF.toAsync(underlying.lpos(key, value, count, args)).map(_.asScala.toSeq.map(Long2long))
  
  def lpush(key: K, values: V*): F[Long] =
    JF.toAsync(underlying.lpush(key, values: _*)).map(Long2long)
  
  def lpushx(key: K, values: V*): F[Long] =
    JF.toAsync(underlying.lpushx(key, values: _*)).map(Long2long)
  
  def lrange(key: K, start: Long, stop: Long): F[Seq[V]] =
    JF.toAsync(underlying.lrange(key, start, stop)).map(_.asScala.toSeq)
  
  def lrange(channel: ValueStreamingChannel[V], key: K, start: Long, stop: Long): F[Long] =
    JF.toAsync(underlying.lrange(channel, key, start, stop)).map(Long2long)
  
  def lrem(key: K, count: Long, value: V): F[Long] =
    JF.toAsync(underlying.lrem(key, count, value)).map(Long2long)
  
  def lset(key: K, index: Long, value: V): F[String] =
    JF.toAsync(underlying.lset(key, index, value))
  
  def ltrim(key: K, start: Long, stop: Long): F[String] =
    JF.toAsync(underlying.ltrim(key, start, stop))
  
  def rpop(key: K): F[V] =
    JF.toAsync(underlying.rpop(key))
  
  def rpop(key: K, count: Long): F[Seq[V]] =
    JF.toAsync(underlying.rpop(key, count)).map(_.asScala.toSeq)
  
  def rpoplpush(source: K, destination: K): F[V] =
    JF.toAsync(underlying.rpoplpush(source, destination))
  
  def rpush(key: K, values: V*): F[Long] =
    JF.toAsync(underlying.rpush(key, values: _*)).map(Long2long)
  
  def rpushx(key: K, values: V*): F[Long] =
    JF.toAsync(underlying.rpushx(key, values: _*)).map(Long2long)
  
}

