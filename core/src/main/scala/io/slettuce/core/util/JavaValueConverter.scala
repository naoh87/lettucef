package io.slettuce.core.util

import cats.effect.kernel.Async
import scala.jdk.CollectionConverters._

object JavaValueConverter {
  implicit class OptionalSyntax[F[_], A](val fa: F[A]) extends AnyVal {
    def asOpt(implicit F: Async[F]): F[Option[A]] =
      F.map(fa)(a => Option(a))
  }

  implicit class LongSyntax[F[_]](val fa: F[java.lang.Long]) extends AnyVal {
    def asLong(implicit F: Async[F]): F[Long] =
      F.map(fa)(Long2long)
  }

  implicit class DoubleSyntax[F[_]](val fa: F[java.lang.Double]) extends AnyVal {
    def asDouble(implicit F: Async[F]): F[Double] =
      F.map(fa)(Double2double)
  }

  implicit class BooleanSyntax[F[_]](val fa: F[java.lang.Boolean]) extends AnyVal {
    def asBoolean(implicit F: Async[F]): F[Boolean] =
      F.map(fa)(Boolean2boolean)
  }


  implicit class List[F[_], A](val fa: F[java.util.List[A]]) extends AnyVal {
    def asList(implicit F: Async[F]): F[Seq[A]] =
      F.map(fa)(a => a.asScala.toSeq)

    def asListOf[R](f: A => R)(implicit F: Async[F]): F[Seq[R]] =
      F.map(fa)(a => a.asScala.toSeq.map(f))
  }
}
