package dev.naoh.lettucef.core.util

import io.lettuce.core._


object LettuceValueConverter {
  def toScoredValue[V](sv: (Double, V)): ScoredValue[V] =
    ScoredValue.just(sv._1, sv._2)

  def fromScoredValue[V](sv: ScoredValue[V]): Option[(Double, V)] =
    if (sv.hasValue) {
      Some((sv.getScore, sv.getValue))
    } else {
      None
    }

  def fromScoredValueUnsafe[V](sv: ScoredValue[V]): (Double, V) =
    (sv.getScore, sv.getValue)

  def fromKeyValue[K, V](kv: KeyValue[K, V]): (K, Option[V]) =
    if (kv.hasValue) {
      (kv.getKey, Some(kv.getValue))
    } else {
      (kv.getKey, None)
    }

  def fromKeyValueUnsafe[K, V](kv: KeyValue[K, V]): (K, V) =
    (kv.getKey, kv.getValue)

  def fromValue[V](v: Value[V]): Option[V] =
    if (v.hasValue) {
      Some(v.getValue)
    } else {
      None
    }

  def toValue[V](v: Option[V]): Value[V] =
    v match {
      case Some(value) => Value.just(value)
      case None => Value.empty()
    }
}
