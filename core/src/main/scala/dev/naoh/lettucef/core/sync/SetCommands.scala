// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.SetCommandsF
import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait SetCommands[F[_], K, V] extends CommandsDeps[F, K, V] with SetCommandsF[F, K, V] {

  protected val underlying: RedisSetAsyncCommands[K, V]
  
  def sadd(key: K, members: V*): F[Long] =
    JF.toSync(underlying.sadd(key, members: _*)).map(Long2long)
  
  def scard(key: K): F[Long] =
    JF.toSync(underlying.scard(key)).map(Long2long)
  
  def sdiff(keys: K*): F[Set[V]] =
    JF.toSync(underlying.sdiff(keys: _*)).map(_.asScala.toSet)
  
  def sdiffstore(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.sdiffstore(destination, keys: _*)).map(Long2long)
  
  def sinter(keys: K*): F[Set[V]] =
    JF.toSync(underlying.sinter(keys: _*)).map(_.asScala.toSet)
  
  def sinterstore(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.sinterstore(destination, keys: _*)).map(Long2long)
  
  def sismember(key: K, member: V): F[Boolean] =
    JF.toSync(underlying.sismember(key, member)).map(Boolean2boolean)
  
  def smembers(key: K): F[Set[V]] =
    JF.toSync(underlying.smembers(key)).map(_.asScala.toSet)
  
  def smismember(key: K, members: V*): F[Seq[Boolean]] =
    JF.toSync(underlying.smismember(key, members: _*)).map(_.asScala.toSeq.map(Boolean2boolean))
  
  def smove(source: K, destination: K, member: V): F[Boolean] =
    JF.toSync(underlying.smove(source, destination, member)).map(Boolean2boolean)
  
  def spop(key: K): F[Option[V]] =
    JF.toSync(underlying.spop(key)).map(Option(_))
  
  def spop(key: K, count: Long): F[Set[V]] =
    JF.toSync(underlying.spop(key, count)).map(_.asScala.toSet)
  
  def srandmember(key: K): F[Option[V]] =
    JF.toSync(underlying.srandmember(key)).map(Option(_))
  
  def srandmember(key: K, count: Long): F[Seq[V]] =
    JF.toSync(underlying.srandmember(key, count)).map(_.asScala.toSeq)
  
  def srem(key: K, members: V*): F[Long] =
    JF.toSync(underlying.srem(key, members: _*)).map(Long2long)
  
  def sunion(keys: K*): F[Set[V]] =
    JF.toSync(underlying.sunion(keys: _*)).map(_.asScala.toSet)
  
  def sunionstore(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.sunionstore(destination, keys: _*)).map(Long2long)
  
  def sscan(key: K): F[DataScanCursor[V]] =
    JF.toSync(underlying.sscan(key)).map(cur => DataScanCursor.from(cur))
  
  def sscan(key: K, scanArgs: ScanArgs): F[DataScanCursor[V]] =
    JF.toSync(underlying.sscan(key, scanArgs)).map(cur => DataScanCursor.from(cur))
  
  def sscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[V]] =
    JF.toSync(underlying.sscan(key, scanCursor, scanArgs)).map(cur => DataScanCursor.from(cur))
  
  def sscan(key: K, scanCursor: ScanCursor): F[DataScanCursor[V]] =
    JF.toSync(underlying.sscan(key, scanCursor)).map(cur => DataScanCursor.from(cur))
  
}