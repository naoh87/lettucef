package dev.naoh.lettucef.core

import dev.naoh.lettucef.api.Commands

trait CommonConnectionF[F[_], K, V] {
  def sync(): Commands.CommonSyncCommandsF[F, K, V]

  def async(): Commands.CommonAsyncCommandsF[F, K, V]
}
