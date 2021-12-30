// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands
import io.lettucef.core.models._
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait ClusterCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisClusterAsyncCommands[K, V]
  
  def auth(password: CharSequence): F[String] =
    JF.toAsync(underlying.auth(password))
  
  def auth(username: String, password: CharSequence): F[String] =
    JF.toAsync(underlying.auth(username, password))
  
  def clusterBumpepoch(): F[String] =
    JF.toAsync(underlying.clusterBumpepoch())
  
  def clusterMeet(ip: String, port: Int): F[String] =
    JF.toAsync(underlying.clusterMeet(ip, port))
  
  def clusterForget(nodeId: String): F[String] =
    JF.toAsync(underlying.clusterForget(nodeId))
  
  def clusterAddSlots(slots: Int*): F[String] =
    JF.toAsync(underlying.clusterAddSlots(slots: _*))
  
  def clusterDelSlots(slots: Int*): F[String] =
    JF.toAsync(underlying.clusterDelSlots(slots: _*))
  
  def clusterSetSlotNode(slot: Int, nodeId: String): F[String] =
    JF.toAsync(underlying.clusterSetSlotNode(slot, nodeId))
  
  def clusterSetSlotStable(slot: Int): F[String] =
    JF.toAsync(underlying.clusterSetSlotStable(slot))
  
  def clusterSetSlotMigrating(slot: Int, nodeId: String): F[String] =
    JF.toAsync(underlying.clusterSetSlotMigrating(slot, nodeId))
  
  def clusterSetSlotImporting(slot: Int, nodeId: String): F[String] =
    JF.toAsync(underlying.clusterSetSlotImporting(slot, nodeId))
  
  def clusterInfo(): F[String] =
    JF.toAsync(underlying.clusterInfo())
  
  def clusterMyId(): F[String] =
    JF.toAsync(underlying.clusterMyId())
  
  def clusterNodes(): F[String] =
    JF.toAsync(underlying.clusterNodes())
  
  def clusterSlaves(nodeId: String): F[Seq[String]] =
    JF.toAsync(underlying.clusterSlaves(nodeId)).map(_.asScala.toSeq)
  
  def clusterGetKeysInSlot(slot: Int, count: Int): F[Seq[K]] =
    JF.toAsync(underlying.clusterGetKeysInSlot(slot, count)).map(_.asScala.toSeq)
  
  def clusterCountKeysInSlot(slot: Int): F[Long] =
    JF.toAsync(underlying.clusterCountKeysInSlot(slot)).map(Long2long)
  
  def clusterCountFailureReports(nodeId: String): F[Long] =
    JF.toAsync(underlying.clusterCountFailureReports(nodeId)).map(Long2long)
  
  def clusterKeyslot(key: K): F[Long] =
    JF.toAsync(underlying.clusterKeyslot(key)).map(Long2long)
  
  def clusterSaveconfig(): F[String] =
    JF.toAsync(underlying.clusterSaveconfig())
  
  def clusterSetConfigEpoch(configEpoch: Long): F[String] =
    JF.toAsync(underlying.clusterSetConfigEpoch(configEpoch))
  
  def clusterSlots(): F[Seq[RedisData[V]]] =
    JF.toAsync(underlying.clusterSlots()).map(_.asScala.toSeq.map(RedisData.from[V]))
  
  def asking(): F[String] =
    JF.toAsync(underlying.asking())
  
  def clusterReplicate(nodeId: String): F[String] =
    JF.toAsync(underlying.clusterReplicate(nodeId))
  
  def clusterFailover(force: Boolean): F[String] =
    JF.toAsync(underlying.clusterFailover(force))
  
  def clusterReset(hard: Boolean): F[String] =
    JF.toAsync(underlying.clusterReset(hard))
  
  def clusterFlushslots(): F[String] =
    JF.toAsync(underlying.clusterFlushslots())
  
}

