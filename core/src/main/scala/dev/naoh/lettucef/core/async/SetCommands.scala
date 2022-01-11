// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait SetCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisSetAsyncCommands[K, V]
  
  def sadd(key: K, members: V*): F[F[Long]] =
    JF.toAsync(underlying.sadd(key, members: _*)).map(_.map(Long2long))
  
  def scard(key: K): F[F[Long]] =
    JF.toAsync(underlying.scard(key)).map(_.map(Long2long))
  
  def sdiff(keys: K*): F[F[Set[V]]] =
    JF.toAsync(underlying.sdiff(keys: _*)).map(_.map(_.asScala.toSet))
  
  def sdiffstore(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.sdiffstore(destination, keys: _*)).map(_.map(Long2long))
  
  def sinter(keys: K*): F[F[Set[V]]] =
    JF.toAsync(underlying.sinter(keys: _*)).map(_.map(_.asScala.toSet))
  
  def sinterstore(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.sinterstore(destination, keys: _*)).map(_.map(Long2long))
  
  def sismember(key: K, member: V): F[F[Boolean]] =
    JF.toAsync(underlying.sismember(key, member)).map(_.map(Boolean2boolean))
  
  def smembers(key: K): F[F[Set[V]]] =
    JF.toAsync(underlying.smembers(key)).map(_.map(_.asScala.toSet))
  
  def smismember(key: K, members: V*): F[F[Seq[Boolean]]] =
    JF.toAsync(underlying.smismember(key, members: _*)).map(_.map(_.asScala.toSeq.map(Boolean2boolean)))
  
  def smove(source: K, destination: K, member: V): F[F[Boolean]] =
    JF.toAsync(underlying.smove(source, destination, member)).map(_.map(Boolean2boolean))
  
  def spop(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.spop(key)).map(_.map(Option(_)))
  
  def spop(key: K, count: Long): F[F[Set[V]]] =
    JF.toAsync(underlying.spop(key, count)).map(_.map(_.asScala.toSet))
  
  def srandmember(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.srandmember(key)).map(_.map(Option(_)))
  
  def srandmember(key: K, count: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.srandmember(key, count)).map(_.map(_.asScala.toSeq))
  
  def srem(key: K, members: V*): F[F[Long]] =
    JF.toAsync(underlying.srem(key, members: _*)).map(_.map(Long2long))
  
  def sunion(keys: K*): F[F[Set[V]]] =
    JF.toAsync(underlying.sunion(keys: _*)).map(_.map(_.asScala.toSet))
  
  def sunionstore(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.sunionstore(destination, keys: _*)).map(_.map(Long2long))
  
  def sscan(key: K): F[F[DataScanCursor[V]]] =
    JF.toAsync(underlying.sscan(key)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def sscan(key: K, scanArgs: ScanArgs): F[F[DataScanCursor[V]]] =
    JF.toAsync(underlying.sscan(key, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def sscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[F[DataScanCursor[V]]] =
    JF.toAsync(underlying.sscan(key, scanCursor, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def sscan(key: K, scanCursor: ScanCursor): F[F[DataScanCursor[V]]] =
    JF.toAsync(underlying.sscan(key, scanCursor)).map(_.map(cur => DataScanCursor.from(cur)))
  
}
