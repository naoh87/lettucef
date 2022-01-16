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

    asyncList.map { config =>

      val syncDir = Paths.get(s"../core/src/main/scala/dev/naoh/lettucef/core/sync/${config.output}.scala").toAbsolutePath
      val sync = FunctionalPrinter()
        .add("// Code generated. DO NOT EDIT")
        .add("package dev.naoh.lettucef.core.sync")
        .newline
        .add(config.imports.map(expr => s"import $expr"): _*)
        .newline.newline
        .add(s"trait ${config.output}[F[_], K, V] extends CommandsDeps[F, K, V] {")
        .newline
        .indented(_
          .add(s"protected val underlying: ${config.underlying}").newline
          .print(config.methods)((p, m) => m.printSync(p)))
        .add("}")
        .newline
        .pipe(print(syncDir, _))

      val asyncDir = Paths.get(s"../core/src/main/scala/dev/naoh/lettucef/core/async/${config.output}.scala").toAbsolutePath
      val async = FunctionalPrinter()
        .add("// Code generated. DO NOT EDIT")
        .add("package dev.naoh.lettucef.core.async")
        .newline
        .add(config.imports.map(expr => s"import $expr"): _*)
        .newline.newline
        .add(s"trait ${config.output}[F[_], K, V] extends CommandsDeps[F, K, V] {")
        .newline
        .indented(_
          .add(s"protected val underlying: ${config.underlying}").newline
          .print(config.methods)((p, m) => m.printAsync(p)))
        .add("}")
        .newline
        .pipe(print(asyncDir, _))

      sync >> async
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

  def refineUnderlying: String = {
    val dispatchHolder = "BaseRedisAsyncCommands[K, V]"
    if (methods.exists(_.dispatch.isDefined) && !underlying.contains(dispatchHolder)) {
      s"$underlying with $dispatchHolder"
    } else {
      underlying
    }
  }

  def refine(): Async =
    copy(
      underlying = refineUnderlying,
      imports = (imports ++ Async.constantImports ++ methods.flatMap(_.imports)).distinct.sortBy {
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
    output: Option[CustomOutput],
    dispatch: Option[Dispatch]
  ) {
    def imports: List[String] = dispatch.toList.flatMap(_.imports)

    val options: Seq[String] = opt.toList.flatten

    val skipArgType = Set("Object", "Date", "ValueStreamingChannel", "KeyStreamingChannel", "KeyValueStreamingChannel", "ScoredValueStreamingChannel")

    def isOutputTarget: Boolean = options.forall(!Set("ignore", "deprecated")(_))

    def refine(): FunDef =
      if (options.contains("nullable")) {
        copy(fun = fun.copy(checkNull = true))
      } else {
        this
      }


    private def convertOut(tpe: TypeExpr): TypeExpr =
      dispatch.map(_.replace(tpe))
        .orElse(output.map(_.replace(tpe)))
        .getOrElse(tpe.toScala(Nil, options.contains("nullable")))

    private lazy val scalaDef =
      fun.mapOutput(convertOut).mapArgs(_.toScala)

    def printSync(p: FunctionalPrinter): FunctionalPrinter =
      if (isOutputTarget && !fun.existArgs(_.existName(skipArgType))) {
        p.add(scalaDef.toSync.scalaDef + " =")
          .indented(
            _.add(fun.syncCall(scalaDef.args, scalaDef.output, dispatch, output.map(_.j2s))))
          .newline
      } else {
        println(s"- skipped ${fun.scalaDef}")
        p
      }

    def printAsync(p: FunctionalPrinter): FunctionalPrinter =
      if (isOutputTarget && !fun.existArgs(_.existName(skipArgType))) {

        p.add(scalaDef.toAsync.scalaDef + " =")
          .indented(
            _.add(fun.asyncCall(scalaDef.args, scalaDef.output, dispatch, output.map(_.j2s))))
          .newline
      } else {
        println(s"- skipped ${fun.scalaDef}")
        p
      }
  }

  case class Dispatch(
    cType: String,
    args: Option[List[String]],
    parse: String,
    output: TypeExpr,
    postfix: Option[String]
  ) {
    def replace(rf: Method.TypeExpr): Method.TypeExpr =
      rf.copy(generics = output :: Nil)

    def call: String = s"CommandType.$cType, dispatchHelper.createRedisDataOutput()" + args.map(_.mkString(", dispatchHelper.createArgs().", ".", "")).getOrElse("")

    def meth: String = "dispatch"

    def pfix: String = postfix.getOrElse("")

    def imports: List[String] =
      List(
        "io.lettuce.core.protocol.CommandKeyword",
        "io.lettuce.core.protocol.CommandType")
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
    "dev.naoh.lettucef.core.commands.CommandsDeps",
    "dev.naoh.lettucef.core.util.LettuceValueConverter",
    "dev.naoh.lettucef.core.util.{JavaFutureUtil => JF}",
    "scala.jdk.CollectionConverters._",
  )
}



