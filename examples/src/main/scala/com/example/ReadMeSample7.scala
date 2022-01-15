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

object ReadMeSample7 extends IOApp.Simple {
  val count = new AtomicInteger(0)
  val printResource: Resource[IO, Int] =
    Resource.make(IO(count.getAndIncrement()).flatTap(n => IO.println(s"$n >")))(n => IO.println(s"$n <"))

  override def run: IO[Unit] = {
    for {
      client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
      pub <- client.connect(StringCodec.UTF8).flatMap(printResource.as)
      pool <- client.connect(StringCodec.UTF8)
        .flatMap(printResource.as)
        .pipe(ResourcePool(maxIdle = 2).make(_))
    } yield for {
      _ <- pub.sync().unlink("hoge")
      _ <- List.range(0, 3).map(i => pub.sync().rpush("hoge", i.toString)).sequence
      _ <- IO.println("rpushed")
      a <- List.fill(4)(pool.use(_.sync().blpop(1, "hoge")).start).sequence
      _ <- a.map(_.joinWithNever).sequence.flatTap(IO.println)
    } yield ()
  }.use(identity)
  // 3 >
  // 1 >
  // 2 >
  // 4 >
  // 1 <
  // 2 <
  // List(Some((hoge,2)), Some((hoge,0)), None, Some((hoge,1)))
  // 4 <
  // 3 <
}
