package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.implicits.toTraverseOps
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.streams._
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec
import scala.concurrent.duration.DurationLong

object ReadMeSample4 extends IOApp.Simple {
  def run: IO[Unit] = {
    val N = 3
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      sub <- client.connectPubSub.stream(StringCodec.UTF8)
      pub <- client.connect(StringCodec.UTF8).map(_.sync())
      _ <- sub.subscribe("A").evalMap(m => pub.publish("B", m.message)).compile.drain.background
      _ <- sub.subscribe("B").evalMap(m => pub.publish("C", m.message)).compile.drain.background
      _ <- sub.subscribe("A", "B", "C").debug().take(N * 3).compile.drain.uncancelable.background
      // Message(A,0)
      // Message(A,1)
      // Message(B,0)
      // Message(A,2)
      // Message(B,1)
      // Message(C,0)
      // Message(B,2)
      // Message(C,1)
      // Message(C,2)
    } yield for {
      _ <- sub.awaitSubscribed("A", "B", "C")
      _ <- List.range(0, N).map(i => pub.publish("A", i.toHexString)).sequence
    } yield ()
  }.use(identity)
}
