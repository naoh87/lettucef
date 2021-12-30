package io.lettucef

import io.circe.Decoder
import io.lettucef.Method.Argument
import io.lettucef.Method.Identifier
import io.lettucef.Method.TypeExpr

case class Method(name: String, args: List[Argument], output: TypeExpr, checkNull: Boolean = false) extends ToScalaCode {
  def scalaDef: String =
    s"def ${Identifier.expr(name)}(${args.map(_.scalaDef).mkString(", ")}): ${output.scalaDef}"

  def asyncCall(expr: Seq[Argument], to: TypeExpr): String = {
    assert(expr.size == args.size)
    val call = args.zip(expr).map(ae => ae._1.call(ae._2)).mkString(", ")
    val postFix = {
      val mapF = javaToScalaF(to.generics.head) match {
        case Some(fun) => s".map($fun)"
        case None => ""
      }
      if (checkNull) {
        s".map(Option(_)$mapF)"
      } else {
        mapF
      }
    }
    s"JF.toAsync(underlying.${Identifier.expr(name)}($call))$postFix"
  }

  private def javaToScalaF(out: TypeExpr): Option[String] = {
    val anyVals = Set("Boolean", "Int", "Long", "Double")
    val skip = Set("Array", "K", "V", "String")

    val customConvert = Map("RedisData" -> "RedisData.from[V]")
    val supported =
      Seq("ClaimedMessages", "StreamMessage", "PendingMessage", "PendingMessages")
        .map(e => e -> s"$e.from").toMap ++ customConvert

    out.name.expr match {
      case expr if anyVals(expr) => Some(s"${expr}2${expr.toLowerCase}")
      case expr if supported.contains(expr) => supported.get(expr)
      case "Seq" =>
        Some(javaToScalaF(out.p1) match {
          case Some(fun) => s"_.asScala.toSeq.map($fun)"
          case None => "_.asScala.toSeq"
        })
      case "Set" =>
        Some(javaToScalaF(out.p1) match {
          case Some(fun) => s"_.asScala.toSet.map($fun)"
          case None => "_.asScala.toSet"
        })
      case "Map" =>
        Some {
          val mod = List(
            javaToScalaF(out.p1).map(f => s".mapKeys($f)"),
            javaToScalaF(out.p2).map(f => s".mapValues($f)")).flatten
          if (mod.isEmpty) {
            "_.asScala.toMap"
          } else {
            "_.asScala" + mod.mkString(".view", "", ".toMap")
          }
        }
      case expr if skip(expr) => None
      case other => //sys.error(s"j2s conversion failure $other")
        println(s"unregistered j2s $other")
        None
    }
  }

  def mapArgs(f: Argument => Argument): Method =
    copy(args = args.map(f))

  def existArgs(f: TypeExpr => Boolean): Boolean =
    args.exists(a => f(a.tpe))

  def mapOutput(f: TypeExpr => TypeExpr): Method =
    copy(output = f(output))

  def toAsync: Method = {
    if (checkNull) {
      copy(output =
        output
          .copy(generics =
            output.generics.map(t => TypeExpr.one("Option").copy(generics = t :: Nil)))
          .toAsync)
    } else {
      copy(output = output.toAsync)
    }
  }
}

trait ToScalaCode {
  def scalaDef: String
}

object JavaAnyVal {
  //  val anyvals = Seq("bype", "boolean", "double", "long", "int")
}

object Method {
  case class TypeExpr(name: TypeName, generics: List[TypeExpr], covar: Option[String]) extends ToScalaCode {
    def p1: TypeExpr = generics.head

    def p2: TypeExpr = generics(1)

    def scalaDef: String = {
      val postFix = if (generics.isEmpty) "" else generics.map(_.scalaDef).mkString("[", ", ", "]")
      val preFix = covar.fold("")(n => s"$n <: ")
      s"$preFix${name.scalaDef}$postFix"
    }

    def toScala: TypeExpr = {
      if (name.isArray) {
        TypeExpr(TypeName("Array"), List(TypeExpr(TypeName(name.expr), generics, None).toScala), covar)
      } else {
        name.expr match {
          case "byte" => TypeExpr.one("Byte")
          case "boolean" => TypeExpr.one("Boolean")
          case "double" => TypeExpr.one("Double")
          case "long" => TypeExpr.one("Long")
          case "int" => TypeExpr.one("Int")
          case "List" => TypeExpr(TypeName("Seq"), generics.map(_.toScala), covar)
          case "Object" if generics.isEmpty => TypeExpr(TypeName("RedisData"), List(TypeExpr.one("V")), covar)
          case _ => copy(generics = generics.map(_.toScala))
        }
      }
    }

    def existName(f: String => Boolean): Boolean =
      f(name.expr) || generics.exists(_.existName(f))

    def toAsync: TypeExpr = {
      assert(generics.size == 1)
      assert(name.expr == "RedisFuture")
      copy(name = TypeName("F"))
    }

    def nameList: Seq[String] =
      name.expr :: generics.flatMap(_.nameList)
  }

  object TypeExpr {
    def one(name: String): TypeExpr = TypeExpr(TypeName(name), List.empty, None)
  }

  case class Argument(name: String, tpe: TypeExpr, isVariable: Boolean) extends ToScalaCode {
    def scalaDef: String =
      s"$name: ${tpe.scalaDef}${if (isVariable) "*" else ""}"

    def call(arg: Argument): String =
      if (isVariable) {
        s"${arg.name}: _*"
      } else if (Set("List", "Map")(tpe.name.expr)) {
        s"${arg.name}.asJava"
      } else {
        arg.name
      }

    def toScala: Argument = {
      val convert = Map("type" -> "tpe")
      copy(name = convert.getOrElse(name, name), tpe = tpe.toScala)
    }
  }

  case class TypeName(expr: String, isArray: Boolean = false) extends ToScalaCode {
    def scalaDef: String =
      if (isArray) {
        s"Array[$expr]"
      } else {
        expr
      }
  }

  object Identifier {
    val Reserved = Set("type")

    def expr(expr: String): String =
      if (Reserved(expr)) {
        s"`$expr`"
      } else {
        expr
      }
  }

  implicit val decoder: Decoder[Method] =
    Decoder[String].emap(e => MethodParser.meth.parseAll(e).left.map(_ => s"fail to parse as Method $e"))
}