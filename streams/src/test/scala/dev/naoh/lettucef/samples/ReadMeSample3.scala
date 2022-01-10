package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.streams.api._
import fs2._
import io.lettuce.core.ScanArgs
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec

object ReadMeSample3 extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      conn <- client.connect(StringCodec.UTF8)
    } yield for {
      _ <- conn.sync().del("Set")
      _ <-
        List
          .range(0, 100).map(_.toHexString).grouped(10)
          .map(args => conn.sync().sadd("Set", args: _*))
          .toList.sequence
    } yield {
      conn.stream()
        .sscan("Set", ScanArgs.Builder.limit(20))
        .chunks
        .map(_.size)
        .debug()
    }
  }.use(Stream.force(_).compile.drain)
}
