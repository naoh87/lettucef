package com.example

import java.util.concurrent.atomic.AtomicInteger
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.functor._
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
      pub <- client.connect(StringCodec.UTF8).flatMap(printResource.as).map(_.sync())
      pool <- client.connect(StringCodec.UTF8).map(_.sync())
        .flatMap(printResource.as)
        .pipe(ResourcePool(maxIdle = 2, minIdle = 1).make(_))
    } yield for {
      _ <- pub.unlink("hoge")
      _ <- List.range(0, 3).map(i => pub.rpush("hoge", i.toString)).sequence
      _ <- IO.println("rpushed")
      a <- List.fill(4)(pool.use(_.blpop(1, "hoge")).start).sequence
      _ <- a.map(_.joinWithNever).sequence.flatTap(IO.println)
    } yield ()
  }.use(identity)
  // 0 >
  // 1 >
  // rpushed
  // 2 >
  // 3 >
  // 4 >
  // 3 <
  // 2 <
  // List(Some((hoge,1)), None, Some((hoge,0)), Some((hoge,2)))
  // 1 <
  // 4 <
  // 0 <
}
