// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import io.lettuce.core.BitFieldArgs
import io.lettuce.core.GetExArgs
import io.lettuce.core.SetArgs
import io.lettuce.core.StrAlgoArgs
import io.lettuce.core.StringMatchResult


trait StringCommandsF[F[_], K, V] {

  def append(key: K, value: V): F[Long]
  
  def bitcount(key: K): F[Long]
  
  def bitcount(key: K, start: Long, end: Long): F[Long]
  
  def bitfield(key: K, bitFieldArgs: BitFieldArgs): F[Seq[Long]]
  
  def bitpos(key: K, state: Boolean): F[Long]
  
  def bitpos(key: K, state: Boolean, start: Long): F[Long]
  
  def bitpos(key: K, state: Boolean, start: Long, end: Long): F[Long]
  
  def bitopAnd(destination: K, keys: K*): F[Long]
  
  def bitopNot(destination: K, source: K): F[Long]
  
  def bitopOr(destination: K, keys: K*): F[Long]
  
  def bitopXor(destination: K, keys: K*): F[Long]
  
  def decr(key: K): F[Long]
  
  def decrby(key: K, amount: Long): F[Long]
  
  def get(key: K): F[Option[V]]
  
  def getbit(key: K, offset: Long): F[Long]
  
  def getdel(key: K): F[Option[V]]
  
  def getex(key: K, args: GetExArgs): F[Option[V]]
  
  def getrange(key: K, start: Long, end: Long): F[V]
  
  def getset(key: K, value: V): F[Option[V]]
  
  def incr(key: K): F[Long]
  
  def incrby(key: K, amount: Long): F[Long]
  
  def incrbyfloat(key: K, amount: Double): F[Double]
  
  def mget(keys: K*): F[Seq[(K, Option[V])]]
  
  def mset(map: Map[K, V]): F[String]
  
  def msetnx(map: Map[K, V]): F[Boolean]
  
  def set(key: K, value: V): F[String]
  
  def set(key: K, value: V, setArgs: SetArgs): F[Option[String]]
  
  def setGet(key: K, value: V): F[Option[V]]
  
  def setGet(key: K, value: V, setArgs: SetArgs): F[Option[V]]
  
  def setbit(key: K, offset: Long, value: Int): F[Long]
  
  def setex(key: K, seconds: Long, value: V): F[String]
  
  def psetex(key: K, milliseconds: Long, value: V): F[String]
  
  def setnx(key: K, value: V): F[Boolean]
  
  def setrange(key: K, offset: Long, value: V): F[Long]
  
  def stralgoLcs(strAlgoArgs: StrAlgoArgs): F[StringMatchResult]
  
  def strlen(key: K): F[Long]
  
}
