package dev.naoh.lettucef.samples

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import dev.naoh.lettucef.api.LettuceF
import cats.syntax.apply._
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.StringCodec

object ReadMeSample6 extends IOApp.Simple {
  override def run: IO[Unit] = {
    for {
      client <- LettuceF.client[IO](RedisClient.create())
      cmd <- client.connect(StringCodec.UTF8, RedisURI.create("redis://127.0.0.1"))
      _ <- Resource.eval(cmd.sync().set("G", "0") >> cmd.sync().set("F", "0"))
    } yield for {
      _ <- cmd.async().watch("F")
      g <- cmd.async().incr("G")
      _ <- cmd.async().multi()
      a <- cmd.async().getset("F", "0")
      b <- cmd.async().incr("F")
      c <- cmd.async().incr("F")
      exec <- cmd.async().exec()
    } yield exec.flatMap {
      case true => (g, a, b, c).tupled.map(Some(_))
      case false => IO(None)
    }
  }.use(_.flatten.flatMap(IO.println).replicateA_(3))
  // Some((1,Some(0),1,2))
  // Some((2,Some(2),1,2))
  // Some((3,Some(2),1,2))
}
