// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.models._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.AclCategory
import io.lettuce.core.AclSetuserArgs
import io.lettuce.core.api.async._
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait AclCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisAclAsyncCommands[K, V]
  
  def aclCat(): F[F[Set[AclCategory]]] =
    JF.toAsync(underlying.aclCat()).map(_.map(_.asScala.toSet))
  
  def aclCat(category: AclCategory): F[F[Set[CommandType]]] =
    JF.toAsync(underlying.aclCat(category)).map(_.map(_.asScala.toSet))
  
  def aclDeluser(usernames: String*): F[F[Long]] =
    JF.toAsync(underlying.aclDeluser(usernames: _*)).map(_.map(Long2long))
  
  def aclGenpass(): F[F[String]] =
    JF.toAsync(underlying.aclGenpass())
  
  def aclGenpass(bits: Int): F[F[String]] =
    JF.toAsync(underlying.aclGenpass(bits))
  
  def aclGetuser(username: String): F[F[Seq[RedisData[V]]]] =
    JF.toAsync(underlying.aclGetuser(username)).map(_.map(_.asScala.toSeq.map(RedisData.from[V])))
  
  def aclList(): F[F[Seq[String]]] =
    JF.toAsync(underlying.aclList()).map(_.map(_.asScala.toSeq))
  
  def aclLoad(): F[F[String]] =
    JF.toAsync(underlying.aclLoad())
  
  def aclLog(): F[F[Seq[Map[String, RedisData[V]]]]] =
    JF.toAsync(underlying.aclLog()).map(_.map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap)))
  
  def aclLog(count: Int): F[F[Seq[Map[String, RedisData[V]]]]] =
    JF.toAsync(underlying.aclLog(count)).map(_.map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap)))
  
  def aclLogReset(): F[F[String]] =
    JF.toAsync(underlying.aclLogReset())
  
  def aclSave(): F[F[String]] =
    JF.toAsync(underlying.aclSave())
  
  def aclSetuser(username: String, setuserArgs: AclSetuserArgs): F[F[String]] =
    JF.toAsync(underlying.aclSetuser(username, setuserArgs))
  
  def aclUsers(): F[F[Seq[String]]] =
    JF.toAsync(underlying.aclUsers()).map(_.map(_.asScala.toSeq))
  
  def aclWhoami(): F[F[String]] =
    JF.toAsync(underlying.aclWhoami())
  
}
