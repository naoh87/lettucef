package io.lettucef

import java.nio.file.Paths
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import fs2.Chunk
import fs2.io.file.Files
import fs2.io.file.Path

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
      val outputDir = Paths.get(s"core/src/main/scala/io/lettucef/core/commands/${async.output}.scala").toAbsolutePath

      val skipArgType = Set("Object", "Date", "ValueStreamingChannel", "KeyStreamingChannel", "KeyValueStreamingChannel", "ScoredValueStreamingChannel")

      FunctionalPrinter()
        .add("// Code generated. DO NOT EDIT")
        .add("package io.lettucef.core.commands")
        .newline
        .add(async.imports.map(expr => s"import $expr"): _*)
        .newline.newline
        .add(s"trait ${async.output}[F[_], K, V] extends AsyncCallCommands[F, K, V] {")
        .newline
        .indented(_
          .add(s"protected val underlying: ${async.underlying}").newline
          .print(async.methods) {
            case (p, m) if m.existArgs(_.existName(skipArgType)) =>
              println(s"- skipped ${m.scalaDef}")
              p
            case (p, m) =>
              outputMeth += 1
              val scalaDef = m.mapOutput(_.toScala()).mapArgs(_.toScala)
              p.add(scalaDef.toAsync.scalaDef + " =")
                .indented(
                  _.add(m.asyncCall(scalaDef.args, scalaDef.output)))
                .newline
          })
        .add("}")
        .newline
        .pipe(print(Path.fromNioPath(outputDir), _))
    }.sequence >> IO.delay {
      println(s"\ntotal method generation count: $outputMeth")
      ExitCode.Success
    }
  }


  def print(path: Path, printer: FunctionalPrinter): IO[Unit] = {
    val content = fs2.Stream.iterable(printer.content).map(s => Chunk.iterable(s.getBytes) ++ Chunk.singleton('\n'.toByte)).unchunks
    Files[IO].writeAll(path)(content).compile.drain
  }
}

case class Async(
  underlying: String,
  output: String,
  methods: List[Method],
  imports: List[String],
  nullable: Option[List[String]],
) {
  val nullMethodName: Set[String] = nullable.toList.flatten.toSet

  def refine(): Async =
    copy(
      imports = (imports ++ Async.constantImports).distinct.sortBy {
        case name if name.startsWith("java.") => (0, name)
        case name if name.startsWith("scala.") => (2, name)
        case name => (1, name)
      },
      methods = methods.map(m => m.copy(checkNull = nullMethodName(m.name))))
}

object Async {
  val constantImports = List(
    "cats.syntax.functor._",
    "io.lettuce.core.api.async._",
    "io.lettucef.core.util.LettuceValueConverter",
    "io.lettucef.core.util.{JavaFutureUtil => JF}",
    "scala.jdk.CollectionConverters._",
  )
}



