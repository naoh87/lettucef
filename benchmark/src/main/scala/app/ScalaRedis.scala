package app

import cats.effect.IO
import cats.effect.std.Semaphore
import cats.effect.unsafe.IORuntime
import com.redis.RedisClient
import com.redis.RedisClientPool
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
class ScalaRedis {

  import ScalaRedis.runtime

  @Benchmark
  def parallel20k(): Unit =
    ScalaRedis.setget.unsafeRunSync()


  @Benchmark
  def pipeline50k(): Unit =
    ScalaRedis.pipeline.unsafeRunSync()

}

object ScalaRedis {
  implicit def runtime: IORuntime = IORuntime.global

  val pool = new RedisClientPool("localhost", 6379)
  val client = new RedisClient("localhost", 6379, batch = RedisClient.BATCH)

  val setget: IO[Unit] =
    BenchTask.parallelTask(IO.blocking(pool.withClient(r => {
      r.set("X", "0")
      r.get("X")
    })), 200)

  val pipeLineJob = List(
    () => client.set("X", "0"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.incr("X"),
    () => client.get[String]("X")
  )

  val pipeline: IO[Unit] =
    Semaphore[IO](1).flatMap { s =>
      BenchTask.parallelTask(s.permit.use { _ =>
        IO.blocking(ScalaRedis.client.batchedPipeline(pipeLineJob))
      }.flatMap(x => IO(assert(x.get.last == Some("8")))), 100) // 10 * 100 * 50 = 50000
    }

  def shutdown(): Unit = {
    client.close()
    pool.close()
  }
}