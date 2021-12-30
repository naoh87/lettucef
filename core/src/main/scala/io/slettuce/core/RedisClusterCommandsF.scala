package io.slettuce.core

import cats.effect.kernel.Async
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands
import io.slettuce.core.commands._
import scala.reflect.ClassTag

final class RedisClusterCommandsF[F[_], K, V](
  protected val underlying: RedisAdvancedClusterAsyncCommands[K, V]
)(implicit F: Async[F], V: ClassTag[V])
  extends AsyncCallCommands[F, K, V]
    with BaseCommands[F, K, V]
    with ClusterCommands[F, K, V]
    with GeoCommands[F, K, V]
    with HashCommands[F, K, V]
    with HLLCommands[F, K, V]
    with KeyCommands[F, K, V]
    with ListCommands[F, K, V]
    with ServerCommands[F, K, V]
    with SetCommands[F, K, V]
    with SortedSetCommands[F, K, V]
    with StreamCommands[F, K, V]
    with StringCommands[F, K, V] {
  implicit protected val _async: Async[F] = F
  implicit protected val _valueTag: ClassTag[V] = V
}
