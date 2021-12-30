// Code generated. DO NOT EDIT
package io.lettucef.core.commands

import cats.syntax.functor._
import io.lettuce.core.GeoAddArgs
import io.lettuce.core.GeoArgs
import io.lettuce.core.GeoCoordinates
import io.lettuce.core.GeoRadiusStoreArgs
import io.lettuce.core.GeoSearch
import io.lettuce.core.GeoValue
import io.lettuce.core.GeoWithin
import io.lettuce.core.api.async._
import io.lettucef.core.util.LettuceValueConverter
import io.lettucef.core.util.{JavaFutureUtil => JF}
import scala.jdk.CollectionConverters._


trait GeoCommands[F[_], K, V] extends AsyncCallCommands[F, K, V] {

  protected val underlying: RedisGeoAsyncCommands[K, V]
  
  def geoadd(key: K, longitude: Double, latitude: Double, member: V): F[Long] =
    JF.toAsync(underlying.geoadd(key, longitude, latitude, member)).map(Long2long)
  
  def geoadd(key: K, longitude: Double, latitude: Double, member: V, args: GeoAddArgs): F[Long] =
    JF.toAsync(underlying.geoadd(key, longitude, latitude, member, args)).map(Long2long)
  
  def geoadd(key: K, values: GeoValue[V]*): F[Long] =
    JF.toAsync(underlying.geoadd(key, values: _*)).map(Long2long)
  
  def geoadd(key: K, args: GeoAddArgs, values: GeoValue[V]*): F[Long] =
    JF.toAsync(underlying.geoadd(key, args, values: _*)).map(Long2long)
  
  def geodist(key: K, from: V, to: V, unit: GeoArgs.Unit): F[Double] =
    JF.toAsync(underlying.geodist(key, from, to, unit)).map(Double2double)
  
  def geohash(key: K, members: V*): F[Seq[Option[String]]] =
    JF.toAsync(underlying.geohash(key, members: _*)).map(_.asScala.toSeq.map(v => LettuceValueConverter.fromValue(v)))
  
  def geopos(key: K, members: V*): F[Seq[GeoCoordinates]] =
    JF.toAsync(underlying.geopos(key, members: _*)).map(_.asScala.toSeq)
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit): F[Set[V]] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit)).map(_.asScala.toSet)
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit, geoArgs)).map(_.asScala.toSeq)
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[Long] =
    JF.toAsync(underlying.georadius(key, longitude, latitude, distance, unit, geoRadiusStoreArgs)).map(Long2long)
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit): F[Set[V]] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit)).map(_.asScala.toSet)
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit, geoArgs)).map(_.asScala.toSeq)
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[Long] =
    JF.toAsync(underlying.georadiusbymember(key, member, distance, unit, geoRadiusStoreArgs)).map(Long2long)
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate): F[Set[V]] =
    JF.toAsync(underlying.geosearch(key, reference, predicate)).map(_.asScala.toSet)
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]] =
    JF.toAsync(underlying.geosearch(key, reference, predicate, geoArgs)).map(_.asScala.toSeq)
  
  def geosearchstore(destination: K, key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs, storeDist: Boolean): F[Long] =
    JF.toAsync(underlying.geosearchstore(destination, key, reference, predicate, geoArgs, storeDist)).map(Long2long)
  
}

