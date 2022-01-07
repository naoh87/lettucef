# LettuceF

Scala Redis functional client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3

# Getting Started

Add to build.sbt

```scala
libraryDependencies += "dev.naoh" %% "lettucef-core" % "0.0.8"
```

Simple Redis command execution

```scala
import dev.naoh.lettucef.core.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    commands <- client.connect(StringCodec.UTF8).map(_.async())
  } yield for {
    _ <- commands.set("key", "value")
    v <- commands.get("key")
  } yield println(v) //Some(value)
}.use(identity)
```

PubSub

```scala
import dev.naoh.lettucef.core.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    cmd <- client.connect(StringCodec.UTF8).map(_.async())
    pubsub <- client.connectPubSub(StringCodec.UTF8)
    pushed <- pubsub.pushedAwait()
    _ <- pushed.debug().compile.drain.background
  } yield for {
    _ <- pubsub.subscribe("Topic")
    _ <- IO.sleep(100.milli)
    _ <- List.range(0, 10)
             .map(i => cmd.publish("Topic", i.toString))
             .sequence
    _ <- IO.sleep(100.milli)
    _ <- pubsub.unsubscribe("Topic")
    _ <- IO.sleep(100.milli)
  } yield ()
}.use(identity)
```

Streaming

```scala
import dev.naoh.lettucef.core.LettuceF

def run: IO[Unit] = {
  for {
    client <- LettuceF.cluster[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    conn <- client.connect(StringCodec.UTF8)
  } yield for {
    _ <- conn.async().del("Set")
    _ <- List.range(0, 100).map(_.toHexString).grouped(10)
             .map(args => conn.async().sadd("Set", args: _*))
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

[Lettuce](https://github.com/lettuce-io/lettuce-core) is incredible performance Java Redis client, but some api is not compatible with scala mind.

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
