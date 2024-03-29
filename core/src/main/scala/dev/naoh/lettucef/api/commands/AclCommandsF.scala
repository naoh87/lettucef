// Code generated by codegen/run; DO NOT EDIT
package dev.naoh.lettucef.api.commands

import dev.naoh.lettucef.api.models._
import io.lettuce.core.AclCategory
import io.lettuce.core.AclSetuserArgs
import io.lettuce.core.protocol.CommandType


trait AclCommandsF[F[_], K, V] {

  def aclCat(): F[Set[AclCategory]]
  
  def aclCat(category: AclCategory): F[Set[CommandType]]
  
  def aclDeluser(usernames: String*): F[Long]
  
  def aclDryRun(username: String, command: String, args: String*): F[String]
  
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
