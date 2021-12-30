// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import io.lettuce.core.AclCategory
import io.lettuce.core.AclSetuserArgs
import io.lettuce.core.protocol.CommandType
import io.lettucef.core.models._
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait AclCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisAclAsyncCommands[K, V]
  
  def aclCat(): F[Set[AclCategory]] =
    JF.toAsync(underlying.aclCat()).map(_.asScala.toSet)
  
  def aclCat(category: AclCategory): F[Set[CommandType]] =
    JF.toAsync(underlying.aclCat(category)).map(_.asScala.toSet)
  
  def aclDeluser(usernames: String*): F[Long] =
    JF.toAsync(underlying.aclDeluser(usernames: _*)).map(Long2long)
  
  def aclGenpass(): F[String] =
    JF.toAsync(underlying.aclGenpass())
  
  def aclGenpass(bits: Int): F[String] =
    JF.toAsync(underlying.aclGenpass(bits))
  
  def aclGetuser(username: String): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.aclGetuser(username)).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def aclList(): F[Seq[String]] =
    JF.toAsync(underlying.aclList()).map(_.asScala.toSeq)
  
  def aclLoad(): F[String] =
    JF.toAsync(underlying.aclLoad())
  
  def aclLog(): F[Seq[Map[String, RedisData[V]]]] =
    JF.toAsync(underlying.aclLog()).map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap))
  
  def aclLog(count: Int): F[Seq[Map[String, RedisData[V]]]] =
    JF.toAsync(underlying.aclLog(count)).map(_.asScala.toSeq.map(_.asScala.view.mapValues(RedisData.from[V]).toMap))
  
  def aclLogReset(): F[String] =
    JF.toAsync(underlying.aclLogReset())
  
  def aclSave(): F[String] =
    JF.toAsync(underlying.aclSave())
  
  def aclSetuser(username: String, setuserArgs: AclSetuserArgs): F[String] =
    JF.toAsync(underlying.aclSetuser(username, setuserArgs))
  
  def aclUsers(): F[Seq[String]] =
    JF.toAsync(underlying.aclUsers()).map(_.asScala.toSeq)
  
  def aclWhoami(): F[String] =
    JF.toAsync(underlying.aclWhoami())
  
}

