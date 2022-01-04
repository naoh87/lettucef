package dev.naoh.lettucef.core

import dev.naoh.lettucef.core.commands.ScanStreamCommands

final class RedisStreamCommandsF[F[_], K, V](
  protected val underlying: ScanStreamCommands.Underlying[F, K, V]
) extends ScanStreamCommands[F, K, V]
