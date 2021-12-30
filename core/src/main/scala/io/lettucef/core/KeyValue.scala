package io.lettucef.core

/**
 * This class is compatible for io.lettuce.core.KeyValue but not null
 */
case class KeyValue[K, V](key: K, value: V)

object KeyValue {
  def fromLettuce[K, V](kv: io.lettuce.core.KeyValue[K, V]): KeyValue[K, Option[V]] =
    KeyValue(kv.getKey, Option(kv.getValue))
}
