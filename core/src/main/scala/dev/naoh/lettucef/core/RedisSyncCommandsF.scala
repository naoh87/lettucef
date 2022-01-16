package dev.naoh.lettucef.core

import cats.effect.kernel.Async
import dev.naoh.lettucef.api.Commands
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.ManualDispatchHelper
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.codec.RedisCodec

final class RedisSyncCommandsF[F[_], K, V](
  protected val underlying: RedisAsyncCommands[K, V],
  codec: RedisCodec[K, V]
)(implicit F: Async[F])
  extends Commands.BareSync[F, K, V]
    with CommandsDeps[F, K, V]
    with sync.AclCommands[F, K, V]
    with sync.BaseCommands[F, K, V]
    with sync.GeoCommands[F, K, V]
    with sync.HashCommands[F, K, V]
    with sync.HLLCommands[F, K, V]
    with sync.KeyCommands[F, K, V]
    with sync.ListCommands[F, K, V]
    with sync.ScriptingCommands[F, K, V]
    with sync.EvalScriptingCommands[F, K, V]
    with sync.ServerCommands[F, K, V]
    with sync.SetCommands[F, K, V]
    with sync.SortedSetCommands[F, K, V]
    with sync.StreamCommands[F, K, V]
    with sync.StringCommands[F, K, V]
    with sync.TransactionCommands[F, K, V] {
  implicit protected val _async: Async[F] = F
  protected val dispatchHelper: ManualDispatchHelper[K, V] = new ManualDispatchHelper(codec)
}

final class RedisAsyncCommandsF[F[_], K, V](
  protected val underlying: RedisAsyncCommands[K, V],
  codec: RedisCodec[K, V]
)(implicit F: Async[F])
  extends Commands.BareAsync[F, K, V]
    with CommandsDeps[F, K, V]
    with async.AclCommands[F, K, V]
    with async.BaseCommands[F, K, V]
    with async.GeoCommands[F, K, V]
    with async.HashCommands[F, K, V]
    with async.HLLCommands[F, K, V]
    with async.KeyCommands[F, K, V]
    with async.ListCommands[F, K, V]
    with async.ScriptingCommands[F, K, V]
    with async.EvalScriptingCommands[F, K, V]
    with async.ServerCommands[F, K, V]
    with async.SetCommands[F, K, V]
    with async.SortedSetCommands[F, K, V]
    with async.StreamCommands[F, K, V]
    with async.StringCommands[F, K, V]
    with async.TransactionCommands[F, K, V] {
  implicit protected val _async: Async[F] = F
  protected val dispatchHelper: ManualDispatchHelper[K, V] = new ManualDispatchHelper(codec)
}
