package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.streams.api._
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec

object ReadMeSample4 extends IOApp.Simple {
  def run: IO[Unit] = {
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      sub <- client.connectPubSub.stream(StringCodec.UTF8)
      pub <- client.connect(StringCodec.UTF8).map(_.async())
      _ <- sub.subscribe("A").evalMap(m => pub.publish("B", m.message)).compile.drain.background
      _ <- sub.subscribe("B").evalMap(m => pub.publish("C", m.message)).compile.drain.background
      _ <- sub.subscribe("A", "B", "C").debug().take(30).compile.drain.uncancelable.background
    } yield for {
      _ <- sub.awaitSubscribed("A", "B", "C")
      _ <- List.range(0, 10).map(i => pub.publish("A", i.toHexString)).sequence
    } yield ()
  }.use(identity)
}
