package dev.naoh.lettucef.core.models.pubsub

trait PushedMessage[+K, +V]

object PushedMessage {
  case class Message[K, V](channel: K, message: V) extends PushedMessage[K, V]

  case class Subscribed[K](channel: K, count: Long) extends PushedMessage[K, Nothing]

  case class Unsubscribed[K](channel: K, count: Long) extends PushedMessage[K, Nothing]

  case class PMessage[K, V](pattern: K, channel: K, message: V) extends PushedMessage[K, V]

  case class PSubscribed[K](pattern: K, count: Long) extends PushedMessage[K, Nothing]

  case class PUnsubscribed[K](pattern: K, count: Long) extends PushedMessage[K, Nothing]

}
