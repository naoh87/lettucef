# LettuceF

Scala Redis FP client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3

# Getting Started

## Core

Add to build.sbt

```scala
libraryDependencies += "dev.naoh" %% "lettucef-core" % "0.0.12"
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
  } yield
    println(v) // Some(value)
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
    start <- IO(System.nanoTime())
    elapsed = (any: Any) => 
      IO((System.nanoTime() - start).nanos).flatTap(dt => IO.println("%4d ms > %s".format(dt.toMillis, any)))
    _ <- async.set("Ix", "0")
    _ <- async.incr("Ix").replicateA_(100000)
    _ <- conn2.sync().get("Ix").flatTap(elapsed)
    //  679 ms > Some(6426)   Commands are executed out of order between different connections
    aget <- async.get("Ix")
    _ <- sync.get("Ix").flatTap(elapsed)
    // 3498 ms > Some(100000) Commands are executed in order on the same connection
    _ <- aget.flatTap(elapsed)
    // 3499 ms > Some(100000)
  } yield ()
}.use(identity)
```

### PubSub

This api is also just wrap lettuce api. It's bothering to control right consistency.

I **recommend** to use stream extension if you want to just receive messages with subscribe or psubscribe.
```scala
import dev.naoh.lettucef.api.LettuceF

def run: IO[Unit] = {
  for {
    d <- Dispatcher[IO]
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    pub <- client.connect(StringCodec.UTF8).map(_.sync())
    sub <- client.connectPubSub(StringCodec.UTF8)
    _ <- sub.setListener(RedisPubSubF.makeListener(IO.println, d))
    // Subscribed(Topic,1)
    // Message(Topic,0)
    // Message(Topic,1)
    // Message(Topic,2)
    // Unsubscribed(Topic,0)
  } yield for {
    _ <- sub.subscribe("Topic")
    _ <- IO.sleep(100.milli)
    _ <- List.range(0, 3).map(i => pub.publish("Topic", i.toString)).sequence
    _ <- IO.sleep(100.milli)
    _ <- sub.unsubscribe("Topic")
    _ <- IO.sleep(100.milli)
  } yield ()
}.use(identity)
```

## Stream Extension

```scala
libraryDependencies += "dev.naoh" %% "lettucef-streams" % "0.0.12"
```

### PubSub

```scala
import dev.naoh.lettucef.api.LettuceF
import dev.naoh.lettucef.api.streams._

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
  } yield {
    conn.stream()
      .sscan("Set", ScanArgs.Builder.limit(20))
      .chunks
      .map(_.size)
      .debug()
  }
}.use(Stream.force(_).compile.drain)
```
# Motivation

[Lettuce](https://github.com/lettuce-io/lettuce-core) is incredible performance Java Redis client, but some api is not
compatible with scala mind.

This library hide the matters when you use Lettuce.

# Missions

- [x] Support scala 2.13.x and 3.xx
- [x] Convert RedisFuture I/F with Async of cats-effect 3
- [x] Convert Java collection types to scala collection
- [x] New type-safe Lua scripting I/F
- [x] Eliminate java.lang.Object I/F
- [x] Eliminate null
- [x] Add useful datatype for Scala
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
    - [ ] Transactions
