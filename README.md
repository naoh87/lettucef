# LettuceF

Scala FP Redis client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3

# Motivation

[Lettuce](https://github.com/lettuce-io/lettuce-core) is incredible performance Java Redis client, but some api is not
compatible with scala mind.

This library hide the matters when you use Lettuce.

# Getting Started

## Core

Add to build.sbt

```scala
libraryDependencies += "dev.naoh" %% "lettucef-core" % "0.1.3"
```

### Basic usage

Redis connection and commands are thread safe.

```scala
import dev.naoh.lettucef.api.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    sync <- client.connect(StringCodec.UTF8).map(_.sync())
  } yield for {
    _ <- sync.set("key", "value")
    v <- sync.get("key")
  } yield println(v) // Some(value)
}.use(identity)
```

### Lua

```scala
def run: IO[Unit] = {
    for {
      client <- LettuceF.client[IO](RedisClient.create())
      cmd <- client.connect(StringCodec.UTF8, RedisURI.create("redis://127.0.0.1")).map(_.sync())
    } yield for {
      _ <- cmd.eval("""return 1;""", Nil, Nil).flatTap(IO.println)
      // RedisInteger(1)
      _ <- cmd.eval("""return redis.call('set',KEYS[1],ARGV[1])""", "foo" :: Nil, "bar" :: Nil).flatTap(IO.println)
      // RedisBulk(OK)
      _ <- cmd.eval("""return {1, {err="ERR"}, "B"};""", Nil, Nil).flatTap(IO.println)
      // RedisArray(RedisInteger(1), RedisError(ERR), RedisBulk(B))
    } yield ()
}.use(identity)
```

### Pipelining

```scala
import dev.naoh.lettucef.api.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    conn <- client.connect(StringCodec.UTF8)
    conn2 <- client.connect(StringCodec.UTF8)
    sync = conn.sync()
    async = conn.async()
  } yield for {
    start <- IO(System.currentTimeMillis())
    elapsed = (any: Any) =>
      IO.println("%4d ms > %s".format(System.currentTimeMillis() - start, any))
    _ <- async.set("Ix", "0")
    _ <- async.incr("Ix").replicateA_(100000)
    aget <- async.get("Ix")
    _ <- async.incr("Ix")
    _ <- conn2.sync().get("Ix").flatTap(elapsed)
    //  679 ms > Some(6426)   Executions run out of order between different connections
    _ <- sync.get("Ix").flatTap(elapsed)
    // 3498 ms > Some(100001) Executions run in order on the same connection
    _ <- aget.flatTap(elapsed)
    // 3499 ms > Some(100000)
  } yield ()
}.use(identity)
```

### PubSub

This api is also just wrap lettuce api. It's bothering to control right consistency.

I **recommend** to use stream extension if you want to just receive messages from subscribe / psubscribe.

```scala
import dev.naoh.lettucef.api.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    pub <- client.connect(StringCodec.UTF8).map(_.sync())
    sub <- client.connectPubSub(StringCodec.UTF8)
    _ <- sub.setListener(RedisPubSubF.makeListener(println))
  } yield for {
    _ <- sub.subscribe("Topic")
    _ <- IO.sleep(100.milli)
    _ <- List.range(0, 3).map(i => pub.publish("Topic", i.toString)).sequence
    _ <- IO.sleep(100.milli)
    _ <- sub.unsubscribe("Topic")
    _ <- IO.sleep(100.milli)
  } yield ()
}.use(identity)
// Subscribed(Topic,1)
// Message(Topic,0)
// Message(Topic,1)
// Message(Topic,2)
// Unsubscribed(Topic,0)
```

## Stream Extension

```scala
libraryDependencies += "dev.naoh" %% "lettucef-streams" % version
```

### PubSub

```scala
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.streams._

def run: IO[Unit] = {
  val N = 3
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    pub <- client.connect(StringCodec.UTF8).map(_.sync())
    sub <- client.connectPubSub(StringCodec.UTF8).stream()
    _ <- sub.setListener(RedisPubSubF.makeListener(printSubscription))
    _ <- sub.subscribe("A").evalMap(m => pub.publish("B", m.message)).compile.drain.background
    _ <- sub.subscribe("B").evalMap(m => pub.publish("C", m.message)).compile.drain.background
    _ <- sub.subscribeAwait("A", "B", "C").flatMap(_.debug().take(N * 3).compile.drain.uncancelable.background)
  } yield for {
    _ <- List.range(0, N).map(i => pub.publish("A", i.toHexString)).sequence
  } yield ()
}.use(identity)

val printSubscription: PushedMessage[String, String] => Unit = {
  case m: Subscribed[_] => println(s"> $m")
  case m: Unsubscribed[_] => println(s"> $m")
  case _ => ()
}
// > Subscribed(A,1)
// > Subscribed(C,2)
// > Subscribed(B,3)
// Message(A,0)
// Message(A,1)
// Message(A,2)
// Message(B,0)
// Message(C,0)
// Message(B,1)
// Message(B,2)
// Message(C,1)
// Message(C,2)
// > Unsubscribed(C,2)
// > Unsubscribed(B,1)
// > Unsubscribed(A,0)
```

### Scan

```scala
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.streams._

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    conn <- client.connect(StringCodec.UTF8)
  } yield for {
    _ <- conn.sync().del("Set")
    _ <- List.range(0, 100).map(_.toHexString).grouped(10)
      .map(args => conn.sync().sadd("Set", args: _*))
      .toList.sequence
    ret <- conn.stream()
      .sscan("Set", ScanArgs.Builder.limit(20))
      .chunks.map(_.size)
      .compile.toList
  } yield println(ret)
  // List(23, 23, 20, 21, 13)
}.use(identity)
```

## Extra Extension

```scala
libraryDependencies += "dev.naoh" %% "lettucef-extras" % version 
```

### Connection Pool

It is useful to execute blocking command

https://lettuce.io/core/release/reference/#_connection_pooling

```scala
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.extras.ResourcePool

val count = new AtomicInteger(0)
val printResource: Resource[IO, Int] =
  Resource.make(IO(count.getAndIncrement()).flatTap(n => IO.println(s"$n >")))(n => IO.println(s"$n <"))
def run: IO[Unit] = {
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
// start          # active=0 idle=1
// 1 >
// 2 >
// 3 >
// Some((hoge,0)) # active=4 idle=0
// Some((hoge,2)) # active=3 idle=1
// Some((hoge,1)) # active=2 idle=2
// 1 <
// None           # active=1 idle=2
// 3 <            
// end            # active=0 idle=2
// 0 <
// 2 <
```

# Features

- [x] Support Scala 2.13 and 3.x
- [x] Convert RedisFuture I/F with Async of cats-effect 3
- [x] Convert Java collection types to scala collection
- [x] New type-safe Lua scripting I/F
- [x] Eliminate java.lang.Object I/F
- [x] Eliminate null
- [x] Add alternative models for Scala
- [x] Add PubSub I/F
- [x] Support cluster/non-cluster RedisClient
- [x] Support All Commands
    - [x] Bitmaps
    - [x] Cluster
    - [x] Connection
    - [x] Geo
    - [x] Hashed
    - [x] HyperLogLog
    - [x] Keys
    - [x] Lists
    - [x] Pub/Sub
    - [x] Scripting
    - [x] Sentinel
    - [x] Server
    - [x] Sets
    - [x] SortedSets
    - [x] Streams
    - [x] Strings
    - [x] Transactions

# Benchmark

Simple benchmark on laptop

```
Benchmark               Mode  Cnt  Score   Error  Units
LettuceF.parallel20k      ss   10  1.959 ± 0.135   s/op
LettuceF.pipeline50k      ss   10  2.546 ± 0.157   s/op
Redis4Cats.parallel20k    ss   10  2.081 ± 0.525   s/op
Redis4Cats.pipeline50k    Fail to complete run
ScalaRedis.parallel20k    ss   10  2.671 ± 0.404   s/op
ScalaRedis.pipeline50k    ss   10  5.860 ± 0.725   s/op
```
