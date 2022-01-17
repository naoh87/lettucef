// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.Limit
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.ZAddArgs
import io.lettuce.core.ZAggregateArgs
import io.lettuce.core.ZStoreArgs
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait SortedSetCommandsF[F[_], K, V] {

  def bzpopmin(timeout: Long, keys: K*): F[Option[(K, (Double, V))]]
  
  def bzpopmin(timeout: Double, keys: K*): F[Option[(K, (Double, V))]]
  
  def bzpopmax(timeout: Long, keys: K*): F[Option[(K, (Double, V))]]
  
  def bzpopmax(timeout: Double, keys: K*): F[Option[(K, (Double, V))]]
  
  def zadd(key: K, score: Double, member: V): F[Long]
  
  def zadd(key: K, scoredValues: (Double, V)*): F[Long]
  
  def zadd(key: K, zAddArgs: ZAddArgs, score: Double, member: V): F[Long]
  
  def zadd(key: K, zAddArgs: ZAddArgs, scoredValues: (Double, V)*): F[Long]
  
  def zaddincr(key: K, score: Double, member: V): F[Option[Double]]
  
  def zaddincr(key: K, zAddArgs: ZAddArgs, score: Double, member: V): F[Option[Double]]
  
  def zcard(key: K): F[Long]
  
  def zcount(key: K, range: RedisRange[Double]): F[Long]
  
  def zdiff(keys: K*): F[Seq[V]]
  
  def zdiffstore(destKey: K, srcKeys: K*): F[Long]
  
  def zdiffWithScores(keys: K*): F[Seq[(Double, V)]]
  
  def zincrby(key: K, amount: Double, member: V): F[Double]
  
  def zinter(keys: K*): F[Seq[V]]
  
  def zinter(aggregateArgs: ZAggregateArgs, keys: K*): F[Seq[V]]
  
  def zinterWithScores(aggregateArgs: ZAggregateArgs, keys: K*): F[Seq[(Double, V)]]
  
  def zinterWithScores(keys: K*): F[Seq[(Double, V)]]
  
  def zinterstore(destination: K, keys: K*): F[Long]
  
  def zinterstore(destination: K, storeArgs: ZStoreArgs, keys: K*): F[Long]
  
  def zlexcount(key: K, range: RedisRange[V]): F[Long]
  
  def zmscore(key: K, members: V*): F[Seq[Option[Double]]]
  
  def zpopmin(key: K): F[Option[(Double, V)]]
  
  def zpopmin(key: K, count: Long): F[Seq[(Double, V)]]
  
  def zpopmax(key: K): F[Option[(Double, V)]]
  
  def zpopmax(key: K, count: Long): F[Seq[(Double, V)]]
  
  def zrandmember(key: K): F[Option[V]]
  
  def zrandmember(key: K, count: Long): F[Seq[V]]
  
  def zrandmemberWithScores(key: K): F[Option[(Double, V)]]
  
  def zrandmemberWithScores(key: K, count: Long): F[Seq[(Double, V)]]
  
  def zrange(key: K, start: Long, stop: Long): F[Seq[V]]
  
  def zrangeWithScores(key: K, start: Long, stop: Long): F[Seq[(Double, V)]]
  
  def zrangebylex(key: K, range: RedisRange[V]): F[Seq[V]]
  
  def zrangebylex(key: K, range: RedisRange[V], limit: Limit): F[Seq[V]]
  
  def zrangebyscore(key: K, range: RedisRange[Double]): F[Seq[V]]
  
  def zrangebyscore(key: K, range: RedisRange[Double], limit: Limit): F[Seq[V]]
  
  def zrangebyscoreWithScores(key: K, range: RedisRange[Double]): F[Seq[(Double, V)]]
  
  def zrangebyscoreWithScores(key: K, range: RedisRange[Double], limit: Limit): F[Seq[(Double, V)]]
  
  def zrangestorebylex(dstKey: K, srcKey: K, range: RedisRange[V], limit: Limit): F[Long]
  
  def zrangestorebyscore(dstKey: K, srcKey: K, range: RedisRange[Double], limit: Limit): F[Long]
  
  def zrank(key: K, member: V): F[Option[Long]]
  
  def zrem(key: K, members: V*): F[Long]
  
  def zremrangebylex(key: K, range: RedisRange[V]): F[Long]
  
  def zremrangebyrank(key: K, start: Long, stop: Long): F[Long]
  
  def zremrangebyscore(key: K, range: RedisRange[Double]): F[Long]
  
  def zrevrange(key: K, start: Long, stop: Long): F[Seq[V]]
  
  def zrevrangeWithScores(key: K, start: Long, stop: Long): F[Seq[(Double, V)]]
  
  def zrevrangebylex(key: K, range: RedisRange[V]): F[Seq[V]]
  
  def zrevrangebylex(key: K, range: RedisRange[V], limit: Limit): F[Seq[V]]
  
  def zrevrangebyscore(key: K, range: RedisRange[Double]): F[Seq[V]]
  
  def zrevrangebyscore(key: K, range: RedisRange[Double], limit: Limit): F[Seq[V]]
  
  def zrevrangebyscoreWithScores(key: K, range: RedisRange[Double]): F[Seq[(Double, V)]]
  
  def zrevrangebyscoreWithScores(key: K, range: RedisRange[Double], limit: Limit): F[Seq[(Double, V)]]
  
  def zrevrangestorebylex(dstKey: K, srcKey: K, range: RedisRange[V], limit: Limit): F[Long]
  
  def zrevrangestorebyscore(dstKey: K, srcKey: K, range: RedisRange[Double], limit: Limit): F[Long]
  
  def zrevrank(key: K, member: V): F[Option[Long]]
  
  def zscan(key: K): F[DataScanCursor[(Double, V)]]
  
  def zscan(key: K, scanArgs: ScanArgs): F[DataScanCursor[(Double, V)]]
  
  def zscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[(Double, V)]]
  
  def zscan(key: K, scanCursor: ScanCursor): F[DataScanCursor[(Double, V)]]
  
  def zscore(key: K, member: V): F[Option[Double]]
  
  def zunion(keys: K*): F[Seq[V]]
  
  def zunion(aggregateArgs: ZAggregateArgs, keys: K*): F[Seq[V]]
  
  def zunionWithScores(aggregateArgs: ZAggregateArgs, keys: K*): F[Seq[(Double, V)]]
  
  def zunionWithScores(keys: K*): F[Seq[(Double, V)]]
  
  def zunionstore(destination: K, keys: K*): F[Long]
  
  def zunionstore(destination: K, storeArgs: ZStoreArgs, keys: K*): F[Long]
  
}