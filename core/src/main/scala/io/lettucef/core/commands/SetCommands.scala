// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import cats.syntax.functor._
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.ValueScanCursor
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait SetCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisSetAsyncCommands[K, V]
  
  def sadd(key: K, members: V*): F[Long] =
    JF.toAsync(underlying.sadd(key, members: _*)).map(Long2long)
  
  def scard(key: K): F[Long] =
    JF.toAsync(underlying.scard(key)).map(Long2long)
  
  def sdiff(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sdiff(keys: _*)).map(_.asScala.toSet)
  
  def sdiffstore(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.sdiffstore(destination, keys: _*)).map(Long2long)
  
  def sinter(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sinter(keys: _*)).map(_.asScala.toSet)
  
  def sinterstore(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.sinterstore(destination, keys: _*)).map(Long2long)
  
  def sismember(key: K, member: V): F[Boolean] =
    JF.toAsync(underlying.sismember(key, member)).map(Boolean2boolean)
  
  def smembers(key: K): F[Set[V]] =
    JF.toAsync(underlying.smembers(key)).map(_.asScala.toSet)
  
  def smismember(key: K, members: V*): F[Seq[Boolean]] =
    JF.toAsync(underlying.smismember(key, members: _*)).map(_.asScala.toSeq.map(Boolean2boolean))
  
  def smove(source: K, destination: K, member: V): F[Boolean] =
    JF.toAsync(underlying.smove(source, destination, member)).map(Boolean2boolean)
  
  def spop(key: K): F[Option[V]] =
    JF.toAsync(underlying.spop(key)).map(Option(_))
  
  def spop(key: K, count: Long): F[Option[Set[V]]] =
    JF.toAsync(underlying.spop(key, count)).map(Option(_).map(_.asScala.toSet))
  
  def srandmember(key: K): F[Option[V]] =
    JF.toAsync(underlying.srandmember(key)).map(Option(_))
  
  def srandmember(key: K, count: Long): F[Option[Seq[V]]] =
    JF.toAsync(underlying.srandmember(key, count)).map(Option(_).map(_.asScala.toSeq))
  
  def srem(key: K, members: V*): F[Long] =
    JF.toAsync(underlying.srem(key, members: _*)).map(Long2long)
  
  def sunion(keys: K*): F[Set[V]] =
    JF.toAsync(underlying.sunion(keys: _*)).map(_.asScala.toSet)
  
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
  
}

