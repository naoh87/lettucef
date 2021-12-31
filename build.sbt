ThisBuild / version := "SNAPSHOT"

val scala213 = "2.13.7"
val scala310 = "3.1.0"

lazy val root = (project in file("."))
  .settings(crossScalaVersions := Nil)
  .aggregate(core)

lazy val core = (project in file("core")).settings(
  name := "lettucef-core",
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala213, scala310),
  test / fork := true,
  libraryDependencies ++= Seq(
    "io.lettuce" % "lettuce-core" % "6.1.5.RELEASE",
    "org.typelevel" %% "cats-effect" % "3.3.1",
    "co.fs2" %% "fs2-core" % "3.2.4",
    "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  )
)

val circeVersion = "0.14.1"
lazy val codegen = (project in file("codegen")).settings(
  name := "lettucef-codegen",
  scalaVersion := scala213,
  run / fork := true,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.3.1",
    "org.typelevel" %% "cats-parse" % "0.3.6",
    "io.circe" %% "circe-yaml" % "0.14.1",
    "co.fs2" %% "fs2-core" % "3.2.4",
    "co.fs2" %% "fs2-io" % "3.2.4",
  ),
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
)