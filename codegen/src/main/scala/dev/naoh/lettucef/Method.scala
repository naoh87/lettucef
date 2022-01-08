package dev.naoh.lettucef

import io.circe.Decoder
import Method.Argument
import Method.Identifier
import Method.TypeExpr

case class Method(name: String, args: List[Argument], output: TypeExpr, checkNull: Boolean = false) extends ToScalaCode {
  def scalaDef: String =
    s"def ${Identifier.expr(name)}(${args.map(_.scalaDef).mkString(", ")}): ${output.scalaDef}"

  def asyncCall(expr: Seq[Argument], to: TypeExpr, customPostFix: Option[String]): String = {
    assert(expr.size == args.size)
    val call = args.zip(expr).map(ae => ae._1.call(ae._2)).mkString(", ")
    val postFix =
      customPostFix.map {
        case "" => ""
        case fun => s".map($fun)"
      }.getOrElse {
        val mapF = javaToScalaF(output.p1, to.p1) match {
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

  private def javaToScalaF(from: TypeExpr, to: TypeExpr): Option[String] = {
    val anyVals = Set("Boolean", "Int", "Long", "Double")
    val skip = Set("Array", "K", "V", "String")

    val valueConvert = Seq("RedisData", "TransactionResult").map(n => n -> s"$n.from[V]").toMap
    val supported =
      Seq("ClaimedMessages", "StreamMessage", "PendingMessage", "PendingMessages", "GeoWithin")
        .map(e => e -> s"$e.from").toMap ++ valueConvert

    to.name.expr match {
      case expr if anyVals(expr) => Some(s"${expr}2${expr.toLowerCase}")
      case expr if supported.contains(expr) => supported.get(expr)
      case "Seq" =>
        Some(javaToScalaF(from.p1, to.p1) match {
          case Some(fun) => s"_.asScala.toSeq.map($fun)"
          case None => "_.asScala.toSeq"
        })
      case "Set" =>
        Some(javaToScalaF(from.p1, to.p1) match {
          case Some(fun) => s"_.asScala.toSet.map($fun)"
          case None => "_.asScala.toSet"
        })
      case "Map" =>
        Some {
          val mod = List(
            javaToScalaF(from.p1, to.p1).map(f => s".mapKeys($f)"),
            javaToScalaF(from.p1, to.p2).map(f => s".mapValues($f)")).flatten
          if (mod.isEmpty) {
            "_.asScala.toMap"
          } else {
            "_.asScala" + mod.mkString(".view", "", ".toMap")
          }
        }
      case expr if skip(expr) => None
      case _ =>
        from.name.expr match {
          case "Value" =>
            val postfix = javaToScalaF(from.p1, to.p1).map(fun => s".map($fun)").getOrElse("")
            Some {
              s"v => LettuceValueConverter.fromValue(v)$postfix"
            }
          case "KeyValue" =>
            to.p2.name.expr match {
              case "Option" =>
                val postfix = javaToScalaF(from.p2, to.p2.p1).map(fun => s".fmap(_.map($fun))").getOrElse("")
                Some {
                  s"kv => LettuceValueConverter.fromKeyValue(kv)$postfix"
                }
              case _ =>
                val postfix = javaToScalaF(from.p2, to.p2).map(fun => s".fmap($fun)").getOrElse("")
                Some {
                  s"kv => LettuceValueConverter.fromKeyValueUnsafe(kv)$postfix"
                }
            }
          case "ScoredValue" =>
            to.name.expr match {
              case "Tuple2" =>
                Some {
                  "LettuceValueConverter.fromScoredValueUnsafe"
                }
              case "Option" =>
                Some {
                  "LettuceValueConverter.fromScoredValue"
                }
            }
          case "ValueScanCursor" | "MapScanCursor" | "KeyScanCursor" | "ScoredValueScanCursor" =>
            Some("cur => DataScanCursor.from(cur)")
          case _ =>
            println(s"unregistered j2s ${from.scalaDef} => ${to.scalaDef}")
            None
        }
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

object JavaTypes {
  val anyvals = Seq("bype", "boolean", "double", "long", "int")
}

object Method {
  case class TypeExpr(name: TypeName, generics: List[TypeExpr], covar: Option[String]) extends ToScalaCode {
    def p1: TypeExpr = generics.head

    def p2: TypeExpr = generics(1)

    def scalaDef: String = {
      name.expr match {
        case "Tuple2" => generics.map(_.scalaDef).mkString("(", ", ", ")")
        case _ =>
          val postFix = if (generics.isEmpty) "" else generics.map(_.scalaDef).mkString("[", ", ", "]")
          val preFix = covar.fold("")(n => s"$n <: ")
          s"$preFix${name.scalaDef}$postFix"
      }
    }

    def toScala(parent: List[TypeName] = Nil, checkNull: Boolean = false): TypeExpr = {
      if (name.isArray) {
        TypeExpr(TypeName("Array"), List(TypeExpr(TypeName(name.expr), generics, None).toScala(parent)), covar)
      } else {
        name.expr match {
          case "byte" => TypeExpr.one("Byte")
          case "boolean" => TypeExpr.one("Boolean")
          case "double" => TypeExpr.one("Double")
          case "long" => TypeExpr.one("Long")
          case "int" => TypeExpr.one("Int")
          case "List" => TypeExpr(TypeName("Seq"), generics.map(_.toScala(name :: parent)), covar)
          case "Object" if generics.isEmpty =>
            TypeExpr(TypeName("RedisData"), List(TypeExpr.one("V")), covar)
          case "TransactionResult" if generics.isEmpty =>
            TypeExpr(TypeName("TransactionResult"), List(TypeExpr.one("V")), covar)
          case "Range" =>
            val converted = p1.name.expr match {
              case "Number" => TypeName("Double")
              case other => p1.name
            }
            TypeExpr.create("RedisRange", p1.copy(name = converted, covar = None) :: Nil)
          case "Value" =>
            TypeExpr.create("Option", p1.toScala(name :: parent) :: Nil)
          case "KeyValue" =>
            if (checkNull) {
              TypeExpr.create("Tuple2", p1 :: p2.toScala(name :: parent) :: Nil)
            } else {
              TypeExpr.create("Tuple2", p1 :: TypeExpr.create("Option", p2.toScala(name :: parent) :: Nil) :: Nil)
            }
          case "ScoredValue" =>
            val tpe = TypeExpr.create("Tuple2", TypeExpr.one("Double") :: p1.toScala(name :: parent) :: Nil)
            if (parent.headOption.exists(_.expr == "RedisFuture")) {
              TypeExpr.create("Option", tpe :: Nil)
            } else {
              tpe
            }
          case "ValueScanCursor" | "KeyScanCursor" =>
            TypeExpr.create("DataScanCursor", p1 :: Nil)
          case "MapScanCursor" =>
            TypeExpr.create("DataScanCursor", TypeExpr.tuple(generics) :: Nil)
          case "ScoredValueScanCursor" =>
            TypeExpr.create("DataScanCursor", TypeExpr.tuple(TypeExpr.one("Double") :: p1 :: Nil) :: Nil)
          case "RedisFuture" =>
            copy(generics = p1.toScala(name :: parent, checkNull) :: Nil)
          case _ => copy(generics = generics.map(_.toScala(name :: parent)))
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

    def create(name: String, gen: List[TypeExpr] = Nil): TypeExpr =
      TypeExpr(TypeName(name), gen, None)

    def tuple(ts: List[TypeExpr]): TypeExpr =
      create(s"Tuple${ts.size}", ts)

    implicit val decoder: Decoder[TypeExpr] =
      Decoder[String].emap(e => MethodParser.tpeExpr.parseAll(e).left.map(_ => s"fail to parse as TypeExpr $e"))
  }

  case class Argument(name: String, tpe: TypeExpr, isVariable: Boolean) extends ToScalaCode {
    def scalaDef: String =
      s"$name: ${tpe.scalaDef}${if (isVariable) "*" else ""}"

    def call(arg: Argument): String =
      if (isVariable) {
        tpe.scalaDef match {
          case fun if fun == arg.tpe.scalaDef =>
            s"${arg.name}: _*"
          case _ if tpe.name.expr == "ScoredValue" =>
            s"${arg.name}.map(LettuceValueConverter.toScoredValue): _*"
          case _ =>
            sys.error(s"fail to call (${arg.scalaDef}) => (${this.scalaDef})")
        }
      } else if (Set("List", "Map")(tpe.name.expr)) {
        s"${arg.name}.asJava"
      } else {
        arg.tpe.name.expr match {
          case "RedisRange" =>
            this.tpe.p1.name.expr match {
              case "Number" => s"${arg.name}.toJavaNumber"
              case _ => s"${arg.name}.toJava"
            }
          case _ =>
            assert(this.tpe.scalaDef == arg.tpe.scalaDef, s"call failure (${this.scalaDef})(${arg.scalaDef})")
            arg.name
        }
      }

    def toScala: Argument = {
      val convert = Map("type" -> "tpe")
      copy(name = convert.getOrElse(name, name), tpe = tpe.toScala())
    }
  }

  case class TypeName(expr: String, isArray: Boolean = false) extends ToScalaCode {
    def scalaDef: String = {
      if (isArray) {
        s"Array[${expr.capitalize}]"
      } else {
        expr.capitalize
      }
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