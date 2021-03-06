package dev.naoh.lettucef.api.models.pubsub

sealed trait RedisPushed[+K, +V]

object RedisPushed {
  case class Message[K, V](channel: K, message: V) extends RedisPushed[K, V]

  case class Subscribed[K](channel: K, count: Long) extends RedisPushed[K, Nothing]

  case class Unsubscribed[K](channel: K, count: Long) extends RedisPushed[K, Nothing]

  case class PMessage[K, V](pattern: K, channel: K, message: V) extends RedisPushed[K, V]

  case class PSubscribed[K](pattern: K, count: Long) extends RedisPushed[K, Nothing]

  case class PUnsubscribed[K](pattern: K, count: Long) extends RedisPushed[K, Nothing]

}
