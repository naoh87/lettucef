// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

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


trait KeyCommandsF[F[_], K, V] {

  def copy(source: K, destination: K): F[Boolean]
  
  def copy(source: K, destination: K, copyArgs: CopyArgs): F[Boolean]
  
  def del(keys: K*): F[Long]
  
  def unlink(keys: K*): F[Long]
  
  def dump(key: K): F[Option[Array[Byte]]]
  
  def exists(keys: K*): F[Long]
  
  def expire(key: K, seconds: Long): F[Boolean]
  
  def expire(key: K, seconds: Duration): F[Boolean]
  
  def expireat(key: K, timestamp: Long): F[Boolean]
  
  def expireat(key: K, timestamp: Instant): F[Boolean]
  
  def keys(pattern: K): F[Seq[K]]
  
  def migrate(host: String, port: Int, key: K, db: Int, timeout: Long): F[String]
  
  def migrate(host: String, port: Int, db: Int, timeout: Long, migrateArgs: MigrateArgs[K]): F[String]
  
  def move(key: K, db: Int): F[Boolean]
  
  def objectEncoding(key: K): F[Option[String]]
  
  def objectFreq(key: K): F[Long]
  
  def objectIdletime(key: K): F[Long]
  
  def objectRefcount(key: K): F[Long]
  
  def persist(key: K): F[Boolean]
  
  def pexpire(key: K, milliseconds: Long): F[Boolean]
  
  def pexpire(key: K, milliseconds: Duration): F[Boolean]
  
  def pexpireat(key: K, timestamp: Long): F[Boolean]
  
  def pexpireat(key: K, timestamp: Instant): F[Boolean]
  
  def pttl(key: K): F[Long]
  
  def randomkey(): F[Option[K]]
  
  def rename(key: K, newKey: K): F[String]
  
  def renamenx(key: K, newKey: K): F[Boolean]
  
  def restore(key: K, ttl: Long, value: Array[Byte]): F[String]
  
  def restore(key: K, value: Array[Byte], args: RestoreArgs): F[String]
  
  def sort(key: K): F[Seq[V]]
  
  def sort(key: K, sortArgs: SortArgs): F[Seq[V]]
  
  def sortStore(key: K, sortArgs: SortArgs, destination: K): F[Long]
  
  def touch(keys: K*): F[Long]
  
  def ttl(key: K): F[Long]
  
  def `type`(key: K): F[String]
  
  def scan(): F[DataScanCursor[K]]
  
  def scan(scanArgs: ScanArgs): F[DataScanCursor[K]]
  
  def scan(scanCursor: ScanCursor, scanArgs: ScanArgs): F[DataScanCursor[K]]
  
  def scan(scanCursor: ScanCursor): F[DataScanCursor[K]]
  
}
