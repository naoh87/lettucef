package dev.naoh.lettucef.core.commands

import cats.effect.IO
import cats.effect.Resource
import dev.naoh.lettucef.core.RedisPubSubF
import dev.naoh.lettucef.core.RedisPubSubF
import dev.naoh.lettucef.core.models.pubsub.PushedMessage.Unsubscribed
import fs2._
import fs2.concurrent.SignallingRef
import dev.naoh.lettucef.core.models.pubsub.PushedMessage._
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
      state <- subscribingState(subscribeCon)
    } yield for {
      _ <- subscribeCon.subscribe(key)
      _ <- state.takeWhile(!_.contains(key)).compile.drain
      numsub <- client.pubsubChannels()
      ret <-
        subscribed.takeWhile(!_.isInstanceOf[Unsubscribed[_]], takeFailure = true)
          .timeout(5.seconds).compile.toList.start
      _ <-
        Stream
          .range(0, 10)
          .evalMap(s => client.publish(key, s.toString.asValue))
          .compile.drain
      _ <- IO.sleep(500.milli) >> subscribeCon.unsubscribe(key)
      _ <- state.takeWhile(_.contains(key)).compile.drain
      ret <- ret.joinWithNever
    } yield {
      numsub shouldBe List(key)
      val messages = (0 until 10).map(i => Message(key, i.toString.asValue)).toList
      ret shouldBe Subscribed(key, 1) +: messages :+ Unsubscribed(key, 0)
    }
  }

  def subscribingState[K, V](pubsub: RedisPubSubF[IO, K, V]): Resource[IO, Stream[IO, Set[K]]] =
    for {
      s <- pubsub.startListen()
      ref <- Resource.eval(SignallingRef[IO, Set[K]](Set.empty))
      _ <- s.evalMap {
        case Subscribed(channel, _) => ref.update(_ + channel)
        case Unsubscribed(channel, _) => ref.update(_ - channel)
        case _ => IO.unit
      }.compile.drain.background
    } yield ref.discrete

}
