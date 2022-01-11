// Code generated. DO NOT EDIT
package dev.naoh.lettucef.core.async

import cats.syntax.functor._
import dev.naoh.lettucef.core.commands.CommandsDeps
import dev.naoh.lettucef.core.models.geo._
import dev.naoh.lettucef.core.util.LettuceValueConverter
import dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}
import io.lettuce.core.GeoAddArgs
import io.lettuce.core.GeoArgs
import io.lettuce.core.GeoCoordinates
import io.lettuce.core.GeoRadiusStoreArgs
import io.lettuce.core.GeoSearch
import io.lettuce.core.GeoValue
import io.lettuce.core.api.async._
import scala.jdk.CollectionConverters._


trait GeoCommands[F[_], K, V] extends CommandsDeps[F, K, V] {

  protected val underlying: RedisGeoAsyncCommands[K, V]
  
  def geoadd(key: K, longitude: Double, latitude: Double, member: V): F[F[Long]] =
    JF.toAsync(underlying.geoadd(key, longitude, latitude, member)).map(_.map(Long2long))
  
  def geoadd(key: K, longitude: Double, latitude: Double, member: V, args: GeoAddArgs): F[F[Long]] =
    JF.toAsync(underlying.geoadd(key, longitude, latitude, member, args)).map(_.map(Long2long))
  
  def geoadd(key: K, values: GeoValue[V]*): F[F[Long]] =
    JF.toAsync(underlying.geoadd(key, values: _*)).map(_.map(Long2long))
  
  def geoadd(key: K, args: GeoAddArgs, values: GeoValue[V]*): F[F[Long]] =
    JF.toAsync(underlying.geoadd(key, args, values: _*)).map(_.map(Long2long))
  
  def geodist(key: K, from: V, to: V, unit: GeoArgs.Unit): F[F[Option[Double]]] =
    JF.toAsync(underlying.geodist(key, from, to, unit)).map(_.map(Option(_).map(Double2double)))
  
  def geohash(key: K, members: V*): F[F[Seq[Option[String]]]] =
    JF.toAsync(underlying.geohash(key, members: _*)).map(_.map(_.asScala.toSeq.map(v => LettuceValueConverter.fromValue(v))))
  
  def geopos(key: K, members: V*): F[F[Seq[GeoCoordinates]]] =
    JF.toAsync(underlying.geopos(key, members: _*)).map(_.map(_.asScala.toSeq))
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit): F[F[Set[V]]] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit)).map(_.map(_.asScala.toSet))
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[F[Seq[GeoWithin[V]]]] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit, geoArgs)).map(_.map(_.asScala.toSeq.map(GeoWithin.from)))
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[F[Long]] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit, geoRadiusStoreArgs)).map(_.map(Long2long))
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit): F[F[Set[V]]] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit)).map(_.map(_.asScala.toSet))
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[F[Seq[GeoWithin[V]]]] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit, geoArgs)).map(_.map(_.asScala.toSeq.map(GeoWithin.from)))
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[F[Long]] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit, geoRadiusStoreArgs)).map(_.map(Long2long))
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate): F[F[Set[V]]] =
    JF.toAsync(underlying.geosearch(key, reference, predicate)).map(_.map(_.asScala.toSet))
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs): F[F[Seq[GeoWithin[V]]]] =
    JF.toAsync(underlying.geosearch(key, reference, predicate, geoArgs)).map(_.map(_.asScala.toSeq.map(GeoWithin.from)))
  
  def geosearchstore(destination: K, key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs, storeDist: Boolean): F[F[Long]] =
    JF.toAsync(underlying.geosearchstore(destination, key, reference, predicate, geoArgs, storeDist)).map(_.map(Long2long))
  
}
