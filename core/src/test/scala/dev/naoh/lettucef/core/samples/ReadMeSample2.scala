package dev.naoh.lettucef.core.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.core.LettuceF
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec
import scala.concurrent.duration.DurationInt

object ReadMeSample2 extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      client <- LettuceF.resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      cmd <- client.connect(StringCodec.UTF8).map(_.async())
      pubsub <- client.connectPubSub(StringCodec.UTF8)
      pushed <- pubsub.pushedAwait()
      _ <- pushed.debug().compile.drain.background
    } yield for {
      _ <- pubsub.subscribe("Topic")
      _ <- IO.sleep(100.milli)
      _ <- List.range(0, 10).map(i => cmd.publish("Topic", i.toString)).sequence
      _ <- IO.sleep(100.milli)
      _ <- pubsub.unsubscribe("Topic")
      _ <- IO.sleep(100.milli)
    } yield ()
  }.use(identity)
}
