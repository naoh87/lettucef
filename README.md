# LettuceF

Scala Redis functional client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3

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
  - [ ] Read all commands document one by one
- [x] Add useful datatype for Scala
- [ ] Convert Flux I/F with fs2.Stream
  - [ ] Sub