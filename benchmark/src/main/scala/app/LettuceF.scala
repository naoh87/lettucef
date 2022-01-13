package app

import java.util.concurrent.TimeUnit
import cats.effect.IO
import cats.effect.std.Semaphore
import cats.effect.unsafe.IORuntime
import dev.naoh.lettucef.core.RedisClientF
import dev.naoh.lettucef.core.RedisConnectionF
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.codec.StringCodec
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 1)
@Fork(value = 10)
@State(Scope.Thread)
class LettuceF {

  import LettuceF.runtime

  @Benchmark
  def parallel20k(): Unit =
    LettuceF.setget.unsafeRunSync()

  @Benchmark
  def pipeline50k(): Unit =
    LettuceF.pipeline.unsafeRunSync()
}

object LettuceF {
  implicit def runtime: IORuntime = IORuntime.global

  val clientF = new RedisClientF[IO](RedisClient.create())

  val conn: RedisConnectionF[IO, String, String] =
    clientF.connect.unsafe(StringCodec.UTF8, RedisURI.create("redis://127.0.0.1:6379")).unsafeRunSync()

  val pipeline: IO[Unit] = {
    val async = conn.async()
    Semaphore[IO](1).flatMap { s =>
      BenchTask.parallelTask(s.permit.use(_ =>
        for {
          _ <- async.set("X", "0")
          _ <- async.incr("X").replicateA_(8)
          x <- async.get("X")
        } yield x
      ).flatMap(_.map(x => assert(x.contains("8")))), 100) // 10 * 100 * 50 = 50000
    }
  }

  val setget: IO[Unit] =
    BenchTask.parallelTask(conn.sync().set("X", "0") >> conn.sync().get("X"), 200) //2 * 200 * 50 = 20000

  def tearDown(): Unit =
    clientF.shutdownAsync(0, 1, TimeUnit.SECONDS).unsafeRunSync()(IORuntime.global)
}