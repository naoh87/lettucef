package com.example

import cats.effect.IO
import cats.effect.IOApp
import dev.naoh.lettucef.api.LettuceF
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.StringCodec

object ScriptExample extends IOApp.Simple {
  override def run: IO[Unit] = {
    for {
      client <- LettuceF.client[IO](RedisClient.create())
      cmd <- client.connect(StringCodec.UTF8, RedisURI.create("redis://127.0.0.1")).map(_.sync())
    } yield for {
      _ <- cmd.eval("""return true;""", Nil, Nil).flatTap(IO.println)
      // RedisInteger(1)
      _ <- cmd.eval("""return false;""", Nil, Nil).flatTap(IO.println)
      // RedisNull
      _ <- cmd.eval("""return redis.call('set',KEYS[1],ARGV[1])""", "foo" :: Nil, "bar" :: Nil).flatTap(IO.println)
      // RedisBulk(OK)
      _ <- cmd.eval("""return 1;""", Nil, Nil).flatTap(IO.println)
      // RedisInteger(1)
      _ <- cmd.eval("""return -2;""", Nil, Nil).flatTap(IO.println)
      // RedisInteger(-2)
      _ <- cmd.eval("""return 3.14;""", Nil, Nil).flatTap(IO.println)
      // RedisInteger(3)
      _ <- cmd.eval("""return "string";""", Nil, Nil).flatTap(IO.println)
      // RedisBulk(string)
      _ <- cmd.eval("""return {ok="OK"};""", Nil, Nil).flatTap(IO.println)
      // RedisBulk(OK)
      _ <- cmd.eval("""return {1, nil, "B"};""", Nil, Nil).flatTap(IO.println)
      // RedisArray(RedisInteger(1))
      _ <- cmd.eval("""return {1, {err="ERR"}, "B"};""", Nil, Nil).flatTap(IO.println)
      // RedisArray(RedisInteger(1), RedisError(ERR), RedisBulk(B))
      _ <- cmd.eval("""return {a="fail"};""", Nil, Nil).flatTap(IO.println)
      // RedisNull
      _ <- cmd.eval("""return {err="ERR"};""", Nil, Nil).attempt.flatTap(IO.println)
      // Left(io.lettuce.core.RedisCommandExecutionException: ERR)
    } yield ()
  }.use(identity)
}
