package dev.naoh.lettucef.api.models

import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

sealed trait RedisData[+V] {

  def decodeAs[A](f: PartialFunction[RedisData[V], Option[A]]): Option[A] =
    f.lift(this).flatten

  def asList: List[RedisData[V]] = this match {
    case RedisData.RedisArray(arr) => arr
    case _ => List.empty
  }
}

object RedisData {
  case class RedisString(value: String) extends RedisData[Nothing]

  case class RedisInteger(value: Long) extends RedisData[Nothing]

  case class RedisDouble(value: Double) extends RedisData[Nothing]

  case class RedisBoolean(value: Boolean) extends RedisData[Nothing]

  case class RedisError(message: String) extends RedisData[Nothing]

  case class RedisArray[V](arr: List[RedisData[V]]) extends RedisData[V] {
    override def toString: String = arr.mkString("RedisArray(", ", ", ")")
  }

  case class RedisBulk[V](value: V) extends RedisData[V]

  case object RedisNull extends RedisData[Nothing]

}
