package dev.naoh.lettucef.core

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.commands.AclCommands
import dev.naoh.lettucef.core.commands.AsyncCallCommands
import dev.naoh.lettucef.core.commands.BaseCommands
import dev.naoh.lettucef.core.commands.ClusterCommands
import dev.naoh.lettucef.core.commands.GeoCommands
import dev.naoh.lettucef.core.commands.HLLCommands
import dev.naoh.lettucef.core.commands.HashCommands
import dev.naoh.lettucef.core.commands.KeyCommands
import dev.naoh.lettucef.core.commands.ListCommands
import dev.naoh.lettucef.core.commands.ScriptingCommands
import dev.naoh.lettucef.core.commands.ServerCommands
import dev.naoh.lettucef.core.commands.SetCommands
import dev.naoh.lettucef.core.commands.SortedSetCommands
import dev.naoh.lettucef.core.commands.StreamCommands
import dev.naoh.lettucef.core.commands.StringCommands
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands
import io.lettuce.core.codec.RedisCodec
import dev.naoh.lettucef.core.commands._
import scala.reflect.ClassTag

final class RedisClusterCommandsF[F[_], K, V](
  protected val underlying: RedisAdvancedClusterAsyncCommands[K, V],
  codec: RedisCodec[K, V]
)(implicit F: Async[F], V: ClassTag[V], K: ClassTag[K])
  extends AsyncCallCommands[F, K, V]
    with AclCommands[F, K, V]
    with BaseCommands[F, K, V]
    with ClusterCommands[F, K, V]
    with GeoCommands[F, K, V]
    with HashCommands[F, K, V]
    with HLLCommands[F, K, V]
    with KeyCommands[F, K, V]
    with ListCommands[F, K, V]
    with ScriptingCommands[F, K, V]
    with ServerCommands[F, K, V]
    with SetCommands[F, K, V]
    with SortedSetCommands[F, K, V]
    with StreamCommands[F, K, V]
    with StringCommands[F, K, V] {
  implicit protected val _async: Async[F] = F
  implicit protected val _valueTag: ClassTag[V] = V
  implicit protected val _keyTag: ClassTag[K] = K
  protected val dispatchHelper: ManualDispatchHelper[K, V] = new ManualDispatchHelper(codec)
}
