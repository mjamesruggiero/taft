import sbt._
import sbt.Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object Dependencies {
  val resolutionRepos = Seq(
    "Snowplow Analytics Maven repo" at "http://maven.snplow.com/releases/",
    "BintrayJCenter" at "http://jcenter.bintray.com",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    "scribe" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/",
    "spray" at "http://repo.spray.io/"
  )

  object Versions {
    // Java
    val logging    = "1.1.3"
    val httpCore   = "4.3"
    val httpClient = "4.3.1"
    val slf4j      = "1.7.5"
    val awsSdk     = "1.6.10"
    val scribe     = "1.3.6"

    // Scala
    val scalacheck = "1.12.2"
    val config     = "1.0.2"
    val scalaUtil  = "0.1.0"
    val scalazon   = "0.5"
    val scalaz     = "0.5"
    val scodec     = "1.0.5"
    val spray      = "1.3.2"
    val nscalatime = "1.8.0"
    val debasishg  = "3.0"
  }

  object Libraries {
    // Java
    val logging     = "commons-logging"            %  "commons-logging" % Versions.logging
    val httpCore    = "org.apache.httpcomponents"  %  "httpcore"        % Versions.httpCore
    val httpClient  = "org.apache.httpcomponents"  %  "httpclient"      % Versions.httpClient
    val slf4j       = "org.slf4j"                  % "slf4j-simple"     % Versions.slf4j
    val awsSdk      = "com.amazonaws"              % "aws-java-sdk"     % Versions.awsSdk
    val scribe      = "org.scribe"                 % "scribe"           % Versions.scribe

    // Scala
    val scalacheck  = "org.scalacheck"             %% "scalacheck"      % Versions.scalacheck
    val config      = "com.typesafe"               %  "config"          % Versions.config
    val scalaUtil   = "com.snowplowanalytics"      %  "scala-util"      % Versions.scalaUtil
    val scalazon    = "io.github.cloudify"         %% "scalazon"        % Versions.scalazon
    val scalaz      = "org.scalaz.stream"          %% "scalaz-stream"   % Versions.scalaz
    val scodec      = "org.scodec"                 %% "scodec-bits"     % Versions.scodec
    val spray       = "io.spray"                   %% "spray-json"      % Versions.spray
    val nscalatime  = "com.github.nscala-time"     %% "nscala-time"     % Versions.nscalatime
    val debasishg   = "net.debasishg"              %% "redisclient"     % Versions.debasishg
  }
}

object TaftBuild extends Build {
  import Dependencies._

  lazy val sbtAssemblySettings = assemblySettings ++ Seq(
    jarName in assembly := {
      name + "-" + version + ".jar"
    }
  )

  val ScalaVersion = "2.10.3"

  lazy val taft = Project(
    id = "taft",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "taft",
      organization := "com.mjamesruggiero",
      version := "0.1-SNAPSHOT",
      scalaVersion := ScalaVersion,

      resolvers ++= Dependencies.resolutionRepos,

      libraryDependencies ++= Seq(
        Libraries.logging,
        Libraries.httpCore,
        Libraries.httpClient,
        Libraries.config,
        Libraries.scalaUtil,
        Libraries.scalazon,
        Libraries.slf4j,
        Libraries.awsSdk,
        Libraries.scalaz,
        Libraries.scalacheck,
        Libraries.scribe,
        Libraries.scodec,
        Libraries.spray,
        Libraries.nscalatime,
        Libraries.debasishg
      )
    )
  )
}
