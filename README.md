# LettuceF

Scala Redis functional client wrapper for [Lettuce](https://github.com/lettuce-io/lettuce-core) with cats-effect 3

# Motivation
[Lettuce](https://github.com/lettuce-io/lettuce-core) is incredible performance Java redis client.

But some api is compatible with scala mind.

This library hide the troublesome matters to use Lettuce.

# Object
- [ ] Eliminate null
- [ ] Eliminate java.lang.Object interface
- [x] New type-safe Lua scripting interface
- [ ] Convert Java collection types
- [ ] Hide RedisFuture with Async of cats-effect 3
- [ ] Hide Flux with fs2.Stream
- [ ] Control resource lifecycle
