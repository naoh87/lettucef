package dev.naoh.lettucef.streams.commands

import dev.naoh.lettucef.core.sync.HashCommands
import dev.naoh.lettucef.core.sync.KeyCommands
import dev.naoh.lettucef.core.sync.SetCommands
import dev.naoh.lettucef.core.sync.SortedSetCommands
import dev.naoh.lettucef.api.models.DataScanCursor
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
  type Underlying[F[_], K, V] = KeyCommands[F, K, V] with SetCommands[F, K, V] with HashCommands[F, K, V] with SortedSetCommands[F, K, V]

  def makeScanStream[F[_], A](init: F[DataScanCursor[A]], next: ScanCursor => F[DataScanCursor[A]]): Stream[F, A] = {
    def go(cursor: DataScanCursor[A]): Pull[F, A, Unit] =
      if (cursor.isFinished) {
        Pull.output(Chunk.vector(cursor.elements))
      } else {
        (Pull.output(Chunk.vector(cursor.elements)) >> Pull.eval(next(cursor))).flatMap(go)
      }

    Pull.eval(init).flatMap(go).stream
  }
}
