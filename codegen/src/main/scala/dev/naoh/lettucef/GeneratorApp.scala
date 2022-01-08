package dev.naoh.lettucef

import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import dev.naoh.lettucef.Method.TypeExpr

object GeneratorApp extends IOApp {

  import cats.syntax.traverse._
  import io.circe.generic.auto._
  import scala.util.chaining._


  override def run(args: List[String]): IO[ExitCode] = IO.defer {
    val Right(asyncList) =
      io.circe.yaml.parser
        .parse(
          scala.io.Source.fromResource("async.yaml").mkString)
        .flatMap(_.as[List[Async]])
        .map(_.map(_.refine()))

    var outputMeth = 0

    asyncList.map { async =>
      val outputDir = Paths.get(s"../core/src/main/scala/dev/naoh/lettucef/core/commands/${async.output}.scala").toAbsolutePath

      FunctionalPrinter()
        .add("// Code generated. DO NOT EDIT")
        .add("package dev.naoh.lettucef.core.commands")
        .newline
        .add(async.imports.map(expr => s"import $expr"): _*)
        .newline.newline
        .add(s"trait ${async.output}[F[_], K, V] extends AsyncCallCommands[F, K, V] {")
        .newline
        .indented(_
          .add(s"protected val underlying: ${async.underlying}").newline
          .print(async.methods)((p, m) => m.print(p)))
        .add("}")
        .newline
        .pipe(print(outputDir, _))
    }.sequence >> IO.delay {
      ExitCode.Success
    }
  }


  def print(path: java.nio.file.Path, printer: FunctionalPrinter): IO[Unit] = IO.blocking {
    Files.write(path, printer.result().getBytes)
  }
}

case class Async(
  underlying: String,
  output: String,
  methods: List[Async.FunDef],
  imports: List[String],
) {

  def refine(): Async =
    copy(
      imports = (imports ++ Async.constantImports).distinct.sortBy {
        case name if name.startsWith("java.") => (0, name)
        case name if name.startsWith("scala.") => (2, name)
        case name => (1, name)
      },
      methods = methods.map(_.refine()).filter(_.isOutputTarget))
}

object Async {
  case class FunDef(
    fun: Method,
    opt: Option[List[String]],
    output: Option[CustomOutput]
  ) {
    val options: Seq[String] = opt.toList.flatten

    val skipArgType = Set("Object", "Date", "ValueStreamingChannel", "KeyStreamingChannel", "KeyValueStreamingChannel", "ScoredValueStreamingChannel")

    def isOutputTarget: Boolean = options.forall(!Set("ignore", "deprecated")(_))

    def refine(): FunDef =
      if (options.contains("nullable")) {
        copy(fun = fun.copy(checkNull = true))
      } else {
        this
      }

    def print(p: FunctionalPrinter): FunctionalPrinter =
      if (isOutputTarget && !fun.existArgs(_.existName(skipArgType))) {
        def convertOut(tpe: TypeExpr): TypeExpr =
          output.map(_.replace(tpe)).getOrElse(tpe.toScala(Nil, options.contains("nullable")))

        val scalaDef =
          fun.mapOutput(convertOut).mapArgs(_.toScala)
        p.add(scalaDef.toAsync.scalaDef + " =")
          .indented(
            _.add(fun.asyncCall(scalaDef.args, scalaDef.output, output.map(_.j2s))))
          .newline
      } else {
        println(s"- skipped ${fun.scalaDef}")
        p
      }

  }

  case class CustomOutput(
    tpe: Method.TypeExpr,
    j2s: String
  ) {
    def replace(rf: Method.TypeExpr): Method.TypeExpr =
      rf.copy(generics = tpe :: Nil)
  }

  val constantImports = List(
    "cats.syntax.functor._",
    "io.lettuce.core.api.async._",
    "dev.naoh.lettucef.core.util.LettuceValueConverter",
    "dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}",
    "scala.jdk.CollectionConverters._",
  )
}



