package dev.naoh.lettucef.core.models

import dev.naoh.lettucef.core.util.LettuceValueConverter
import io.lettuce.core.ScanCursor
import scala.jdk.CollectionConverters._

class DataScanCursor[A](val elements: Seq[A], cursor: String, finished: Boolean) extends ScanCursor(cursor, finished) {
  def map[B](f: A => B): DataScanCursor[B] =
    new DataScanCursor[B](elements.map(f), cursor, finished)
}

object DataScanCursor {
  def from[V](j: io.lettuce.core.ValueScanCursor[V]): DataScanCursor[V] =
    new DataScanCursor(j.getValues.asScala.toSeq, j.getCursor, j.isFinished)

  def from[K, V](j: io.lettuce.core.MapScanCursor[K, V]): DataScanCursor[(K, V)] =
    new DataScanCursor(j.getMap.asScala.toSeq, j.getCursor, j.isFinished)

  def from[K](j: io.lettuce.core.KeyScanCursor[K]): DataScanCursor[K] =
    new DataScanCursor(j.getKeys.asScala.toSeq, j.getCursor, j.isFinished)

  def from[V](j: io.lettuce.core.ScoredValueScanCursor[V]): DataScanCursor[(Double, V)] =
    new DataScanCursor(j.getValues.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe), j.getCursor, j.isFinished)
}