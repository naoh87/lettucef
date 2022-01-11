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
      conn2 <- client.connect(StringCodec.UTF8)
      sync = conn.sync()
      async = conn.async()
    } yield for {
      start <- IO(System.nanoTime())
      _ <- async.set("Ix", "0")
      _ <- async.incr("Ix").replicateA_(100000)
      _ <- conn2.sync().get("Ix").flatTap(IO.println) // Some(6453)
      aget <- async.get("Ix")
      _ <- sync.get("Ix").flatTap(IO.println) // Some(1000000)
      _ <- aget.flatTap(IO.println) // Some(1000000)
      end <- IO(System.nanoTime())
    } yield
      println((end - start).nanos.toMillis) // 3320
  }.use(identity)
}
