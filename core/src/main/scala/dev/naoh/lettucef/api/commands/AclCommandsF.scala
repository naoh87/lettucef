// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.AclCategory
import io.lettuce.core.AclSetuserArgs
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait AclCommandsF[F[_], K, V] {

  def aclCat(): F[Set[AclCategory]]
  
  def aclCat(category: AclCategory): F[Set[CommandType]]
  
  def aclDeluser(usernames: String*): F[Long]
  
  def aclGenpass(): F[String]
  
  def aclGenpass(bits: Int): F[String]
  
  def aclGetuser(username: String): F[List[RedisData[V]]]
  
  def aclList(): F[Seq[String]]
  
  def aclLoad(): F[String]
  
  def aclLog(): F[List[RedisData[V]]]
  
  def aclLog(count: Int): F[List[RedisData[V]]]
  
  def aclLogReset(): F[String]
  
  def aclSave(): F[String]
  
  def aclSetuser(username: String, setuserArgs: AclSetuserArgs): F[String]
  
  def aclUsers(): F[Seq[String]]
  
  def aclWhoami(): F[String]
  
}
