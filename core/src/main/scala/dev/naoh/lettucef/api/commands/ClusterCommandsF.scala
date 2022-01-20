// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.api.async._
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands
import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import scala.jdk.CollectionConverters._


trait ClusterCommandsF[F[_], K, V] {

  def auth(password: CharSequence): F[String]
  
  def auth(username: String, password: CharSequence): F[String]
  
  def clusterBumpepoch(): F[String]
  
  def clusterMeet(ip: String, port: Int): F[String]
  
  def clusterForget(nodeId: String): F[String]
  
  def clusterAddSlots(slots: Int*): F[String]
  
  def clusterDelSlots(slots: Int*): F[String]
  
  def clusterSetSlotNode(slot: Int, nodeId: String): F[String]
  
  def clusterSetSlotStable(slot: Int): F[String]
  
  def clusterSetSlotMigrating(slot: Int, nodeId: String): F[String]
  
  def clusterSetSlotImporting(slot: Int, nodeId: String): F[String]
  
  def clusterInfo(): F[String]
  
  def clusterMyId(): F[String]
  
  def clusterNodes(): F[String]
  
  def clusterSlaves(nodeId: String): F[Seq[String]]
  
  def clusterGetKeysInSlot(slot: Int, count: Int): F[Seq[K]]
  
  def clusterCountKeysInSlot(slot: Int): F[Long]
  
  def clusterCountFailureReports(nodeId: String): F[Long]
  
  def clusterKeyslot(key: K): F[Long]
  
  def clusterSaveconfig(): F[String]
  
  def clusterSetConfigEpoch(configEpoch: Long): F[String]
  
  def clusterSlots(): F[List[RedisData[V]]]
  
  def asking(): F[String]
  
  def clusterReplicate(nodeId: String): F[String]
  
  def clusterFailover(force: Boolean): F[String]
  
  def clusterReset(hard: Boolean): F[String]
  
  def clusterFlushslots(): F[String]
  
}
