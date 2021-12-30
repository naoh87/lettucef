package io.slettuce.core.models

import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

sealed trait RedisData[+V]

object RedisData {
  case class RedisString(value: String) extends RedisData[Nothing]

  case class RedisInteger(value: Long) extends RedisData[Nothing]

  case class RedisArray[V](arr: Seq[RedisData[V]]) extends RedisData[V]

  case class RedisBulk[V](value: V) extends RedisData[V]

  case object RedisNull extends RedisData[Nothing]

  def from[V: ClassTag](j: Any): RedisData[V] =
    j match {
      case null => RedisNull
      case v: V => RedisBulk(v)
      case v: Long => RedisInteger(v)
      case v: String => RedisString(v)
      case a: java.util.List[_] =>
        RedisArray(a.asScala.toSeq.map(from[V]))
    }
}
