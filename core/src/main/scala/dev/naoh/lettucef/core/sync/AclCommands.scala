// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.sync

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.AclCategory
import io.lettuce.core.AclSetuserArgs
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait AclCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisAclAsyncCommands[K, V]
  
  def aclCat(): F[Set[AclCategory]] =
    JF.toSync(underlying.aclCat()).map(_.asScala.toSet)
  
  def aclCat(category: AclCategory): F[Set[CommandType]] =
    JF.toSync(underlying.aclCat(category)).map(_.asScala.toSet)
  
  def aclDeluser(usernames: String*): F[Long] =
    JF.toSync(underlying.aclDeluser(usernames: _*)).map(Long2long)
  
  def aclGenpass(): F[String] =
    JF.toSync(underlying.aclGenpass())
  
  def aclGenpass(bits: Int): F[String] =
    JF.toSync(underlying.aclGenpass(bits))
  
  def aclGetuser(username: String): F[Seq[RedisData[V]]] =
    JF.toSync(underlying.aclGetuser(username)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def aclList(): F[Seq[String]] =
    JF.toSync(underlying.aclList()).map(_.asScala.toSeq)
  
  def aclLoad(): F[String] =
    JF.toSync(underlying.aclLoad())
  
  def aclLog(): F[Seq[Map[String, RedisData[V]]]] =
    JF.toSync(underlying.aclLog()).map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap))
  
  def aclLog(count: Int): F[Seq[Map[String, RedisData[V]]]] =
    JF.toSync(underlying.aclLog(count)).map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap))
  
  def aclLogReset(): F[String] =
    JF.toSync(underlying.aclLogReset())
  
  def aclSave(): F[String] =
    JF.toSync(underlying.aclSave())
  
  def aclSetuser(username: String, setuserArgs: AclSetuserArgs): F[String] =
    JF.toSync(underlying.aclSetuser(username, setuserArgs))
  
  def aclUsers(): F[Seq[String]] =
    JF.toSync(underlying.aclUsers()).map(_.asScala.toSeq)
  
  def aclWhoami(): F[String] =
    JF.toSync(underlying.aclWhoami())
  
}
