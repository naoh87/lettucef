package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import dev.naoh.lettucef.api.LettuceF
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec
import scala.concurrent.duration._

object ReadMeSample5 extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      conn <- client.connect(StringCodec.UTF8)
      sync = conn.sync()
      async = conn.async()
    } yield for {
      start <- IO(System.nanoTime())
      _ <- sync.set("Ix", "0")
      _ <- async.incr("Ix").replicateA_(100000)
      aget <- async.get("Ix")
      sget <- sync.get("Ix")
      aget <- aget
      end <- IO(System.nanoTime())
    } yield
      println((sget, aget, (end - start).nanos.toMillis)) //(Some(100000),Some(100000),3338)
  }.use(identity)
}
