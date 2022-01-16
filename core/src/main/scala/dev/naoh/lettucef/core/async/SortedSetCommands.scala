// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.SortedSetCommandsF
import dev.naoh.lettucef.api.Commands
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


trait SortedSetCommands[F[_], K, V] extends CommandsDeps[F, K, V] with SortedSetCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisSortedSetAsyncCommands[K, V]
  
  def bzpopmin(timeout: Long, keys: K*): F[F[Option[(K, (Double, V))]]] =
    JF.toAsync(underlying.bzpopmin(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv).fmap(LettuceValueConverter.fromScoredValueUnsafe))))
  
  def bzpopmin(timeout: Double, keys: K*): F[F[Option[(K, (Double, V))]]] =
    JF.toAsync(underlying.bzpopmin(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv).fmap(LettuceValueConverter.fromScoredValueUnsafe))))
  
  def bzpopmax(timeout: Long, keys: K*): F[F[Option[(K, (Double, V))]]] =
    JF.toAsync(underlying.bzpopmax(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv).fmap(LettuceValueConverter.fromScoredValueUnsafe))))
  
  def bzpopmax(timeout: Double, keys: K*): F[F[Option[(K, (Double, V))]]] =
    JF.toAsync(underlying.bzpopmax(timeout, keys: _*)).map(_.map(Option(_).map(kv => LettuceValueConverter.fromKeyValueUnsafe(kv).fmap(LettuceValueConverter.fromScoredValueUnsafe))))
  
  def zadd(key: K, score: Double, member: V): F[F[Long]] =
    JF.toAsync(underlying.zadd(key, score, member)).map(_.map(Long2long))
  
  def zadd(key: K, scoredValues: (Double, V)*): F[F[Long]] =
    JF.toAsync(underlying.zadd(key, scoredValues.map(LettuceValueConverter.toScoredValue): _*)).map(_.map(Long2long))
  
  def zadd(key: K, zAddArgs: ZAddArgs, score: Double, member: V): F[F[Long]] =
    JF.toAsync(underlying.zadd(key, zAddArgs, score, member)).map(_.map(Long2long))
  
  def zadd(key: K, zAddArgs: ZAddArgs, scoredValues: (Double, V)*): F[F[Long]] =
    JF.toAsync(underlying.zadd(key, zAddArgs, scoredValues.map(LettuceValueConverter.toScoredValue): _*)).map(_.map(Long2long))
  
  def zaddincr(key: K, score: Double, member: V): F[F[Option[Double]]] =
    JF.toAsync(underlying.zaddincr(key, score, member)).map(_.map(Option(_).map(Double2double)))
  
  def zaddincr(key: K, zAddArgs: ZAddArgs, score: Double, member: V): F[F[Option[Double]]] =
    JF.toAsync(underlying.zaddincr(key, zAddArgs, score, member)).map(_.map(Option(_).map(Double2double)))
  
  def zcard(key: K): F[F[Long]] =
    JF.toAsync(underlying.zcard(key)).map(_.map(Long2long))
  
  def zcount(key: K, range: RedisRange[Double]): F[F[Long]] =
    JF.toAsync(underlying.zcount(key, range.toJavaNumber)).map(_.map(Long2long))
  
  def zdiff(keys: K*): F[F[Seq[V]]] =
    JF.toAsync(underlying.zdiff(keys: _*)).map(_.map(_.asScala.toSeq))
  
  def zdiffstore(destKey: K, srcKeys: K*): F[F[Long]] =
    JF.toAsync(underlying.zdiffstore(destKey, srcKeys: _*)).map(_.map(Long2long))
  
  def zdiffWithScores(keys: K*): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zdiffWithScores(keys: _*)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zincrby(key: K, amount: Double, member: V): F[F[Double]] =
    JF.toAsync(underlying.zincrby(key, amount, member)).map(_.map(Double2double))
  
  def zinter(keys: K*): F[F[Seq[V]]] =
    JF.toAsync(underlying.zinter(keys: _*)).map(_.map(_.asScala.toSeq))
  
  def zinter(aggregateArgs: ZAggregateArgs, keys: K*): F[F[Seq[V]]] =
    JF.toAsync(underlying.zinter(aggregateArgs, keys: _*)).map(_.map(_.asScala.toSeq))
  
  def zinterWithScores(aggregateArgs: ZAggregateArgs, keys: K*): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zinterWithScores(aggregateArgs, keys: _*)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zinterWithScores(keys: K*): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zinterWithScores(keys: _*)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zinterstore(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.zinterstore(destination, keys: _*)).map(_.map(Long2long))
  
  def zinterstore(destination: K, storeArgs: ZStoreArgs, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.zinterstore(destination, storeArgs, keys: _*)).map(_.map(Long2long))
  
  def zlexcount(key: K, range: RedisRange[V]): F[F[Long]] =
    JF.toAsync(underlying.zlexcount(key, range.toJava)).map(_.map(Long2long))
  
  def zmscore(key: K, members: V*): F[F[Seq[Option[Double]]]] =
    JF.toAsync(underlying.zmscore(key, members: _*)).map(_.map(_.asScala.toSeq.map(Option(_).map(Double2double))))
  
  def zpopmin(key: K): F[F[Option[(Double, V)]]] =
    JF.toAsync(underlying.zpopmin(key)).map(_.map(LettuceValueConverter.fromScoredValue))
  
  def zpopmin(key: K, count: Long): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zpopmin(key, count)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zpopmax(key: K): F[F[Option[(Double, V)]]] =
    JF.toAsync(underlying.zpopmax(key)).map(_.map(LettuceValueConverter.fromScoredValue))
  
  def zpopmax(key: K, count: Long): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zpopmax(key, count)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrandmember(key: K): F[F[Option[V]]] =
    JF.toAsync(underlying.zrandmember(key)).map(_.map(Option(_)))
  
  def zrandmember(key: K, count: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrandmember(key, count)).map(_.map(_.asScala.toSeq))
  
  def zrandmemberWithScores(key: K): F[F[Option[(Double, V)]]] =
    JF.toAsync(underlying.zrandmemberWithScores(key)).map(_.map(LettuceValueConverter.fromScoredValue))
  
  def zrandmemberWithScores(key: K, count: Long): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrandmemberWithScores(key, count)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrange(key: K, start: Long, stop: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrange(key, start, stop)).map(_.map(_.asScala.toSeq))
  
  def zrangeWithScores(key: K, start: Long, stop: Long): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrangeWithScores(key, start, stop)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrangebylex(key: K, range: RedisRange[V]): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrangebylex(key, range.toJava)).map(_.map(_.asScala.toSeq))
  
  def zrangebylex(key: K, range: RedisRange[V], limit: Limit): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrangebylex(key, range.toJava, limit)).map(_.map(_.asScala.toSeq))
  
  def zrangebyscore(key: K, range: RedisRange[Double]): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrangebyscore(key, range.toJavaNumber)).map(_.map(_.asScala.toSeq))
  
  def zrangebyscore(key: K, range: RedisRange[Double], limit: Limit): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrangebyscore(key, range.toJavaNumber, limit)).map(_.map(_.asScala.toSeq))
  
  def zrangebyscoreWithScores(key: K, range: RedisRange[Double]): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrangebyscoreWithScores(key, range.toJavaNumber)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrangebyscoreWithScores(key: K, range: RedisRange[Double], limit: Limit): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrangebyscoreWithScores(key, range.toJavaNumber, limit)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrangestorebylex(dstKey: K, srcKey: K, range: RedisRange[V], limit: Limit): F[F[Long]] =
    JF.toAsync(underlying.zrangestorebylex(dstKey, srcKey, range.toJava, limit)).map(_.map(Long2long))
  
  def zrangestorebyscore(dstKey: K, srcKey: K, range: RedisRange[Double], limit: Limit): F[F[Long]] =
    JF.toAsync(underlying.zrangestorebyscore(dstKey, srcKey, range.toJavaNumber, limit)).map(_.map(Long2long))
  
  def zrank(key: K, member: V): F[F[Option[Long]]] =
    JF.toAsync(underlying.zrank(key, member)).map(_.map(Option(_).map(Long2long)))
  
  def zrem(key: K, members: V*): F[F[Long]] =
    JF.toAsync(underlying.zrem(key, members: _*)).map(_.map(Long2long))
  
  def zremrangebylex(key: K, range: RedisRange[V]): F[F[Long]] =
    JF.toAsync(underlying.zremrangebylex(key, range.toJava)).map(_.map(Long2long))
  
  def zremrangebyrank(key: K, start: Long, stop: Long): F[F[Long]] =
    JF.toAsync(underlying.zremrangebyrank(key, start, stop)).map(_.map(Long2long))
  
  def zremrangebyscore(key: K, range: RedisRange[Double]): F[F[Long]] =
    JF.toAsync(underlying.zremrangebyscore(key, range.toJavaNumber)).map(_.map(Long2long))
  
  def zrevrange(key: K, start: Long, stop: Long): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrevrange(key, start, stop)).map(_.map(_.asScala.toSeq))
  
  def zrevrangeWithScores(key: K, start: Long, stop: Long): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrevrangeWithScores(key, start, stop)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrevrangebylex(key: K, range: RedisRange[V]): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrevrangebylex(key, range.toJava)).map(_.map(_.asScala.toSeq))
  
  def zrevrangebylex(key: K, range: RedisRange[V], limit: Limit): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrevrangebylex(key, range.toJava, limit)).map(_.map(_.asScala.toSeq))
  
  def zrevrangebyscore(key: K, range: RedisRange[Double]): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrevrangebyscore(key, range.toJavaNumber)).map(_.map(_.asScala.toSeq))
  
  def zrevrangebyscore(key: K, range: RedisRange[Double], limit: Limit): F[F[Seq[V]]] =
    JF.toAsync(underlying.zrevrangebyscore(key, range.toJavaNumber, limit)).map(_.map(_.asScala.toSeq))
  
  def zrevrangebyscoreWithScores(key: K, range: RedisRange[Double]): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrevrangebyscoreWithScores(key, range.toJavaNumber)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrevrangebyscoreWithScores(key: K, range: RedisRange[Double], limit: Limit): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zrevrangebyscoreWithScores(key, range.toJavaNumber, limit)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zrevrangestorebylex(dstKey: K, srcKey: K, range: RedisRange[V], limit: Limit): F[F[Long]] =
    JF.toAsync(underlying.zrevrangestorebylex(dstKey, srcKey, range.toJava, limit)).map(_.map(Long2long))
  
  def zrevrangestorebyscore(dstKey: K, srcKey: K, range: RedisRange[Double], limit: Limit): F[F[Long]] =
    JF.toAsync(underlying.zrevrangestorebyscore(dstKey, srcKey, range.toJavaNumber, limit)).map(_.map(Long2long))
  
  def zrevrank(key: K, member: V): F[F[Option[Long]]] =
    JF.toAsync(underlying.zrevrank(key, member)).map(_.map(Option(_).map(Long2long)))
  
  def zscan(key: K): F[F[DataScanCursor[(Double, V)]]] =
    JF.toAsync(underlying.zscan(key)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def zscan(key: K, scanArgs: ScanArgs): F[F[DataScanCursor[(Double, V)]]] =
    JF.toAsync(underlying.zscan(key, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def zscan(key: K, scanCursor: ScanCursor, scanArgs: ScanArgs): F[F[DataScanCursor[(Double, V)]]] =
    JF.toAsync(underlying.zscan(key, scanCursor, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def zscan(key: K, scanCursor: ScanCursor): F[F[DataScanCursor[(Double, V)]]] =
    JF.toAsync(underlying.zscan(key, scanCursor)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def zscore(key: K, member: V): F[F[Option[Double]]] =
    JF.toAsync(underlying.zscore(key, member)).map(_.map(Option(_).map(Double2double)))
  
  def zunion(keys: K*): F[F[Seq[V]]] =
    JF.toAsync(underlying.zunion(keys: _*)).map(_.map(_.asScala.toSeq))
  
  def zunion(aggregateArgs: ZAggregateArgs, keys: K*): F[F[Seq[V]]] =
    JF.toAsync(underlying.zunion(aggregateArgs, keys: _*)).map(_.map(_.asScala.toSeq))
  
  def zunionWithScores(aggregateArgs: ZAggregateArgs, keys: K*): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zunionWithScores(aggregateArgs, keys: _*)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zunionWithScores(keys: K*): F[F[Seq[(Double, V)]]] =
    JF.toAsync(underlying.zunionWithScores(keys: _*)).map(_.map(_.asScala.toSeq.map(LettuceValueConverter.fromScoredValueUnsafe)))
  
  def zunionstore(destination: K, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.zunionstore(destination, keys: _*)).map(_.map(Long2long))
  
  def zunionstore(destination: K, storeArgs: ZStoreArgs, keys: K*): F[F[Long]] =
    JF.toAsync(underlying.zunionstore(destination, storeArgs, keys: _*)).map(_.map(Long2long))
  
}
