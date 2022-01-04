package dev.naoh.lettucef.core.samples

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.core.LettuceF
import fs2._
import io.lettuce.core.ScanArgs
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec

object ReadMeSample3 extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    for {
      client <- LettuceF.resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      conn <- client.connect(StringCodec.UTF8)
    } yield for {
      _ <- conn.async().del("Set")
      _ <-
        List
          .range(0, 100).map(_.toHexString).grouped(10)
          .map(args => conn.async().sadd("Set", args: _*))
          .toList.sequence
    } yield {
      conn.stream().sscan("Set", ScanArgs.Builder.limit(20)).chunks.map(_.size).debug()
    }
  }.use(Stream.force(_).compile.drain.as(ExitCode.Success))
}
