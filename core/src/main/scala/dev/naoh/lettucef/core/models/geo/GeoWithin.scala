package dev.naoh.lettucef.core.models.geo

import io.lettuce.core.GeoCoordinates
import io.lettuce.core.GeoValue

case class GeoWithin[V](
  member: V,
  distance: Option[Double],
  geohash: Option[Long],
  geoCoordinates: Option[GeoCoordinates]
) {
  def toValue: Option[GeoValue[V]] =
    geoCoordinates.map(c => GeoValue.just(c, member))
}

object GeoWithin {
  def from[V](j: io.lettuce.core.GeoWithin[V]): GeoWithin[V] =
    GeoWithin(
      j.getMember,
      Option(j.getDistance),
      Option(j.getGeohash),
      Option(j.getCoordinates))
}
