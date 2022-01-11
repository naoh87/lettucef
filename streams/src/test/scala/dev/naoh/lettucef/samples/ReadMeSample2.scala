package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Dispatcher
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.core.RedisPubSubF
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec
import scala.concurrent.duration.DurationInt

object ReadMeSample2 extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      d <- Dispatcher[IO]
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      pub <- client.connect(StringCodec.UTF8).map(_.sync())
      sub <- client.connectPubSub(StringCodec.UTF8)
      _ <- sub.setListener(RedisPubSubF.makeListener(IO.println, d))
      // Subscribed(Topic,1)
      // Message(Topic,0)
      // Message(Topic,1)
      // Message(Topic,2)
      // Unsubscribed(Topic,0)
    } yield for {
      _ <- sub.subscribe("Topic")
      _ <- IO.sleep(100.milli)
      _ <- List.range(0, 3).map(i => pub.publish("Topic", i.toString)).sequence
      _ <- IO.sleep(100.milli)
      _ <- sub.unsubscribe("Topic")
      _ <- IO.sleep(100.milli)
    } yield ()
  }.use(identity)
}
