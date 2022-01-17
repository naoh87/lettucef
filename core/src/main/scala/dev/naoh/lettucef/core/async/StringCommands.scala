// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.StringCommandsF
import dev.naoh.lettucef.api.Commands
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


trait StringCommands[F[_], K, V] extends CommandsDeps[F, K, V] with StringCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisStringAsyncCommands[K, V]
  
  def append(key: K, value: V): F[F[Long]] =
    JF.toAsync(underlying.append(key, value)).map(_.map(Long2long))
  
  def bitcount(key: K): F[F[Long]] =
    JF.toAsync(underlying.bitcount(key)).map(_.map(Long2long))
  
  def bitcount(key: K, start: Long, end: Long): F[F[Long]] =
    JF.toAsync(underlying.bitcount(key, start, end)).map(_.map(Long2long))
  
  def bitfield(key: K, bitFieldArgs: BitFieldArgs): F[F[Seq[Long]]] =
    JF.toAsync(underlying.bitfield(key, bitFieldArgs)).map(_.map(_.asScala.toSeq.map(Long2long)))
  
  def bitpos(key: K, state: Boolean): F[F[Long]] =
    JF.toAsync(underlying.bitpos(key, state)).map(_.map(Long2long))
  
  def bitpos(key: K, state: Boolean, start: Long): F[F[Long]] =
    JF.toAsync(underlying.bitpos(key, state, start)).map(_.map(Long2long))
  
  def bitpos(key: K, state: Boolean, start: Long, end: Long): F[F[Long]] =
    JF.toAsync(underlying.bitpos(key, state, start, end)).map(_.map(Long2long))
  
  def bitopAnd(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.bitopAnd(destination, keys: _*)).map(_.map(Long2long))
  
  def bitopNot(destination: K, source: K): F[F[Long]] =
    JF.toAsync(underlying.bitopNot(destination, source)).map(_.map(Long2long))
  
  def bitopOr(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.bitopOr(destination, keys: _*)).map(_.map(Long2long))
  
  def bitopXor(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.bitopXor(destination, keys: _*)).map(_.map(Long2long))
  
  def decr(key: K): F[F[Long]] =
    JF.toAsync(underlying.decr(key)).map(_.map(Long2long))
  
  def decrby(key: K, amount: Long): F[F[Long]] =
    JF.toAsync(underlying.decrby(key, amount)).map(_.map(Long2long))
  
  def get(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.get(key)).map(_.map(Option(_)))
  
  def getbit(key: K, offset: Long): F[F[Long]] =
    JF.toAsync(underlying.getbit(key, offset)).map(_.map(Long2long))
  
  def getdel(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.getdel(key)).map(_.map(Option(_)))
  
  def getex(key: K, args: GetExArgs): F[F[Option[V]]] =
    JF.toAsync(underlying.getex(key, args)).map(_.map(Option(_)))
  
  def getrange(key: K, start: Long, end: Long): F[F[V]] =
    JF.toAsync(underlying.getrange(key, start, end))
  
  def getset(key: K, value: V): F[F[Option[V]]] =
    JF.toAsync(underlying.getset(key, value)).map(_.map(Option(_)))
  
  def incr(key: K): F[F[Long]] =
    JF.toAsync(underlying.incr(key)).map(_.map(Long2long))
  
  def incrby(key: K, amount: Long): F[F[Long]] =
    JF.toAsync(underlying.incrby(key, amount)).map(_.map(Long2long))
  
  def incrbyfloat(key: K, amount: Double): F[F[Double]] =
    JF.toAsync(underlying.incrbyfloat(key, amount)).map(_.map(Double2double))
  
  def mget(keys: K*): F[F[Seq[(K, Option[V])]]] =
    JF.toAsync(underlying.mget(keys: _*)).map(_.map(_.asScala.toSeq.map(kv => LettuceValueConverter.fromKeyValue(kv))))
  
  def mset(map: Map[K, V]): F[F[String]] =
    JF.toAsync(underlying.mset(map.asJava))
  
  def msetnx(map: Map[K, V]): F[F[Boolean]] =
    JF.toAsync(underlying.msetnx(map.asJava)).map(_.map(Boolean2boolean))
  
  def set(key: K, value: V): F[F[String]] =
    JF.toAsync(underlying.set(key, value))
  
  def set(key: K, value: V, setArgs: SetArgs): F[F[Option[String]]] =
    JF.toAsync(underlying.set(key, value, setArgs)).map(_.map(Option(_)))
  
  def setGet(key: K, value: V): F[F[Option[V]]] =
    JF.toAsync(underlying.setGet(key, value)).map(_.map(Option(_)))
  
  def setGet(key: K, value: V, setArgs: SetArgs): F[F[Option[V]]] =
    JF.toAsync(underlying.setGet(key, value, setArgs)).map(_.map(Option(_)))
  
  def setbit(key: K, offset: Long, value: Int): F[F[Long]] =
    JF.toAsync(underlying.setbit(key, offset, value)).map(_.map(Long2long))
  
  def setex(key: K, seconds: Long, value: V): F[F[String]] =
    JF.toAsync(underlying.setex(key, seconds, value))
  
  def psetex(key: K, milliseconds: Long, value: V): F[F[String]] =
    JF.toAsync(underlying.psetex(key, milliseconds, value))
  
  def setnx(key: K, value: V): F[F[Boolean]] =
    JF.toAsync(underlying.setnx(key, value)).map(_.map(Boolean2boolean))
  
  def setrange(key: K, offset: Long, value: V): F[F[Long]] =
    JF.toAsync(underlying.setrange(key, offset, value)).map(_.map(Long2long))
  
  def stralgoLcs(strAlgoArgs: StrAlgoArgs): F[F[StringMatchResult]] =
    JF.toAsync(underlying.stralgoLcs(strAlgoArgs))
  
  def strlen(key: K): F[F[Long]] =
    JF.toAsync(underlying.strlen(key)).map(_.map(Long2long))
  
}