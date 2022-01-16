package dev.naoh.lettucef.streams

import dev.naoh.lettucef.core.CommonConnectionF
import dev.naoh.lettucef.streams.commands.ScanStreamCommands

final class RedisStreamCommandsF[F[_], K, V](
  protected val underlying: ScanStreamCommands.Underlying[F, K, V]
) extends ScanStreamCommands[F, K, V]

trait StreamCommandApiOps {
  implicit class RedisStreamCommandsOps[F[_], K, V](conn: CommonConnectionF[F, K, V]) {
    def stream(): RedisStreamCommandsF[F, K, V] = new RedisStreamCommandsF(conn.sync())
  }
}