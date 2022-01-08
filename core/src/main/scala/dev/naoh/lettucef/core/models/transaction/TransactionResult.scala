package dev.naoh.lettucef.core.models.transaction

import dev.naoh.lettucef.core.models.RedisData
import scala.reflect.ClassTag

case class TransactionResult[V](
  wasDiscarded: Boolean,
  result: Seq[RedisData[V]]
)

object TransactionResult {

  import scala.jdk.CollectionConverters._

  def from[V: ClassTag](j: io.lettuce.core.TransactionResult): TransactionResult[V] =
    TransactionResult(
      j.wasDiscarded(),
      j.asScala.map(RedisData.from).toSeq)
}
