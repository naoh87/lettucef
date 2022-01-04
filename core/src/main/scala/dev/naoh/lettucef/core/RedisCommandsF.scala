package dev.naoh.lettucef.core

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.commands._
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.codec.RedisCodec
import scala.reflect.ClassTag

final class RedisCommandsF[F[_], K, V](
  protected val underlying: RedisAsyncCommands[K, V],
  codec: RedisCodec[K, V]
)(implicit F: Async[F], V: ClassTag[V], K: ClassTag[K])
  extends AsyncCallCommands[F, K, V]
    with AclCommands[F, K, V]
    with BaseCommands[F, K, V]
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
