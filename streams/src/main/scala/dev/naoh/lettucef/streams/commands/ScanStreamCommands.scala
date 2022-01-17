package dev.naoh.lettucef.streams.commands

import dev.naoh.lettucef.api.commands._
import dev.naoh.lettucef.api.models.RedisScanCursor
import fs2._
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor

trait ScanStreamCommands[F[_], K, V] {
  protected val underlying: ScanStreamCommands.Underlying[F, K, V]

  def scan(): Stream[F, K] =
    ScanStreamCommands.makeScanStream(underlying.scan(), underlying.scan)

  def scan(scanArgs: ScanArgs): Stream[F, K] =
    ScanStreamCommands.makeScanStream(underlying.scan(scanArgs), underlying.scan(_, scanArgs))

  def sscan(key: K): Stream[F, V] =
    ScanStreamCommands.makeScanStream(underlying.sscan(key), underlying.sscan(key, _))

  def sscan(key: K, scanArgs: ScanArgs): Stream[F, V] =
    ScanStreamCommands.makeScanStream(underlying.sscan(key, scanArgs), underlying.sscan(key, _, scanArgs))

  def hscan(key: K): Stream[F, (K, V)] =
    ScanStreamCommands.makeScanStream(underlying.hscan(key), underlying.hscan(key, _))

  def hscan(key: K, scanArgs: ScanArgs): Stream[F, (K, V)] =
    ScanStreamCommands.makeScanStream(underlying.hscan(key, scanArgs), underlying.hscan(key, _, scanArgs))

  def zscan(key: K): Stream[F, (Double, V)] =
    ScanStreamCommands.makeScanStream(underlying.zscan(key), underlying.zscan(key, _))

  def zscan(key: K, scanArgs: ScanArgs): Stream[F, (Double, V)] =
    ScanStreamCommands.makeScanStream(underlying.zscan(key, scanArgs), underlying.zscan(key, _, scanArgs))
}

object ScanStreamCommands {
  type Underlying[F[_], K, V] = KeyCommandsF[F, K, V] with SetCommandsF[F, K, V] with HashCommandsF[F, K, V] with SortedSetCommandsF[F, K, V]

  def makeScanStream[F[_], A](init: F[RedisScanCursor[A]], next: ScanCursor => F[RedisScanCursor[A]]): Stream[F, A] = {
    def go(cursor: RedisScanCursor[A]): Pull[F, A, Unit] =
      if (cursor.isFinished) {
        Pull.output(Chunk.vector(cursor.elements))
      } else {
        (Pull.output(Chunk.vector(cursor.elements)) >> Pull.eval(next(cursor))).flatMap(go)
      }

    Pull.eval(init).flatMap(go).stream
  }
}
