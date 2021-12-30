package io.lettucef.core.models.stream

import scala.jdk.CollectionConverters._

case class ClaimedMessages[K, V](id: String, messages: Seq[StreamMessage[K, V]])

object ClaimedMessages {
  def from[K, V](jcm: io.lettuce.core.models.stream.ClaimedMessages[K, V]): ClaimedMessages[K, V] =
    ClaimedMessages(jcm.getId, jcm.getMessages.asScala.toSeq.map(StreamMessage.from))
}

case class StreamMessage[K, V](stream: K, id: String, body: Map[K, V]) {
  def isEmpty: Boolean = body.isEmpty
}

object StreamMessage {
  def from[K, V](jsm: io.lettuce.core.StreamMessage[K, V]): StreamMessage[K, V] =
    StreamMessage(
      jsm.getStream,
      jsm.getId,
      if (jsm.getBody eq null) Map.empty else jsm.getBody.asScala.toMap)
}

case class PendingMessage(
  id: String,
  consumer: String, //This should be K. but lettuce api is unsoundness
  msSinceLastDelivery: Long,
  redeliveryCount: Long
)

object PendingMessage {
  def from(jpm: io.lettuce.core.models.stream.PendingMessage): PendingMessage =
    PendingMessage(
      jpm.getId,
      jpm.getConsumer,
      jpm.getMsSinceLastDelivery,
      jpm.getRedeliveryCount
    )
}

case class PendingMessages(
  count: Long,
  messageIds: io.lettuce.core.Range[String],
  consumerMessageCount: Map[String, Long]
)
object PendingMessages {
  def from(jpm: io.lettuce.core.models.stream.PendingMessages): PendingMessages =
    PendingMessages(
      jpm.getCount,
      jpm.getMessageIds,
      jpm.getConsumerMessageCount.asScala.view.mapValues(Long2long).toMap
    )
}