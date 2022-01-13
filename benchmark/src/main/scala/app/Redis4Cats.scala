package app

import cats.effect.IO
import cats.effect.std.Semaphore
import cats.effect.unsafe.IORuntime
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.NoOp._
import dev.profunktor.redis4cats.hlist.HCons
import dev.profunktor.redis4cats.hlist.HNil
import dev.profunktor.redis4cats.pipeline.RedisPipeline
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.annotations.Warmup

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 1)
@Fork(value = 10)
@State(Scope.Thread)
class Redis4Cats {

  import Redis4Cats.runtime

//  unable to complete task
//  @Benchmark
//  def pipeline50k(): Unit =
//    Redis4Cats.pipelineTask.unsafeRunSync()

  @Benchmark
  def parallel20k(): Unit =
    Redis4Cats.setGet.unsafeRunSync()

  @TearDown
  def down(): Unit = Redis4Cats.tearDown().unsafeRunSync()
}

object Redis4Cats {

  import dev.profunktor.redis4cats._

  implicit def runtime: IORuntime = IORuntime.global

  val (redis, finalizer) =
    BenchHelper.extractResource {
      Redis[IO].utf8("redis://127.0.0.1:6379")
    }.unsafeRunSync()

  val setGet: IO[Unit] =
    BenchTask.parallelTask(redis.set("X", "0") >> redis.get("X"), 200)

  implicit val num: Numeric[String] = null

  val incrGet = redis.set("I", "0").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.incr("I").void ::
    redis.get("I") :: HNil


  //No execution order is guaranteed
  //https://redis4cats.profunktor.dev/pipelining.html
  val pipelineTask = {
    Semaphore[IO](1).flatMap { s =>
      val incr10 = RedisPipeline(redis).filterExec(incrGet)
      BenchTask
        .parallelTask(
          s.permit
            .use(_ => incr10)
            .map {
              case HCons(opt, HNil) => opt != Some("8")
            },
          repeat = 100) //2 * 200 * 50 = 20000
    }
  }

  def tearDown(): IO[Unit] =
    finalizer
}