val scala213 = "2.13.11"
val scala33 = "3.3.0"


def dev(ghUser: String, name: String, email: String): Developer =
  Developer(ghUser, name, email, url(s"https://github.com/$ghUser"))

ThisBuild / licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / organization := "dev.naoh"
ThisBuild / homepage := Some(url("https://github.com/naoh87/lettucef"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/naoh87/lettucef"), "scm:git@github.com:naoh87/lettucef.git"))
ThisBuild / versionScheme := Some("semver-spec")
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
ThisBuild / developers := List(dev("naoh87", "naoh", "naoh87@gmail.coma"))
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

import ReleaseTransformations._
releaseCrossBuild := true // true if you cross-build the project for multiple Scala versions
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  releaseStepCommandAndRemaining("codegen/run"),
  runClean,
  releaseStepCommandAndRemaining("+compile"),
  releaseStepCommandAndRemaining("+test"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
//  pushChanges
)

lazy val root = (project in file("."))
  .settings(
    name := "LettuceF",
    scalaVersion := scala213,
    crossScalaVersions := Nil,
    publishArtifact := false
  )
  .aggregate(core, streams, extras)

lazy val core = (project in file("core"))
  .settings(name := "lettucef-core")
  .settings(core_dependency)
  .settings(publishSetting)
  .settings(common_settings)

lazy val streams = (project in file("streams"))
  .settings(name := "lettucef-streams")
  .settings(core_dependency)
  .settings(publishSetting)
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % "3.7.0",
    ),
  )
  .dependsOn(core)

lazy val extras = (project in file("extras"))
  .settings(name := "lettucef-extras")
  .settings(publishSetting)
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.5.7",
    )
  )

lazy val examples =
  (project in file("examples"))
    .settings(
      name := "examples",
      scalaVersion := scala213,
      run / fork := true
    )
    .dependsOn(streams, extras)

import pl.project13.scala.sbt.JmhPlugin

lazy val benchmark = (project in file("benchmark"))
  .settings(name := "benchmark")
  .enablePlugins(JmhPlugin)
  .dependsOn(streams)
  .settings(
    scalaVersion := scala213,
    Test / fork := true,
    run / fork := true,
    libraryDependencies ++= Seq(
      "dev.profunktor" %% "redis4cats-effects" % "1.0.0",
      "net.debasishg" %% "redisclient" % "3.41",
    )
  )

val core_dependency = Seq(
  libraryDependencies ++= Seq(
    "io.lettuce" % "lettuce-core" % "6.2.4.RELEASE",
    "org.typelevel" %% "cats-effect" % "3.4.8",
    "org.scalatest" %% "scalatest" % "3.2.14" % "test",
  ),
)

val common_settings = Seq(
  Test / fork := true,
  run / fork := true,
  scalafixOnCompile := scalaVersion.value == scala213,
  Compile / scalacOptions ++= (if (scalaVersion.value == scala213) Seq(
    "-Ywarn-unused"
  ) else Seq(

  ))
)

val publishSetting = Seq(
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala213, scala33),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  publishTo := sonatypePublishToBundle.value
)

val circeVersion = "0.14.1"
lazy val codegen = (project in file("codegen")).settings(
  name := "lettucef-codegen",
  scalaVersion := scala213,
  run / fork := true,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.4.8",
    "org.typelevel" %% "cats-parse" % "0.3.9",
    "io.circe" %% "circe-yaml" % "0.14.1",
    "co.fs2" %% "fs2-core" % "3.4.0",
    "co.fs2" %% "fs2-io" % "3.3.0",
  ),
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
)
