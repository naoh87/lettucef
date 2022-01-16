// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import dev.naoh.lettucef.api.commands.KeyCommandsF
import dev.naoh.lettucef.api.Commands
import java.time.Duration
import java.time.Instant
import cats.syntax.functor._
import dev.naoh.lettucef.api.models._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.CopyArgs
import io.lettuce.core.MigrateArgs
import io.lettuce.core.RestoreArgs
import io.lettuce.core.ScanArgs
import io.lettuce.core.ScanCursor
import io.lettuce.core.SortArgs
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait KeyCommands[F[_], K, V] extends CommandsDeps[F, K, V] with KeyCommandsF[Commands.Compose[F, F]#R, K, V] {

  protected val underlying: RedisKeyAsyncCommands[K, V]
  
  def copy(source: K, destination: K): F[F[Boolean]] =
    JF.toAsync(underlying.copy(source, destination)).map(_.map(Boolean2boolean))
  
  def copy(source: K, destination: K, copyArgs: CopyArgs): F[F[Boolean]] =
    JF.toAsync(underlying.copy(source, destination, copyArgs)).map(_.map(Boolean2boolean))
  
  def del(keys: K*): F[F[Long]] =
    JF.toAsync(underlying.del(keys: _*)).map(_.map(Long2long))
  
  def unlink(keys: K*): F[F[Long]] =
    JF.toAsync(underlying.unlink(keys: _*)).map(_.map(Long2long))
  
  def dump(key: K): F[F[Option[Array[Byte]]]] =
    JF.toAsync(underlying.dump(key)).map(_.map(Option(_)))
  
  def exists(keys: K*): F[F[Long]] =
    JF.toAsync(underlying.exists(keys: _*)).map(_.map(Long2long))
  
  def expire(key: K, seconds: Long): F[F[Boolean]] =
    JF.toAsync(underlying.expire(key, seconds)).map(_.map(Boolean2boolean))
  
  def expire(key: K, seconds: Duration): F[F[Boolean]] =
    JF.toAsync(underlying.expire(key, seconds)).map(_.map(Boolean2boolean))
  
  def expireat(key: K, timestamp: Long): F[F[Boolean]] =
    JF.toAsync(underlying.expireat(key, timestamp)).map(_.map(Boolean2boolean))
  
  def expireat(key: K, timestamp: Instant): F[F[Boolean]] =
    JF.toAsync(underlying.expireat(key, timestamp)).map(_.map(Boolean2boolean))
  
  def keys(pattern: K): F[F[Seq[K]]] =
    JF.toAsync(underlying.keys(pattern)).map(_.map(_.asScala.toSeq))
  
  def migrate(host: String, port: Int, key: K, db: Int, timeout: Long): F[F[String]] =
    JF.toAsync(underlying.migrate(host, port, key, db, timeout))
  
  def migrate(host: String, port: Int, db: Int, timeout: Long, migrateArgs: MigrateArgs[K]): F[F[String]] =
    JF.toAsync(underlying.migrate(host, port, db, timeout, migrateArgs))
  
  def move(key: K, db: Int): F[F[Boolean]] =
    JF.toAsync(underlying.move(key, db)).map(_.map(Boolean2boolean))
  
  def objectEncoding(key: K): F[F[Option[String]]] =
    JF.toAsync(underlying.objectEncoding(key)).map(_.map(Option(_)))
  
  def objectFreq(key: K): F[F[Long]] =
    JF.toAsync(underlying.objectFreq(key)).map(_.map(Long2long))
  
  def objectIdletime(key: K): F[F[Long]] =
    JF.toAsync(underlying.objectIdletime(key)).map(_.map(Long2long))
  
  def objectRefcount(key: K): F[F[Long]] =
    JF.toAsync(underlying.objectRefcount(key)).map(_.map(Long2long))
  
  def persist(key: K): F[F[Boolean]] =
    JF.toAsync(underlying.persist(key)).map(_.map(Boolean2boolean))
  
  def pexpire(key: K, milliseconds: Long): F[F[Boolean]] =
    JF.toAsync(underlying.pexpire(key, milliseconds)).map(_.map(Boolean2boolean))
  
  def pexpire(key: K, milliseconds: Duration): F[F[Boolean]] =
    JF.toAsync(underlying.pexpire(key, milliseconds)).map(_.map(Boolean2boolean))
  
  def pexpireat(key: K, timestamp: Long): F[F[Boolean]] =
    JF.toAsync(underlying.pexpireat(key, timestamp)).map(_.map(Boolean2boolean))
  
  def pexpireat(key: K, timestamp: Instant): F[F[Boolean]] =
    JF.toAsync(underlying.pexpireat(key, timestamp)).map(_.map(Boolean2boolean))
  
  def pttl(key: K): F[F[Long]] =
    JF.toAsync(underlying.pttl(key)).map(_.map(Long2long))
  
  def randomkey(): F[F[Option[K]]] =
    JF.toAsync(underlying.randomkey()).map(_.map(Option(_)))
  
  def rename(key: K, newKey: K): F[F[String]] =
    JF.toAsync(underlying.rename(key, newKey))
  
  def renamenx(key: K, newKey: K): F[F[Boolean]] =
    JF.toAsync(underlying.renamenx(key, newKey)).map(_.map(Boolean2boolean))
  
  def restore(key: K, ttl: Long, value: Array[Byte]): F[F[String]] =
    JF.toAsync(underlying.restore(key, ttl, value))
  
  def restore(key: K, value: Array[Byte], args: RestoreArgs): F[F[String]] =
    JF.toAsync(underlying.restore(key, value, args))
  
  def sort(key: K): F[F[Seq[V]]] =
    JF.toAsync(underlying.sort(key)).map(_.map(_.asScala.toSeq))
  
  def sort(key: K, sortArgs: SortArgs): F[F[Seq[V]]] =
    JF.toAsync(underlying.sort(key, sortArgs)).map(_.map(_.asScala.toSeq))
  
  def sortStore(key: K, sortArgs: SortArgs, destination: K): F[F[Long]] =
    JF.toAsync(underlying.sortStore(key, sortArgs, destination)).map(_.map(Long2long))
  
  def touch(keys: K*): F[F[Long]] =
    JF.toAsync(underlying.touch(keys: _*)).map(_.map(Long2long))
  
  def ttl(key: K): F[F[Long]] =
    JF.toAsync(underlying.ttl(key)).map(_.map(Long2long))
  
  def `type`(key: K): F[F[String]] =
    JF.toAsync(underlying.`type`(key))
  
  def scan(): F[F[DataScanCursor[K]]] =
    JF.toAsync(underlying.scan()).map(_.map(cur => DataScanCursor.from(cur)))
  
  def scan(scanArgs: ScanArgs): F[F[DataScanCursor[K]]] =
    JF.toAsync(underlying.scan(scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def scan(scanCursor: ScanCursor, scanArgs: ScanArgs): F[F[DataScanCursor[K]]] =
    JF.toAsync(underlying.scan(scanCursor, scanArgs)).map(_.map(cur => DataScanCursor.from(cur)))
  
  def scan(scanCursor: ScanCursor): F[F[DataScanCursor[K]]] =
    JF.toAsync(underlying.scan(scanCursor)).map(_.map(cur => DataScanCursor.from(cur)))
  
}
