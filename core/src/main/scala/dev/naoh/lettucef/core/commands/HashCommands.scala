// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.commands

import cats.syntax.functor._
import dev.naoh.lettucef.core.models._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait HashCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisHashAsyncCommands[K, V]
  
  def hdel(key: K, fields: K*): F[Long] =
    JF.toAsync(underlying.hdel(key, fields: _*)).map(Long2long)
  
  def hexists(key: K, field: K): F[Boolean] =
    JF.toAsync(underlying.hexists(key, field)).map(Boolean2boolean)
  
  def hget(key: K, field: K): F[Option[V]] =
    JF.toAsync(underlying.hget(key, field)).map(Option(_))
  
  def hincrby(key: K, field: K, amount: Long): F[Long] =
    JF.toAsync(underlying.hincrby(key, field, amount)).map(Long2long)
  
  def hincrbyfloat(key: K, field: K, amount: Double): F[Double] =
    JF.toAsync(underlying.hincrbyfloat(key, field, amount)).map(Double2double)
  
  def hgetall(key: K): F[Map[K, V]] =
    JF.toAsync(underlying.hgetall(key)).map(_.asScala.toMap)
  
  def hkeys(key: K): F[Seq[K]] =
    JF.toAsync(underlying.hkeys(key)).map(_.asScala.toSeq)
  
  def hlen(key: K): F[Long] =
    JF.toAsync(underlying.hlen(key)).map(Long2long)
  
  def hmget(key: K, fields: K*): F[Seq[(K, Option[V])]] =
    JF.toAsync(underlying.hmget(key, fields: _*)).map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv)))
  
  def hmset(key: K, map: Map[K, V]): F[String] =
    JF.toAsync(underlying.hmset(key, map.asJava))
  
  def hrandfield(key: K): F[Option[K]] =
    JF.toAsync(underlying.hrandfield(key)).map(Option(_))
  
  def hrandfield(key: K, count: Long): F[Seq[K]] =
    JF.toAsync(underlying.hrandfield(key, count)).map(_.asScala.toSeq)
  
  def hrandfieldWithvalues(key: K): F[(K, Option[V])] =
    JF.toAsync(underlying.hrandfieldWithvalues(key)).map(kv => LettuceValueConverter.fromKeyValue(kv))
  
  def hrandfieldWithvalues(key: K, count: Long): F[Seq[(K, Option[V])]] =
    JF.toAsync(underlying.hrandfieldWithvalues(key, count)).map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv)))
  
  def hscan(key: K): F[DataScanCursor[(K, V)]] =
    JF.toAsync(underlying.hscan(key)).map(cur => DataScanCursor.from(cur))
  
  def hscan(key: K, scanArgs: ScanArgs): F[DataScanCursor[(K, V)]] =
    JF.toAsync(underlying.hscan(key, scanArgs)).map(cur => DataScanCursor.from(cur))
  
  def hscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[(K, V)]] =
    JF.toAsync(underlying.hscan(key, scanCursor, scanArgs)).map(cur => DataScanCursor.from(cur))
  
  def hscan(key: K, scanCursor: ScanCursor): F[DataScanCursor[(K, V)]] =
    JF.toAsync(underlying.hscan(key, scanCursor)).map(cur => DataScanCursor.from(cur))
  
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
  
}

