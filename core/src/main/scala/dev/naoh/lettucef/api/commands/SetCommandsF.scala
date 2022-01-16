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


trait SetCommandsF[F[_], K, V] {

  def sadd(key: K, members: V*): F[Long]
  
  def scard(key: K): F[Long]
  
  def sdiff(keys: K*): F[Set[V]]
  
  def sdiffstore(destination: K, keys: K*): F[Long]
  
  def sinter(keys: K*): F[Set[V]]
  
  def sinterstore(destination: K, keys: K*): F[Long]
  
  def sismember(key: K, member: V): F[Boolean]
  
  def smembers(key: K): F[Set[V]]
  
  def smismember(key: K, members: V*): F[Seq[Boolean]]
  
  def smove(source: K, destination: K, member: V): F[Boolean]
  
  def spop(key: K): F[Option[V]]
  
  def spop(key: K, count: Long): F[Set[V]]
  
  def srandmember(key: K): F[Option[V]]
  
  def srandmember(key: K, count: Long): F[Seq[V]]
  
  def srem(key: K, members: V*): F[Long]
  
  def sunion(keys: K*): F[Set[V]]
  
  def sunionstore(destination: K, keys: K*): F[Long]
  
  def sscan(key: K): F[DataScanCursor[V]]
  
  def sscan(key: K, scanArgs: ScanArgs): F[DataScanCursor[V]]
  
  def sscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[V]]
  
  def sscan(key: K, scanCursor: ScanCursor): F[DataScanCursor[V]]
  
}
