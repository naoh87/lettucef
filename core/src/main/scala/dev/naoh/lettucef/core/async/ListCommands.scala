// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.ListCommandsF
import dev.naoh.lettucef.api.Commands
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.LMoveArgs
import io.lettuce.core.LPosArgs
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait ListCommands[F[_], K, V] extends CommandsDeps[F, K, V] with ListCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisListAsyncCommands[K, V]
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Long): F[F[Option[V]]] =
    JF.toAsync(underlying.blmove(source, destination, args, timeout)).map(_.map(Option(_)))
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Double): F[F[Option[V]]] =
    JF.toAsync(underlying.blmove(source, destination, args, timeout)).map(_.map(Option(_)))
  
  def blpop(timeout: Long, keys: K*): F[F[Option[(K, V)]]] =
    JF.toAsync(underlying.blpop(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv))))
  
  def blpop(timeout: Double, keys: K*): F[F[Option[(K, V)]]] =
    JF.toAsync(underlying.blpop(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv))))
  
  def brpop(timeout: Long, keys: K*): F[F[Option[(K, V)]]] =
    JF.toAsync(underlying.brpop(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv))))
  
  def brpop(timeout: Double, keys: K*): F[F[Option[(K, V)]]] =
    JF.toAsync(underlying.brpop(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv))))
  
  def brpoplpush(timeout: Long, source: K, destination: K): F[F[Option[V]]] =
    JF.toAsync(underlying.brpoplpush(timeout, source, destination)).map(_.map(Option(_)))
  
  def brpoplpush(timeout: Double, source: K, destination: K): F[F[Option[V]]] =
    JF.toAsync(underlying.brpoplpush(timeout, source, destination)).map(_.map(Option(_)))
  
  def lindex(key: K, index: Long): F[F[Option[V]]] =
    JF.toAsync(underlying.lindex(key, index)).map(_.map(Option(_)))
  
  def linsert(key: K, before: Boolean, pivot: V, value: V): F[F[Long]] =
    JF.toAsync(underlying.linsert(key, before, pivot, value)).map(_.map(Long2long))
  
  def llen(key: K): F[F[Long]] =
    JF.toAsync(underlying.llen(key)).map(_.map(Long2long))
  
  def lmove(source: K, destination: K, args: LMoveArgs): F[F[Option[V]]] =
    JF.toAsync(underlying.lmove(source, destination, args)).map(_.map(Option(_)))
  
  def lpop(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.lpop(key)).map(_.map(Option(_)))
  
  def lpop(key: K, count: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.lpop(key, count)).map(_.map(_.asScala.toSeq))
  
  def lpos(key: K, value: V): F[F[Option[Long]]] =
    JF.toAsync(underlying.lpos(key, value)).map(_.map(Option(_).map(Long2long)))
  
  def lpos(key: K, value: V, args: LPosArgs): F[F[Option[Long]]] =
    JF.toAsync(underlying.lpos(key, value, args)).map(_.map(Option(_).map(Long2long)))
  
  def lpos(key: K, value: V, count: Int): F[F[Seq[Long]]] =
    JF.toAsync(underlying.lpos(key, value, count)).map(_.map(_.asScala.toSeq.map(Long2long)))
  
  def lpos(key: K, value: V, count: Int, args: LPosArgs): F[F[Seq[Long]]] =
    JF.toAsync(underlying.lpos(key, value, count, args)).map(_.map(_.asScala.toSeq.map(Long2long)))
  
  def lpush(key: K, values: V*): F[F[Long]] =
    JF.toAsync(underlying.lpush(key, values: _*)).map(_.map(Long2long))
  
  def lpushx(key: K, values: V*): F[F[Long]] =
    JF.toAsync(underlying.lpushx(key, values: _*)).map(_.map(Long2long))
  
  def lrange(key: K, start: Long, stop: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.lrange(key, start, stop)).map(_.map(_.asScala.toSeq))
  
  def lrem(key: K, count: Long, value: V): F[F[Long]] =
    JF.toAsync(underlying.lrem(key, count, value)).map(_.map(Long2long))
  
  def lset(key: K, index: Long, value: V): F[F[String]] =
    JF.toAsync(underlying.lset(key, index, value))
  
  def ltrim(key: K, start: Long, stop: Long): F[F[String]] =
    JF.toAsync(underlying.ltrim(key, start, stop))
  
  def rpop(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.rpop(key)).map(_.map(Option(_)))
  
  def rpop(key: K, count: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.rpop(key, count)).map(_.map(_.asScala.toSeq))
  
  def rpoplpush(source: K, destination: K): F[F[Option[V]]] =
    JF.toAsync(underlying.rpoplpush(source, destination)).map(_.map(Option(_)))
  
  def rpush(key: K, values: V*): F[F[Long]] =
    JF.toAsync(underlying.rpush(key, values: _*)).map(_.map(Long2long))
  
  def rpushx(key: K, values: V*): F[F[Long]] =
    JF.toAsync(underlying.rpushx(key, values: _*)).map(_.map(Long2long))
  
}
