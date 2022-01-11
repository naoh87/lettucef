package dev.naoh.lettucef.core.sync

import cats.effect.IO
import cats.effect.Resource
import cats.effect.std.Dispatcher
import cats.effect.std.Queue
import cats.syntax.traverse._
import dev.naoh.lettucef.core.RedisPubSubF
import dev.naoh.lettucef.core.models.pubsub.PushedMessage
import dev.naoh.lettucef.core.models.pubsub.PushedMessage.Unsubscribed
import dev.naoh.lettucef.core.models.pubsub.PushedMessage._
import dev.naoh.lettucef.core.sync.RedisTest.RedisKey
import dev.naoh.lettucef.core.sync.RedisTest.RedisValue
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class PubSubSpec extends AnyFreeSpec with Matchers {

  import RedisTest.syntax._

  "pub sub" in RedisTest.runWith { (asyncR, pubsubR) =>
    val key = "aa".asKey
    for {
      pub <- asyncR
      sub <- pubsubR
      d <- Dispatcher[IO]
      q <- Resource.eval(Queue.unbounded[IO, PushedMessage[RedisKey, RedisValue]])
      _ <- sub.setListener(RedisPubSubF.makeListener(q.offer, d))
    } yield for {
      _ <- sub.subscribe(key)
      _ <- q.take.map(_ shouldBe Subscribed(key, 1))
      numsub <- pub.pubsubChannels()
      _ <- List.range(0, 10).map(i => pub.publish(key, i.toHexString.asValue)).sequence
      ret <- List.fill(10)(q.take).sequence
      _ <- sub.unsubscribe(key)
      _ <- q.take.map(_ shouldBe Unsubscribed(key, 0))
    } yield {
      numsub shouldBe List(key)
      ret shouldBe List.range(0, 10).map(i => Message(key, i.toHexString.asValue))
    }
  }

}
