package io.lettucef.core.commands

import cats.effect.IO
import fs2._
import io.lettucef.core.models.pubsub.PushedMessage
import io.lettucef.core.models.pubsub.PushedMessage._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.concurrent.duration.DurationInt

class PubSubSpec extends AnyFreeSpec with Matchers {

  import RedisTest.syntax._

  "pub sub" in RedisTest.runWith { (asyncR, pubsubR) =>
    val key = "aa".asKey
    for {
      client <- asyncR
      subscribeCon <- pubsubR
      subscribed <- subscribeCon.startListen()
    } yield for {
      _ <- subscribeCon.subscribe(key)
      numsub <- client.pubsubNumsub(key)
      ret <-
        subscribed.takeWhile(!_.isInstanceOf[Unsubscribed[_]], takeFailure = true)
          .timeout(5.seconds).compile.toList.start
      _ <-
        Stream
          .range(0, 10)
          .evalMap(s => client.publish(key, s.toString.asValue))
          .compile.drain
      _ <- subscribeCon.unsubscribe(key)
      ret <- ret.joinWithNever
    } yield {
      numsub shouldBe Map(key -> 1)
      val messages = (0 until 10).map(i => Message(key, i.toString.asValue)).toList
      ret shouldBe Subscribed(key, 1) +: messages :+ Unsubscribed(key, 0)
    }
  }

}
