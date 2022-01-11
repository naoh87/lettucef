// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.BitFieldArgs
import io.lettuce.core.GetExArgs
import io.lettuce.core.SetArgs
import io.lettuce.core.StrAlgoArgs
import io.lettuce.core.StringMatchResult
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait StringCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisStringAsyncCommands[K, V]
  
  def append(key: K, value: V): F[Long] =
    JF.toSync(underlying.append(key, value)).map(Long2long)
  
  def bitcount(key: K): F[Long] =
    JF.toSync(underlying.bitcount(key)).map(Long2long)
  
  def bitcount(key: K, start: Long, end: Long): F[Long] =
    JF.toSync(underlying.bitcount(key, start, end)).map(Long2long)
  
  def bitfield(key: K, bitFieldArgs: BitFieldArgs): F[Seq[Long]] =
    JF.toSync(underlying.bitfield(key, bitFieldArgs)).map(_.asScala.toSeq.map(Long2long))
  
  def bitpos(key: K, state: Boolean): F[Long] =
    JF.toSync(underlying.bitpos(key, state)).map(Long2long)
  
  def bitpos(key: K, state: Boolean, start: Long): F[Long] =
    JF.toSync(underlying.bitpos(key, state, start)).map(Long2long)
  
  def bitpos(key: K, state: Boolean, start: Long, end: Long): F[Long] =
    JF.toSync(underlying.bitpos(key, state, start, end)).map(Long2long)
  
  def bitopAnd(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.bitopAnd(destination, keys: _*)).map(Long2long)
  
  def bitopNot(destination: K, source: K): F[Long] =
    JF.toSync(underlying.bitopNot(destination, source)).map(Long2long)
  
  def bitopOr(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.bitopOr(destination, keys: _*)).map(Long2long)
  
  def bitopXor(destination: K, keys: K*): F[Long] =
    JF.toSync(underlying.bitopXor(destination, keys: _*)).map(Long2long)
  
  def decr(key: K): F[Long] =
    JF.toSync(underlying.decr(key)).map(Long2long)
  
  def decrby(key: K, amount: Long): F[Long] =
    JF.toSync(underlying.decrby(key, amount)).map(Long2long)
  
  def get(key: K): F[Option[V]] =
    JF.toSync(underlying.get(key)).map(Option(_))
  
  def getbit(key: K, offset: Long): F[Long] =
    JF.toSync(underlying.getbit(key, offset)).map(Long2long)
  
  def getdel(key: K): F[Option[V]] =
    JF.toSync(underlying.getdel(key)).map(Option(_))
  
  def getex(key: K, args: GetExArgs): F[Option[V]] =
    JF.toSync(underlying.getex(key, args)).map(Option(_))
  
  def getrange(key: K, start: Long, end: Long): F[V] =
    JF.toSync(underlying.getrange(key, start, end))
  
  def getset(key: K, value: V): F[Option[V]] =
    JF.toSync(underlying.getset(key, value)).map(Option(_))
  
  def incr(key: K): F[Long] =
    JF.toSync(underlying.incr(key)).map(Long2long)
  
  def incrby(key: K, amount: Long): F[Long] =
    JF.toSync(underlying.incrby(key, amount)).map(Long2long)
  
  def incrbyfloat(key: K, amount: Double): F[Double] =
    JF.toSync(underlying.incrbyfloat(key, amount)).map(Double2double)
  
  def mget(keys: K*): F[Seq[(K, Option[V])]] =
    JF.toSync(underlying.mget(keys: _*)).map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv)))
  
  def mset(map: Map[K, V]): F[String] =
    JF.toSync(underlying.mset(map.asJava))
  
  def msetnx(map: Map[K, V]): F[Boolean] =
    JF.toSync(underlying.msetnx(map.asJava)).map(Boolean2boolean)
  
  def set(key: K, value: V): F[String] =
    JF.toSync(underlying.set(key, value))
  
  def set(key: K, value: V, setArgs: SetArgs): F[Option[String]] =
    JF.toSync(underlying.set(key, value, setArgs)).map(Option(_))
  
  def setGet(key: K, value: V): F[Option[V]] =
    JF.toSync(underlying.setGet(key, value)).map(Option(_))
  
  def setGet(key: K, value: V, setArgs: SetArgs): F[Option[V]] =
    JF.toSync(underlying.setGet(key, value, setArgs)).map(Option(_))
  
  def setbit(key: K, offset: Long, value: Int): F[Long] =
    JF.toSync(underlying.setbit(key, offset, value)).map(Long2long)
  
  def setex(key: K, seconds: Long, value: V): F[String] =
    JF.toSync(underlying.setex(key, seconds, value))
  
  def psetex(key: K, milliseconds: Long, value: V): F[String] =
    JF.toSync(underlying.psetex(key, milliseconds, value))
  
  def setnx(key: K, value: V): F[Boolean] =
    JF.toSync(underlying.setnx(key, value)).map(Boolean2boolean)
  
  def setrange(key: K, offset: Long, value: V): F[Long] =
    JF.toSync(underlying.setrange(key, offset, value)).map(Long2long)
  
  def stralgoLcs(strAlgoArgs: StrAlgoArgs): F[StringMatchResult] =
    JF.toSync(underlying.stralgoLcs(strAlgoArgs))
  
  def strlen(key: K): F[Long] =
    JF.toSync(underlying.strlen(key)).map(Long2long)
  
}
