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


trait HashCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisHashAsyncCommands[K, V]
  
  def hdel(key: K, fields: K*): F[F[Long]] =
    JF.toAsync(underlying.hdel(key, fields: _*)).map(_.map(Long2long))
  
  def hexists(key: K, field: K): F[F[Boolean]] =
    JF.toAsync(underlying.hexists(key, field)).map(_.map(Boolean2boolean))
  
  def hget(key: K, field: K): F[F[Option[V]]] =
    JF.toAsync(underlying.hget(key, field)).map(_.map(Option(_)))
  
  def hincrby(key: K, field: K, amount: Long): F[F[Long]] =
    JF.toAsync(underlying.hincrby(key, field, amount)).map(_.map(Long2long))
  
  def hincrbyfloat(key: K, field: K, amount: Double): F[F[Double]] =
    JF.toAsync(underlying.hincrbyfloat(key, field, amount)).map(_.map(Double2double))
  
  def hgetall(key: K): F[F[Map[K, V]]] =
    JF.toAsync(underlying.hgetall(key)).map(_.map(_.asScala.toMap))
  
  def hkeys(key: K): F[F[Seq[K]]] =
    JF.toAsync(underlying.hkeys(key)).map(_.map(_.asScala.toSeq))
  
  def hlen(key: K): F[F[Long]] =
    JF.toAsync(underlying.hlen(key)).map(_.map(Long2long))
  
  def hmget(key: K, fields: K*): F[F[Seq[(K, Option[V])]]] =
    JF.toAsync(underlying.hmget(key, fields: _*)).map(_.map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv))))
  
  def hmset(key: K, map: Map[K, V]): F[F[String]] =
    JF.toAsync(underlying.hmset(key, map.asJava))
  
  def hrandfield(key: K): F[F[Option[K]]] =
    JF.toAsync(underlying.hrandfield(key)).map(_.map(Option(_)))
  
  def hrandfield(key: K, count: Long): F[F[Seq[K]]] =
    JF.toAsync(underlying.hrandfield(key, count)).map(_.map(_.asScala.toSeq))
  
  def hrandfieldWithvalues(key: K): F[F[(K, Option[V])]] =
    JF.toAsync(underlying.hrandfieldWithvalues(key)).map(_.map(kv => LettuceValueConverter.fromKeyValue(kv)))
  
  def hrandfieldWithvalues(key: K, count: Long): F[F[Seq[(K, Option[V])]]] =
    JF.toAsync(underlying.hrandfieldWithvalues(key, count)).map(_.map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv))))
  
  def hscan(key: K): F[F[DataScanCursor[(K, V)]]] =
    JF.toAsync(underlying.hscan(key)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def hscan(key: K, scanArgs: ScanArgs): F[F[DataScanCursor[(K, V)]]] =
    JF.toAsync(underlying.hscan(key, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def hscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[F[DataScanCursor[(K, V)]]] =
    JF.toAsync(underlying.hscan(key, scanCursor, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def hscan(key: K, scanCursor: ScanCursor): F[F[DataScanCursor[(K, V)]]] =
    JF.toAsync(underlying.hscan(key, scanCursor)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def hset(key: K, field: K, value: V): F[F[Boolean]] =
    JF.toAsync(underlying.hset(key, field, value)).map(_.map(Boolean2boolean))
  
  def hset(key: K, map: Map[K, V]): F[F[Long]] =
    JF.toAsync(underlying.hset(key, map.asJava)).map(_.map(Long2long))
  
  def hsetnx(key: K, field: K, value: V): F[F[Boolean]] =
    JF.toAsync(underlying.hsetnx(key, field, value)).map(_.map(Boolean2boolean))
  
  def hstrlen(key: K, field: K): F[F[Long]] =
    JF.toAsync(underlying.hstrlen(key, field)).map(_.map(Long2long))
  
  def hvals(key: K): F[F[Seq[V]]] =
    JF.toAsync(underlying.hvals(key)).map(_.map(_.asScala.toSeq))
  
}
