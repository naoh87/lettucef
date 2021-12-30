// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import java.time.Duration
import java.time.Instant
import io.lettuce.core.CopyArgs
import io.lettuce.core.KeyScanCursor
import io.lettuce.core.MigrateArgs
import io.lettuce.core.RestoreArgs
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.SortArgs
import io.lettuce.core.StreamScanCursor
import io.lettuce.core.output.KeyStreamingChannel
import io.lettuce.core.output.ValueStreamingChannel
import cats.syntax.functor._
import io.lettuce.core.api.async._
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait KeyCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisKeyAsyncCommands[K, V]
  
  def copy(source: K, destination: K): F[Boolean] =
    JF.toAsync(underlying.copy(source, destination)).map(Boolean2boolean)
  
  def copy(source: K, destination: K, copyArgs: CopyArgs): F[Boolean] =
    JF.toAsync(underlying.copy(source, destination, copyArgs)).map(Boolean2boolean)
  
  def del(keys: K*): F[Long] =
    JF.toAsync(underlying.del(keys: _*)).map(Long2long)
  
  def unlink(keys: K*): F[Long] =
    JF.toAsync(underlying.unlink(keys: _*)).map(Long2long)
  
  def dump(key: K): F[Array[Byte]] =
    JF.toAsync(underlying.dump(key))
  
  def exists(keys: K*): F[Long] =
    JF.toAsync(underlying.exists(keys: _*)).map(Long2long)
  
  def expire(key: K, seconds: Long): F[Boolean] =
    JF.toAsync(underlying.expire(key, seconds)).map(Boolean2boolean)
  
  def expire(key: K, seconds: Duration): F[Boolean] =
    JF.toAsync(underlying.expire(key, seconds)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Long): F[Boolean] =
    JF.toAsync(underlying.expireat(key, timestamp)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Instant): F[Boolean] =
    JF.toAsync(underlying.expireat(key, timestamp)).map(Boolean2boolean)
  
  def keys(pattern: K): F[Seq[K]] =
    JF.toAsync(underlying.keys(pattern)).map(_.asScala.toSeq)
  
  def keys(channel: KeyStreamingChannel[K], pattern: K): F[Long] =
    JF.toAsync(underlying.keys(channel, pattern)).map(Long2long)
  
  def migrate(host: String, port: Int, key: K, db: Int, timeout: Long): F[String] =
    JF.toAsync(underlying.migrate(host, port, key, db, timeout))
  
  def migrate(host: String, port: Int, db: Int, timeout: Long, migrateArgs: MigrateArgs[K]): F[String] =
    JF.toAsync(underlying.migrate(host, port, db, timeout, migrateArgs))
  
  def move(key: K, db: Int): F[Boolean] =
    JF.toAsync(underlying.move(key, db)).map(Boolean2boolean)
  
  def objectEncoding(key: K): F[String] =
    JF.toAsync(underlying.objectEncoding(key))
  
  def objectFreq(key: K): F[Long] =
    JF.toAsync(underlying.objectFreq(key)).map(Long2long)
  
  def objectIdletime(key: K): F[Long] =
    JF.toAsync(underlying.objectIdletime(key)).map(Long2long)
  
  def objectRefcount(key: K): F[Long] =
    JF.toAsync(underlying.objectRefcount(key)).map(Long2long)
  
  def persist(key: K): F[Boolean] =
    JF.toAsync(underlying.persist(key)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Long): F[Boolean] =
    JF.toAsync(underlying.pexpire(key, milliseconds)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Duration): F[Boolean] =
    JF.toAsync(underlying.pexpire(key, milliseconds)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Long): F[Boolean] =
    JF.toAsync(underlying.pexpireat(key, timestamp)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Instant): F[Boolean] =
    JF.toAsync(underlying.pexpireat(key, timestamp)).map(Boolean2boolean)
  
  def pttl(key: K): F[Long] =
    JF.toAsync(underlying.pttl(key)).map(Long2long)
  
  def randomkey(): F[K] =
    JF.toAsync(underlying.randomkey())
  
  def rename(key: K, newKey: K): F[String] =
    JF.toAsync(underlying.rename(key, newKey))
  
  def renamenx(key: K, newKey: K): F[Boolean] =
    JF.toAsync(underlying.renamenx(key, newKey)).map(Boolean2boolean)
  
  def restore(key: K, ttl: Long, value: Array[Byte]): F[String] =
    JF.toAsync(underlying.restore(key, ttl, value))
  
  def restore(key: K, value: Array[Byte], args: RestoreArgs): F[String] =
    JF.toAsync(underlying.restore(key, value, args))
  
  def sort(key: K): F[Seq[V]] =
    JF.toAsync(underlying.sort(key)).map(_.asScala.toSeq)
  
  def sort(channel: ValueStreamingChannel[V], key: K): F[Long] =
    JF.toAsync(underlying.sort(channel, key)).map(Long2long)
  
  def sort(key: K, sortArgs: SortArgs): F[Seq[V]] =
    JF.toAsync(underlying.sort(key, sortArgs)).map(_.asScala.toSeq)
  
  def sort(channel: ValueStreamingChannel[V], key: K, sortArgs: SortArgs): F[Long] =
    JF.toAsync(underlying.sort(channel, key, sortArgs)).map(Long2long)
  
  def sortStore(key: K, sortArgs: SortArgs, destination: K): F[Long] =
    JF.toAsync(underlying.sortStore(key, sortArgs, destination)).map(Long2long)
  
  def touch(keys: K*): F[Long] =
    JF.toAsync(underlying.touch(keys: _*)).map(Long2long)
  
  def ttl(key: K): F[Long] =
    JF.toAsync(underlying.ttl(key)).map(Long2long)
  
  def `type`(key: K): F[String] =
    JF.toAsync(underlying.`type`(key))
  
  def scan(): F[KeyScanCursor[K]] =
    JF.toAsync(underlying.scan())
  
  def scan(scanArgs: ScanArgs): F[KeyScanCursor[K]] =
    JF.toAsync(underlying.scan(scanArgs))
  
  def scan(scanCursor: ScanCursor, scanArgs: ScanArgs): F[KeyScanCursor[K]] =
    JF.toAsync(underlying.scan(scanCursor, scanArgs))
  
  def scan(scanCursor: ScanCursor): F[KeyScanCursor[K]] =
    JF.toAsync(underlying.scan(scanCursor))
  
  def scan(channel: KeyStreamingChannel[K]): F[StreamScanCursor] =
    JF.toAsync(underlying.scan(channel))
  
  def scan(channel: KeyStreamingChannel[K], scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.scan(channel, scanArgs))
  
  def scan(channel: KeyStreamingChannel[K], scanCursor: ScanCursor, scanArgs: ScanArgs): F[StreamScanCursor] =
    JF.toAsync(underlying.scan(channel, scanCursor, scanArgs))
  
  def scan(channel: KeyStreamingChannel[K], scanCursor: ScanCursor): F[StreamScanCursor] =
    JF.toAsync(underlying.scan(channel, scanCursor))
  
}

