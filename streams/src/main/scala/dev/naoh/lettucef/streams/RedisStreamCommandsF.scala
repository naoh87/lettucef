package dev.naoh.lettucef.streams

import dev.naoh.lettucef.core.RedisClusterConnectionF
import dev.naoh.lettucef.core.RedisConnectionF
import dev.naoh.lettucef.streams.commands.ScanStreamCommands

final class RedisStreamCommandsF[F[_], K, V](
  protected val underlying: ScanStreamCommands.Underlying[F, K, V]
) extends ScanStreamCommands[F, K, V]

trait StreamCommandApiOps {
  implicit class RedisClusterConnectionFOps[F[_], K, V](conn: RedisClusterConnectionF[F, K, V]) {
    def stream(): RedisStreamCommandsF[F, K, V] = new RedisStreamCommandsF(conn.async())
  }

  implicit class RedisConnectionFOps[F[_], K, V](conn: RedisConnectionF[F, K, V]) {
    def stream(): RedisStreamCommandsF[F, K, V] = new RedisStreamCommandsF(conn.async())
  }
}