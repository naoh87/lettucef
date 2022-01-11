package dev.naoh.lettucef.api.models

import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

sealed trait RedisData[+V] {

  def decodeAs[A](f: PartialFunction[RedisData[V], Option[A]]): Option[A] =
    f.lift(this).flatten
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

  def from[V: ClassTag](j: Any): RedisData[V] =
    j match {
      case null => RedisNull
      case v: V => RedisBulk(v)
      case v: Long => RedisInteger(v)
      case v: String => RedisString(v)
      case a: java.util.List[_] =>
        val b = List.newBuilder[RedisData[V]]
        a.asScala.foreach(e => b.addOne(from[V](e)))
        RedisArray(b.result())
      case v: Double => RedisDouble(v)
      case v: Boolean => RedisBoolean(v)
    }
}
