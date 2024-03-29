package dev.naoh.lettucef.core.sync

import cats.effect.IO
import dev.naoh.lettucef.api.models.RedisData
import io.lettuce.core.AclSetuserArgs
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class RedisDataCommandsSpec extends AnyFreeSpec with Matchers {


  "check" in RedisTest.commands { sync =>
    def notEmpty[R](data: List[RedisData[R]]): IO[Unit] =
      IO(data should not be empty)

    for {
      _ <- sync.role().flatMap(notEmpty)
      _ <- sync.aclSetuser("hoge", AclSetuserArgs.Builder.allCommands())
      _ <- sync.aclGetuser("hoge").flatTap(notEmpty)
      _ <- sync.aclDeluser("hoge")
//      _ <- sync.clusterSlots().flatTap(notEmpty)
      _ <- sync.commandInfo("GET", "SET", "MGET").flatTap(notEmpty)
    } yield {

    }
  }

}
