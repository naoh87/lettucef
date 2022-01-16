// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait HashCommandsF[F[_], K, V] {

  def hdel(key: K, fields: K*): F[Long]
  
  def hexists(key: K, field: K): F[Boolean]
  
  def hget(key: K, field: K): F[Option[V]]
  
  def hincrby(key: K, field: K, amount: Long): F[Long]
  
  def hincrbyfloat(key: K, field: K, amount: Double): F[Double]
  
  def hgetall(key: K): F[Map[K, V]]
  
  def hkeys(key: K): F[Seq[K]]
  
  def hlen(key: K): F[Long]
  
  def hmget(key: K, fields: K*): F[Seq[(K, Option[V])]]
  
  def hmset(key: K, map: Map[K, V]): F[String]
  
  def hrandfield(key: K): F[Option[K]]
  
  def hrandfield(key: K, count: Long): F[Seq[K]]
  
  def hrandfieldWithvalues(key: K): F[(K, Option[V])]
  
  def hrandfieldWithvalues(key: K, count: Long): F[Seq[(K, Option[V])]]
  
  def hscan(key: K): F[DataScanCursor[(K, V)]]
  
  def hscan(key: K, scanArgs: ScanArgs): F[DataScanCursor[(K, V)]]
  
  def hscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[(K, V)]]
  
  def hscan(key: K, scanCursor: ScanCursor): F[DataScanCursor[(K, V)]]
  
  def hset(key: K, field: K, value: V): F[Boolean]
  
  def hset(key: K, map: Map[K, V]): F[Long]
  
  def hsetnx(key: K, field: K, value: V): F[Boolean]
  
  def hstrlen(key: K, field: K): F[Long]
  
  def hvals(key: K): F[Seq[V]]
  
}
