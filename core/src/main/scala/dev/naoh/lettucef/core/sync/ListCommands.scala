// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.ListCommandsF
import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.LMoveArgs
import io.lettuce.core.LPosArgs
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait ListCommands[F[_], K, V] extends CommandsDeps[F, K, V] with ListCommandsF[F, K, V] {

  protected val underlying: RedisListAsyncCommands[K, V]
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Long): F[Option[V]] =
    JF.toSync(underlying.blmove(source, destination, args, timeout)).map(Option(_))
  
  def blmove(source: K, destination: K, args: LMoveArgs, timeout: Double): F[Option[V]] =
    JF.toSync(underlying.blmove(source, destination, args, timeout)).map(Option(_))
  
  def blpop(timeout: Long, keys: K*): F[Option[(K, V)]] =
    JF.toSync(underlying.blpop(timeout, keys: _*)).map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv)))
  
  def blpop(timeout: Double, keys: K*): F[Option[(K, V)]] =
    JF.toSync(underlying.blpop(timeout, keys: _*)).map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv)))
  
  def brpop(timeout: Long, keys: K*): F[Option[(K, V)]] =
    JF.toSync(underlying.brpop(timeout, keys: _*)).map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv)))
  
  def brpop(timeout: Double, keys: K*): F[Option[(K, V)]] =
    JF.toSync(underlying.brpop(timeout, keys: _*)).map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv)))
  
  def brpoplpush(timeout: Long, source: K, destination: K): F[Option[V]] =
    JF.toSync(underlying.brpoplpush(timeout, source, destination)).map(Option(_))
  
  def brpoplpush(timeout: Double, source: K, destination: K): F[Option[V]] =
    JF.toSync(underlying.brpoplpush(timeout, source, destination)).map(Option(_))
  
  def lindex(key: K, index: Long): F[Option[V]] =
    JF.toSync(underlying.lindex(key, index)).map(Option(_))
  
  def linsert(key: K, before: Boolean, pivot: V, value: V): F[Long] =
    JF.toSync(underlying.linsert(key, before, pivot, value)).map(Long2long)
  
  def llen(key: K): F[Long] =
    JF.toSync(underlying.llen(key)).map(Long2long)
  
  def lmove(source: K, destination: K, args: LMoveArgs): F[Option[V]] =
    JF.toSync(underlying.lmove(source, destination, args)).map(Option(_))
  
  def lpop(key: K): F[Option[V]] =
    JF.toSync(underlying.lpop(key)).map(Option(_))
  
  def lpop(key: K, count: Long): F[Seq[V]] =
    JF.toSync(underlying.lpop(key, count)).map(_.asScala.toSeq)
  
  def lpos(key: K, value: V): F[Option[Long]] =
    JF.toSync(underlying.lpos(key, value)).map(Option(_).map(Long2long))
  
  def lpos(key: K, value: V, args: LPosArgs): F[Option[Long]] =
    JF.toSync(underlying.lpos(key, value, args)).map(Option(_).map(Long2long))
  
  def lpos(key: K, value: V, count: Int): F[Seq[Long]] =
    JF.toSync(underlying.lpos(key, value, count)).map(_.asScala.toSeq.map(Long2long))
  
  def lpos(key: K, value: V, count: Int, args: LPosArgs): F[Seq[Long]] =
    JF.toSync(underlying.lpos(key, value, count, args)).map(_.asScala.toSeq.map(Long2long))
  
  def lpush(key: K, values: V*): F[Long] =
    JF.toSync(underlying.lpush(key, values: _*)).map(Long2long)
  
  def lpushx(key: K, values: V*): F[Long] =
    JF.toSync(underlying.lpushx(key, values: _*)).map(Long2long)
  
  def lrange(key: K, start: Long, stop: Long): F[Seq[V]] =
    JF.toSync(underlying.lrange(key, start, stop)).map(_.asScala.toSeq)
  
  def lrem(key: K, count: Long, value: V): F[Long] =
    JF.toSync(underlying.lrem(key, count, value)).map(Long2long)
  
  def lset(key: K, index: Long, value: V): F[String] =
    JF.toSync(underlying.lset(key, index, value))
  
  def ltrim(key: K, start: Long, stop: Long): F[String] =
    JF.toSync(underlying.ltrim(key, start, stop))
  
  def rpop(key: K): F[Option[V]] =
    JF.toSync(underlying.rpop(key)).map(Option(_))
  
  def rpop(key: K, count: Long): F[Seq[V]] =
    JF.toSync(underlying.rpop(key, count)).map(_.asScala.toSeq)
  
  def rpoplpush(source: K, destination: K): F[Option[V]] =
    JF.toSync(underlying.rpoplpush(source, destination)).map(Option(_))
  
  def rpush(key: K, values: V*): F[Long] =
    JF.toSync(underlying.rpush(key, values: _*)).map(Long2long)
  
  def rpushx(key: K, values: V*): F[Long] =
    JF.toSync(underlying.rpushx(key, values: _*)).map(Long2long)
  
}