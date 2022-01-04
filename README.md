# LettuceF

Scala Redis functional client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3


# Getting Started
```scala
libraryDependencies += "dev.naoh" %% "lettucef-core" % "0.0.4"
```
```scala
def run(): IO[Unit] = {
  for {
    client <- RedisClientF.resource[IO](RedisClusterClient.create("redis://127.0.0.1:7000"))
    commands <- client.connect(StringCodec.UTF8).map(_.async())
  } yield for {
    _ <- commands.set("key", "value")
    _ <- commands.get("key")
  } yield ()
}.use(identity)
```


# Motivation
[Lettuce](https://github.com/lettuce-io/lettuce-core) is incredible performance Java Redis client.

But some api is not compatible with scala mind.

This library hide the troublesome matters when you use Lettuce.


# Missions
- [x] Support scala 2.13.x and 3.xx
- [x] Convert RedisFuture I/F with Async of cats-effect 3
- [x] Convert Java collection types to scala collection
- [x] New type-safe Lua scripting I/F
- [x] Eliminate java.lang.Object I/F
- [x] Eliminate null
- [x] Add useful datatype for Scala
- [x] Add PubSub Interface
