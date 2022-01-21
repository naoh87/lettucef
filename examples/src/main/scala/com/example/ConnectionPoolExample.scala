package com.example

import java.util.concurrent.atomic.AtomicInteger
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.functor._
import cats.syntax.parallel._
import cats.syntax.traverse._
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.extras.ResourcePool
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.codec.StringCodec
import scala.util.chaining.scalaUtilChainingOps

object ConnectionPoolExample extends IOApp.Simple {
  val count = new AtomicInteger(0)
  val printResource: Resource[IO, Int] =
    Resource.make(IO(count.getAndIncrement()).flatTap(n => IO.println(s"$n >")))(n => IO.println(s"$n <"))

  override def run: IO[Unit] = {
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      pub <- client.connect(StringCodec.UTF8).map(_.sync())
      pool <- client.connect(StringCodec.UTF8).map(_.sync())
        .flatMap(printResource.as)
        .pipe(ResourcePool(maxIdle = 2, minIdle = 1).make(_))
    } yield for {
      _ <- pub.unlink("hoge")
      _ <- List.range(0, 3).map(i => pub.rpush("hoge", i.toString)).sequence
      _ <- IO.println("start")
      _ <- pool.use(_.blpop(1, "hoge").flatTap(IO.println)).parReplicateA(4)
      _ <- IO.println("end")
    } yield ()
  }.use(identity)
  // 0 >
  // start
  // 1 >
  // 2 >
  // 3 >
  // Some((hoge,0))
  // Some((hoge,2))
  // Some((hoge,1))
  // 1 <
  // None
  // 3 <
  // end
  // 0 <
  // 2 <
}
