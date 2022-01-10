package dev.naoh.lettucef.core

import cats.effect.kernel.Async
import dev.naoh.lettucef.core.sync.SentinelCommands
import dev.naoh.lettucef.core.util.JavaFutureUtil
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands
import scala.reflect.ClassTag


class RedisSentinelCommandsF[F[_], K, V](
  conn: StatefulRedisSentinelConnection[K, V],
  codec: RedisCodec[K, V]
)(implicit F: Async[F], V: ClassTag[V], K: ClassTag[K]
) extends SentinelCommands[F, K, V] {
  protected val underlying: RedisSentinelAsyncCommands[K, V] = conn.async()
  implicit protected val _async: Async[F] = F
  implicit protected val _valueTag: ClassTag[V] = V
  implicit protected val _keyTag: ClassTag[K] = K
  protected val dispatchHelper: ManualDispatchHelper[K, V] = new ManualDispatchHelper(codec)

  def closeAsync(): F[Unit] =
    F.void(JavaFutureUtil.toSync(conn.closeAsync())(F))
}
