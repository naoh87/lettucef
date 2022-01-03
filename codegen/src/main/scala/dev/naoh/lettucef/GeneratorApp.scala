package dev.naoh.lettucef

import java.io.FileWriter
import java.nio.file.Paths
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

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
      println("-" * 32 + "\n" + async.underlying)
      val outputDir = Paths.get(s"../core/src/main/scala/dev/naoh/lettucef/core/commands/${async.output}.scala").toAbsolutePath

      val skipArgType = Set("Object", "Date", "ValueStreamingChannel", "KeyStreamingChannel", "KeyValueStreamingChannel", "ScoredValueStreamingChannel")

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
          .print(async.methods.map(_.fun)) {
            case (p, m) if m.existArgs(_.existName(skipArgType)) =>
              println(s"- skipped ${m.scalaDef}")
              p
            case (p, m) =>
              outputMeth += 1
              val scalaDef = m.mapOutput(_.toScala(Nil, m.checkNull)).mapArgs(_.toScala)
              p.add(scalaDef.toAsync.scalaDef + " =")
                .indented(
                  _.add(m.asyncCall(scalaDef.args, scalaDef.output)))
                .newline
          })
        .add("}")
        .newline
        .pipe(print(outputDir.toFile, _))
    }.sequence >> IO.delay {
      println(s"\ntotal method generation count: $outputMeth")
      ExitCode.Success
    }
  }


  def print(path: java.io.File, printer: FunctionalPrinter): IO[Unit] = IO.blocking {
    val fw = new FileWriter(path)
    try {
      printer.content.foreach { line =>
        fw.write(line)
        fw.write('\n')
      }
      fw.flush()
    } finally {
      fw.close()
    }
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
  ) {
    val options: Seq[String] = opt.toList.flatten

    def isOutputTarget: Boolean = options.forall(!Set("ignore", "deprecated")(_))

    def refine(): FunDef =
      if (options.contains("nullable")) {
        copy(fun = fun.copy(checkNull = true))
      } else {
        this
      }
  }

  val constantImports = List(
    "cats.syntax.functor._",
    "io.lettuce.core.api.async._",
    "dev.naoh.lettucef.core.util.LettuceValueConverter",
    "dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}",
    "scala.jdk.CollectionConverters._",
  )
}



