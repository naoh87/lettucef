// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import io.lettuce.core.LMoveArgs
import io.lettuce.core.LPosArgs


trait ListCommandsF[F[_], K, V] {

  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Long): F[Option[V]]
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Double): F[Option[V]]
  
  def blpop(timeout: Long, keys: K*): F[Option[(K, V)]]
  
  def blpop(timeout: Double, keys: K*): F[Option[(K, V)]]
  
  def brpop(timeout: Long, keys: K*): F[Option[(K, V)]]
  
  def brpop(timeout: Double, keys: K*): F[Option[(K, V)]]
  
  def brpoplpush(timeout: Long, source: K, destination: K): F[Option[V]]
  
  def brpoplpush(timeout: Double, source: K, destination: K): F[Option[V]]
  
  def lindex(key: K, index: Long): F[Option[V]]
  
  def linsert(key: K, before: Boolean, pivot: V, value: V): F[Long]
  
  def llen(key: K): F[Long]
  
  def lmove(source: K, destination: K, args: LMoveArgs): F[Option[V]]
  
  def lpop(key: K): F[Option[V]]
  
  def lpop(key: K, count: Long): F[Seq[V]]
  
  def lpos(key: K, value: V): F[Option[Long]]
  
  def lpos(key: K, value: V, args: LPosArgs): F[Option[Long]]
  
  def lpos(key: K, value: V, count: Int): F[Seq[Long]]
  
  def lpos(key: K, value: V, count: Int, args: LPosArgs): F[Seq[Long]]
  
  def lpush(key: K, values: V*): F[Long]
  
  def lpushx(key: K, values: V*): F[Long]
  
  def lrange(key: K, start: Long, stop: Long): F[Seq[V]]
  
  def lrem(key: K, count: Long, value: V): F[Long]
  
  def lset(key: K, index: Long, value: V): F[String]
  
  def ltrim(key: K, start: Long, stop: Long): F[String]
  
  def rpop(key: K): F[Option[V]]
  
  def rpop(key: K, count: Long): F[Seq[V]]
  
  def rpoplpush(source: K, destination: K): F[Option[V]]
  
  def rpush(key: K, values: V*): F[Long]
  
  def rpushx(key: K, values: V*): F[Long]
  
}
