// Code generated. DO NOT EDIT
package dev.naoh.lettucef.api.commands

import dev.naoh.lettucef.api.models.geo._
import io.lettuce.core.GeoAddArgs
import io.lettuce.core.GeoArgs
import io.lettuce.core.GeoCoordinates
import io.lettuce.core.GeoRadiusStoreArgs
import io.lettuce.core.GeoSearch
import io.lettuce.core.GeoValue


trait GeoCommandsF[F[_], K, V] {

  def geoadd(key: K, longitude: Double, latitude: Double, member: V): F[Long]
  
  def geoadd(key: K, longitude: Double, latitude: Double, member: V, args: GeoAddArgs): F[Long]
  
  def geoadd(key: K, values: GeoValue[V]*): F[Long]
  
  def geoadd(key: K, args: GeoAddArgs, values: GeoValue[V]*): F[Long]
  
  def geodist(key: K, from: V, to: V, unit: GeoArgs.Unit): F[Option[Double]]
  
  def geohash(key: K, members: V*): F[Seq[Option[String]]]
  
  def geopos(key: K, members: V*): F[Seq[GeoCoordinates]]
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit): F[Set[V]]
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]]
  
  def georadius(key: K, longitude: Double, latitude: Double, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[Long]
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit): F[Set[V]]
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]]
  
  def georadiusbymember(key: K, member: V, distance: Double, unit: GeoArgs.Unit, geoRadiusStoreArgs: GeoRadiusStoreArgs[K]): F[Long]
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate): F[Set[V]]
  
  def geosearch(key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs): F[Seq[GeoWithin[V]]]
  
  def geosearchstore(destination: K, key: K, reference: GeoSearch.GeoRef[K], predicate: GeoSearch.GeoPredicate, geoArgs: GeoArgs, storeDist: Boolean): F[Long]
  
}
