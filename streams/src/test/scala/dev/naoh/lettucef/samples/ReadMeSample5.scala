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
      start <- IO(System.currentTimeMillis())
      elapsed = (any: Any) =>
        IO.println("%4d ms > %s".format(System.currentTimeMillis() - start, any))
      _ <- async.set("Ix", "0")
      _ <- async.incr("Ix").replicateA_(100000)
      aget <- async.get("Ix")
      _ <- async.incr("Ix")
      _ <- conn2.sync().get("Ix").flatTap(elapsed)
      //  679 ms > Some(6426)   Executions run out of order between different connections
      _ <- sync.get("Ix").flatTap(elapsed)
      // 3498 ms > Some(100001) Executions run in order on the same connection
      _ <- aget.flatTap(elapsed)
      // 3499 ms > Some(100000)
    } yield ()
  }.use(identity)
}
