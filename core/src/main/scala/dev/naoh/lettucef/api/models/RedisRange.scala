package dev.naoh.lettucef.api.models

import io.lettuce.core.{Range => LRange}
import RedisRange.Boundary.Exclusive
import RedisRange.Boundary.Inclusive
import RedisRange.Boundary.Unbounded
import RedisRange._


case class RedisRange[A](from: Boundary[A], to: Boundary[A]) {
  def toJavaNumber(implicit ev: A => java.lang.Number): LRange[java.lang.Number] =
    LRange.from(from.toJava, to.toJava)

  def toJava: LRange[A] =
    LRange.from(from.toJava, to.toJava)
}

object RedisRange {
  val unbounded: RedisRange[Nothing] = RedisRange[Nothing](Unbounded, Unbounded)

  def between[A](from: A, to: A): RedisRange[A] = RedisRange(Inclusive(from), Inclusive(to))

  def inclusive[A](from: A): Inclusive[A] = Inclusive(from)

  def exclusive[A](from: A): Exclusive[A] = Exclusive(from)

  sealed trait Boundary[+A] {
    def ~[B >: A](rhs: Boundary[B]): RedisRange[B] = RedisRange(this, rhs)

    def openEnd[B >: A]: RedisRange[B] = RedisRange(this, Unbounded)

    def openStart[B >: A]: RedisRange[B] = RedisRange(Unbounded, this)

    def to[B >: A](rhs: B): RedisRange[B] = this ~ Inclusive(rhs)

    def until[B >: A](rhs: B): RedisRange[B] = this ~ Exclusive(rhs)

    def toJava[R](implicit ev: A => R): LRange.Boundary[R]
  }

  object Boundary {
    case object Unbounded extends Boundary[Nothing] {
      def toJava[R](implicit ev: Nothing => R): LRange.Boundary[R] =
        LRange.Boundary.unbounded()
    }

    final case class Inclusive[A](a: A) extends Boundary[A] {
      def toJava[R](implicit ev: A => R): LRange.Boundary[R] =
        LRange.Boundary.including(ev(a))
    }

    final case class Exclusive[A](a: A) extends Boundary[A] {
      def toJava[R](implicit ev: A => R): LRange.Boundary[R] =
        LRange.Boundary.excluding(ev(a))
    }
  }
}
