package dev.naoh.lettucef.api.models

import dev.naoh.lettucef.core.util.LettuceValueConverter
import io.lettuce.core.ScanCursor
import scala.jdk.CollectionConverters._

class RedisScanCursor[A](val elements: Vector[A], cursor: String, finished: Boolean) extends ScanCursor(cursor, finished) {
  def map[B](f: A => B): RedisScanCursor[B] =
    new RedisScanCursor[B](elements.map(f), cursor, finished)
}

object RedisScanCursor {
  def from[V](j: io.lettuce.core.ValueScanCursor[V]): RedisScanCursor[V] =
    new RedisScanCursor(j.getValues.asScala.toVector, j.getCursor, j.isFinished)

  def from[K, V](j: io.lettuce.core.MapScanCursor[K, V]): RedisScanCursor[(K, V)] =
    new RedisScanCursor(j.getMap.asScala.toVector, j.getCursor, j.isFinished)

  def from[K](j: io.lettuce.core.KeyScanCursor[K]): RedisScanCursor[K] =
    new RedisScanCursor(j.getKeys.asScala.toVector, j.getCursor, j.isFinished)

  def from[V](j: io.lettuce.core.ScoredValueScanCursor[V]): RedisScanCursor[(Double, V)] =
    new RedisScanCursor(j.getValues.asScala.toVector.map(LettuceValueConverter.fromScoredValueUnsafe), j.getCursor, j.isFinished)
}
