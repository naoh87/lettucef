package io.lettucef

import cats.data.NonEmptyList
import io.lettucef.Method.Argument
import io.lettucef.Method.TypeExpr
import io.lettucef.Method.TypeName

object MethodParser {

  import cats.parse.Parser0
  import cats.parse.Rfc5234
  import cats.parse.{Parser => P}

  val name: P[String] = Rfc5234.alpha.rep.map(_.toList.mkString)

  val dot: P[String] = P.char('.').as(".")

  val tpeName: P[TypeName] =
    (name.repSep(dot).map(_.toList.mkString(".")) ~ P.string("[]").as(true).orElse(P.pure(false))).map((TypeName.apply _).tupled)

  val whitespaces: Parser0[Unit] = Rfc5234.sp.rep.void

  val whitespaces0: Parser0[Unit] = Rfc5234.sp.rep0.void

  val listSep: P[Unit] =
    P.char(',').soft.surroundedBy(whitespaces0).void

  def rep[A](pa: P[A]): Parser0[NonEmptyList[A]] =
    pa.repSep(listSep).surroundedBy(whitespaces0)

  def rep0[A](pa: P[A]): Parser0[List[A]] =
    pa.repSep0(listSep).surroundedBy(whitespaces0)

  val tpeExpr: P[TypeExpr] = P.recursive[TypeExpr] {
    recurse =>
      val generics = rep(recurse).between(P.char('<'), P.char('>')).map(_.toList) | P.unit.as(List.empty)
      val covar = P.string("? extends ").as(Option("_")).orElse(P.pure(None))
      (covar.with1 ~tpeName ~ generics).map {
        case ((covar, name), gen) => TypeExpr(name, gen, covar)
      }
  }
  val arg: P[Argument] =
    (tpeExpr ~ P.string("...").as(true).orElse(P.unit.as(false)) ~ (whitespaces *> name)).map {
      case ((tpe, isVar), name) => Argument(name, tpe, isVar)
    }

  val args: P[List[Argument]] =
    rep0(arg).with1.between(P.char('('), P.char(')'))

  val meth: P[Method] =
    ((((tpeExpr <* whitespaces) ~ name) ~ args) <* (P.char(';') ~ P.end)).map {
      case ((output, name), args) =>
        Method(name, args, output)
    }

}
