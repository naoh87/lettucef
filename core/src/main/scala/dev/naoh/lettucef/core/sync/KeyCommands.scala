// Code generated by codegen/run; DO NOT EDIT
package dev.naoh.lettucef.core.sync

import dev.naoh.lettucef.api.commands.KeyCommandsF
import java.time.Duration
import java.time.Instant
import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.CopyArgs
import io.lettuce.core.ExpireArgs
import io.lettuce.core.MigrateArgs
import io.lettuce.core.RestoreArgs
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.SortArgs
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait KeyCommands[F[_], K, V] extends CommandsDeps[F, K, V] with KeyCommandsF[F, K, V] {

  protected val underlying: RedisKeyAsyncCommands[K, V]
  
  def copy(source: K, destination: K): F[Boolean] =
    JF.toSync(underlying.copy(source, destination)).map(Boolean2boolean)
  
  def copy(source: K, destination: K, copyArgs: CopyArgs): F[Boolean] =
    JF.toSync(underlying.copy(source, destination, copyArgs)).map(Boolean2boolean)
  
  def del(keys: K*): F[Long] =
    JF.toSync(underlying.del(keys: _*)).map(Long2long)
  
  def unlink(keys: K*): F[Long] =
    JF.toSync(underlying.unlink(keys: _*)).map(Long2long)
  
  def dump(key: K): F[Option[Array[Byte]]] =
    JF.toSync(underlying.dump(key)).map(Option(_))
  
  def exists(keys: K*): F[Long] =
    JF.toSync(underlying.exists(keys: _*)).map(Long2long)
  
  def expire(key: K, seconds: Long): F[Boolean] =
    JF.toSync(underlying.expire(key, seconds)).map(Boolean2boolean)
  
  def expire(key: K, seconds: Long, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.expire(key, seconds, expireArgs)).map(Boolean2boolean)
  
  def expire(key: K, seconds: Duration): F[Boolean] =
    JF.toSync(underlying.expire(key, seconds)).map(Boolean2boolean)
  
  def expire(key: K, seconds: Duration, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.expire(key, seconds, expireArgs)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Long): F[Boolean] =
    JF.toSync(underlying.expireat(key, timestamp)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Long, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.expireat(key, timestamp, expireArgs)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Instant): F[Boolean] =
    JF.toSync(underlying.expireat(key, timestamp)).map(Boolean2boolean)
  
  def expireat(key: K, timestamp: Instant, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.expireat(key, timestamp, expireArgs)).map(Boolean2boolean)
  
  def expiretime(key: K): F[Long] =
    JF.toSync(underlying.expiretime(key)).map(Long2long)
  
  def keys(pattern: K): F[Seq[K]] =
    JF.toSync(underlying.keys(pattern)).map(_.asScala.toSeq)
  
  def migrate(host: String, port: Int, key: K, db: Int, timeout: Long): F[String] =
    JF.toSync(underlying.migrate(host, port, key, db, timeout))
  
  def migrate(host: String, port: Int, db: Int, timeout: Long, migrateArgs: MigrateArgs[K]): F[String] =
    JF.toSync(underlying.migrate(host, port, db, timeout, migrateArgs))
  
  def move(key: K, db: Int): F[Boolean] =
    JF.toSync(underlying.move(key, db)).map(Boolean2boolean)
  
  def objectEncoding(key: K): F[Option[String]] =
    JF.toSync(underlying.objectEncoding(key)).map(Option(_))
  
  def objectFreq(key: K): F[Long] =
    JF.toSync(underlying.objectFreq(key)).map(Long2long)
  
  def objectIdletime(key: K): F[Long] =
    JF.toSync(underlying.objectIdletime(key)).map(Long2long)
  
  def objectRefcount(key: K): F[Long] =
    JF.toSync(underlying.objectRefcount(key)).map(Long2long)
  
  def persist(key: K): F[Boolean] =
    JF.toSync(underlying.persist(key)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Long): F[Boolean] =
    JF.toSync(underlying.pexpire(key, milliseconds)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Long, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.pexpire(key, milliseconds, expireArgs)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Duration): F[Boolean] =
    JF.toSync(underlying.pexpire(key, milliseconds)).map(Boolean2boolean)
  
  def pexpire(key: K, milliseconds: Duration, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.pexpire(key, milliseconds, expireArgs)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Long): F[Boolean] =
    JF.toSync(underlying.pexpireat(key, timestamp)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Long, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.pexpireat(key, timestamp, expireArgs)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Instant): F[Boolean] =
    JF.toSync(underlying.pexpireat(key, timestamp)).map(Boolean2boolean)
  
  def pexpireat(key: K, timestamp: Instant, expireArgs: ExpireArgs): F[Boolean] =
    JF.toSync(underlying.pexpireat(key, timestamp, expireArgs)).map(Boolean2boolean)
  
  def pexpiretime(key: K): F[Long] =
    JF.toSync(underlying.pexpiretime(key)).map(Long2long)
  
  def pttl(key: K): F[Long] =
    JF.toSync(underlying.pttl(key)).map(Long2long)
  
  def randomkey(): F[Option[K]] =
    JF.toSync(underlying.randomkey()).map(Option(_))
  
  def rename(key: K, newKey: K): F[String] =
    JF.toSync(underlying.rename(key, newKey))
  
  def renamenx(key: K, newKey: K): F[Boolean] =
    JF.toSync(underlying.renamenx(key, newKey)).map(Boolean2boolean)
  
  def restore(key: K, ttl: Long, value: Array[Byte]): F[String] =
    JF.toSync(underlying.restore(key, ttl, value))
  
  def restore(key: K, value: Array[Byte], args: RestoreArgs): F[String] =
    JF.toSync(underlying.restore(key, value, args))
  
  def sort(key: K): F[Seq[V]] =
    JF.toSync(underlying.sort(key)).map(_.asScala.toSeq)
  
  def sort(key: K, sortArgs: SortArgs): F[Seq[V]] =
    JF.toSync(underlying.sort(key, sortArgs)).map(_.asScala.toSeq)
  
  def sortReadOnly(key: K): F[Seq[V]] =
    JF.toSync(underlying.sortReadOnly(key)).map(_.asScala.toSeq)
  
  def sortReadOnly(key: K, sortArgs: SortArgs): F[Seq[V]] =
    JF.toSync(underlying.sortReadOnly(key, sortArgs)).map(_.asScala.toSeq)
  
  def sortStore(key: K, sortArgs: SortArgs, destination: K): F[Long] =
    JF.toSync(underlying.sortStore(key, sortArgs, destination)).map(Long2long)
  
  def touch(keys: K*): F[Long] =
    JF.toSync(underlying.touch(keys: _*)).map(Long2long)
  
  def ttl(key: K): F[Long] =
    JF.toSync(underlying.ttl(key)).map(Long2long)
  
  def `type`(key: K): F[String] =
    JF.toSync(underlying.`type`(key))
  
  def scan(): F[RedisScanCursor[K]] =
    JF.toSync(underlying.scan()).map(cur => RedisScanCursor.from(cur))
  
  def scan(scanArgs: ScanArgs): F[RedisScanCursor[K]] =
    JF.toSync(underlying.scan(scanArgs)).map(cur => RedisScanCursor.from(cur))
  
  def scan(scanCursor: ScanCursor, scanArgs: ScanArgs): F[RedisScanCursor[K]] =
    JF.toSync(underlying.scan(scanCursor, scanArgs)).map(cur => RedisScanCursor.from(cur))
  
  def scan(scanCursor: ScanCursor): F[RedisScanCursor[K]] =
    JF.toSync(underlying.scan(scanCursor)).map(cur => RedisScanCursor.from(cur))
  
}
