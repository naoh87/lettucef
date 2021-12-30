// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import io.lettuce.core.BitFieldArgs
import io.lettuce.core.GetExArgs
import io.lettuce.core.KeyValue
import io.lettuce.core.RedisFuture
import io.lettuce.core.SetArgs
import io.lettuce.core.StrAlgoArgs
import io.lettuce.core.StringMatchResult
import io.lettuce.core.output.KeyValueStreamingChannel
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait StringCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisStringAsyncCommands[K, V]
  
  def append(key: K, value: V): F[Long] =
    JF.toAsync(underlying.append(key, value)).map(Long2long)
  
  def bitcount(key: K): F[Long] =
    JF.toAsync(underlying.bitcount(key)).map(Long2long)
  
  def bitcount(key: K, start: Long, end: Long): F[Long] =
    JF.toAsync(underlying.bitcount(key, start, end)).map(Long2long)
  
  def bitfield(key: K, bitFieldArgs: BitFieldArgs): F[Seq[Long]] =
    JF.toAsync(underlying.bitfield(key, bitFieldArgs)).map(_.asScala.toSeq.map(Long2long))
  
  def bitpos(key: K, state: Boolean): F[Long] =
    JF.toAsync(underlying.bitpos(key, state)).map(Long2long)
  
  def bitpos(key: K, state: Boolean, start: Long): F[Long] =
    JF.toAsync(underlying.bitpos(key, state, start)).map(Long2long)
  
  def bitpos(key: K, state: Boolean, start: Long, end: Long): F[Long] =
    JF.toAsync(underlying.bitpos(key, state, start, end)).map(Long2long)
  
  def bitopAnd(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.bitopAnd(destination, keys: _*)).map(Long2long)
  
  def bitopNot(destination: K, source: K): F[Long] =
    JF.toAsync(underlying.bitopNot(destination, source)).map(Long2long)
  
  def bitopOr(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.bitopOr(destination, keys: _*)).map(Long2long)
  
  def bitopXor(destination: K, keys: K*): F[Long] =
    JF.toAsync(underlying.bitopXor(destination, keys: _*)).map(Long2long)
  
  def decr(key: K): F[Long] =
    JF.toAsync(underlying.decr(key)).map(Long2long)
  
  def decrby(key: K, amount: Long): F[Long] =
    JF.toAsync(underlying.decrby(key, amount)).map(Long2long)
  
  def get(key: K): F[V] =
    JF.toAsync(underlying.get(key))
  
  def getbit(key: K, offset: Long): F[Long] =
    JF.toAsync(underlying.getbit(key, offset)).map(Long2long)
  
  def getdel(key: K): F[V] =
    JF.toAsync(underlying.getdel(key))
  
  def getex(key: K, args: GetExArgs): F[V] =
    JF.toAsync(underlying.getex(key, args))
  
  def getrange(key: K, start: Long, end: Long): F[V] =
    JF.toAsync(underlying.getrange(key, start, end))
  
  def getset(key: K, value: V): F[V] =
    JF.toAsync(underlying.getset(key, value))
  
  def incr(key: K): F[Long] =
    JF.toAsync(underlying.incr(key)).map(Long2long)
  
  def incrby(key: K, amount: Long): F[Long] =
    JF.toAsync(underlying.incrby(key, amount)).map(Long2long)
  
  def incrbyfloat(key: K, amount: Double): F[Double] =
    JF.toAsync(underlying.incrbyfloat(key, amount)).map(Double2double)
  
  def mget(keys: K*): F[Seq[KeyValue[K, V]]] =
    JF.toAsync(underlying.mget(keys: _*)).map(_.asScala.toSeq)
  
  def mget(channel: KeyValueStreamingChannel[K, V], keys: K*): F[Long] =
    JF.toAsync(underlying.mget(channel, keys: _*)).map(Long2long)
  
  def mset(map: Map[K, V]): F[String] =
    JF.toAsync(underlying.mset(map.asJava))
  
  def msetnx(map: Map[K, V]): F[Boolean] =
    JF.toAsync(underlying.msetnx(map.asJava)).map(Boolean2boolean)
  
  def set(key: K, value: V): F[String] =
    JF.toAsync(underlying.set(key, value))
  
  def set(key: K, value: V, setArgs: SetArgs): F[String] =
    JF.toAsync(underlying.set(key, value, setArgs))
  
  def setGet(key: K, value: V): F[V] =
    JF.toAsync(underlying.setGet(key, value))
  
  def setGet(key: K, value: V, setArgs: SetArgs): F[V] =
    JF.toAsync(underlying.setGet(key, value, setArgs))
  
  def setbit(key: K, offset: Long, value: Int): F[Long] =
    JF.toAsync(underlying.setbit(key, offset, value)).map(Long2long)
  
  def setex(key: K, seconds: Long, value: V): F[String] =
    JF.toAsync(underlying.setex(key, seconds, value))
  
  def psetex(key: K, milliseconds: Long, value: V): F[String] =
    JF.toAsync(underlying.psetex(key, milliseconds, value))
  
  def setnx(key: K, value: V): F[Boolean] =
    JF.toAsync(underlying.setnx(key, value)).map(Boolean2boolean)
  
  def setrange(key: K, offset: Long, value: V): F[Long] =
    JF.toAsync(underlying.setrange(key, offset, value)).map(Long2long)
  
  def stralgoLcs(strAlgoArgs: StrAlgoArgs): F[StringMatchResult] =
    JF.toAsync(underlying.stralgoLcs(strAlgoArgs))
  
  def strlen(key: K): F[Long] =
    JF.toAsync(underlying.strlen(key)).map(Long2long)
  
}

