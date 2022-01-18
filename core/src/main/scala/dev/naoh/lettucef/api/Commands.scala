package dev.naoh.lettucef.api

import dev.naoh.lettucef.api.commands._

object Commands {
  type Compose[F[_], G[_]] = {type R[A] = F[G[A]]}

  type CommonSyncCommandsF[F[_], K, V] =
    AclCommandsF[F, K, V]
      with BaseCommandsF[F, K, V]
      with GeoCommandsF[F, K, V]
      with HashCommandsF[F, K, V]
      with HLLCommandsF[F, K, V]
      with KeyCommandsF[F, K, V]
      with ListCommandsF[F, K, V]
      with ScriptingCommandsF[F, K, V]
      with EvalScriptingCommandsF[F, K, V]
      with ServerCommandsF[F, K, V]
      with SetCommandsF[F, K, V]
      with SortedSetCommandsF[F, K, V]
      with StreamCommandsF[F, K, V]
      with StringCommandsF[F, K, V]

  type CommonAsyncCommandsF[F[_], K, V] = CommonSyncCommandsF[Compose[F, F]#R, K, V]
}

