package dev.naoh.lettucef.api

import dev.naoh.lettucef.api.commands._

object Commands {
  type Compose[F[_], G[_]] = {type R[A] = F[G[A]]}

  trait ClusterSync[F[_], K, V] extends
    AclCommandsF[F, K, V] with
    BaseCommandsF[F, K, V] with
    ClusterCommandsF[F, K, V] with
    GeoCommandsF[F, K, V] with
    HashCommandsF[F, K, V] with
    HLLCommandsF[F, K, V] with
    KeyCommandsF[F, K, V] with
    ListCommandsF[F, K, V] with
    ScriptingCommandsF[F, K, V] with
    EvalScriptingCommandsF[F, K, V] with
    ServerCommandsF[F, K, V] with
    SetCommandsF[F, K, V] with
    SortedSetCommandsF[F, K, V] with
    StreamCommandsF[F, K, V] with
    StringCommandsF[F, K, V]

  trait ClusterAsync[F[_], K, V] extends
    AclCommandsF[Compose[F, F]#R, K, V] with
    BaseCommandsF[Compose[F, F]#R, K, V] with
    ClusterCommandsF[Compose[F, F]#R, K, V] with
    GeoCommandsF[Compose[F, F]#R, K, V] with
    HashCommandsF[Compose[F, F]#R, K, V] with
    HLLCommandsF[Compose[F, F]#R, K, V] with
    KeyCommandsF[Compose[F, F]#R, K, V] with
    ListCommandsF[Compose[F, F]#R, K, V] with
    ScriptingCommandsF[Compose[F, F]#R, K, V] with
    EvalScriptingCommandsF[Compose[F, F]#R, K, V] with
    ServerCommandsF[Compose[F, F]#R, K, V] with
    SetCommandsF[Compose[F, F]#R, K, V] with
    SortedSetCommandsF[Compose[F, F]#R, K, V] with
    StreamCommandsF[Compose[F, F]#R, K, V] with
    StringCommandsF[Compose[F, F]#R, K, V]

  trait BareSync[F[_], K, V] extends
    AclCommandsF[F, K, V] with
    BaseCommandsF[F, K, V] with
    GeoCommandsF[F, K, V] with
    HashCommandsF[F, K, V] with
    HLLCommandsF[F, K, V] with
    KeyCommandsF[F, K, V] with
    ListCommandsF[F, K, V] with
    ScriptingCommandsF[F, K, V] with
    EvalScriptingCommandsF[F, K, V] with
    ServerCommandsF[F, K, V] with
    SetCommandsF[F, K, V] with
    SortedSetCommandsF[F, K, V] with
    StreamCommandsF[F, K, V] with
    StringCommandsF[F, K, V] with
    TransactionCommandsF[F, K, V]

  trait BareAsync[F[_], K, V] extends
    AclCommandsF[Compose[F, F]#R, K, V] with
    BaseCommandsF[Compose[F, F]#R, K, V] with
    GeoCommandsF[Compose[F, F]#R, K, V] with
    HashCommandsF[Compose[F, F]#R, K, V] with
    HLLCommandsF[Compose[F, F]#R, K, V] with
    KeyCommandsF[Compose[F, F]#R, K, V] with
    ListCommandsF[Compose[F, F]#R, K, V] with
    ScriptingCommandsF[Compose[F, F]#R, K, V] with
    EvalScriptingCommandsF[Compose[F, F]#R, K, V] with
    ServerCommandsF[Compose[F, F]#R, K, V] with
    SetCommandsF[Compose[F, F]#R, K, V] with
    SortedSetCommandsF[Compose[F, F]#R, K, V] with
    StreamCommandsF[Compose[F, F]#R, K, V] with
    StringCommandsF[Compose[F, F]#R, K, V] with
    TransactionCommandsF[Compose[F, F]#R, K, V]

}

