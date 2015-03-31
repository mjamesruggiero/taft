import sbt._
import sbt.Keys._
import com.github.bigtoast.sbtthrift.ThriftPlugin
import sbtassembly.Plugin._
import AssemblyKeys._

object Dependencies {
  val resolutionRepos = Seq(
    "Snowplow Analytics Maven repo" at "http://maven.snplow.com/releases/",
    "BintrayJCenter" at "http://jcenter.bintray.com",
    "bigtoast-github" at "http://bigtoast.github.com/repo/",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  )

  object Versions {
    // Java
    val logging    = "1.1.3"
    val httpCore   = "4.3"
    val httpClient = "4.3.1"
    val jacksonCore = "2.3.0"
    val slf4j      = "1.7.5"
    val awsSdk     = "1.6.10"

    // Scala
    val scalacheck = "1.12.2"
    val argot      = "1.0.1"
    val config     = "1.0.2"
    val scalaUtil  = "0.1.0"
    val scalazon   = "0.5"
    val scalaz = "0.5"

    // Scala compile only for sbt-thrift.
    val commonsLang3 = "3.1"
    val thrift = "0.9.0"
  }

  object Libraries {
    // Java
    val logging     = "commons-logging"            %  "commons-logging" % Versions.logging
    val httpCore    = "org.apache.httpcomponents"  %  "httpcore"        % Versions.httpCore
    val httpClient  = "org.apache.httpcomponents"  %  "httpclient"      % Versions.httpClient
    val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core"     % Versions.jacksonCore
    val slf4j       = "org.slf4j"                  % "slf4j-simple"     % Versions.slf4j
    val awsSdk      = "com.amazonaws"              % "aws-java-sdk"     % Versions.awsSdk

    // Scala
    val scalacheck  = "org.scalacheck"             %% "scalacheck"      % Versions.scalacheck
    val argot       = "org.clapper"                %% "argot"           % Versions.argot
    val config      = "com.typesafe"               %  "config"          % Versions.config
    val scalaUtil   = "com.snowplowanalytics"      %  "scala-util"      % Versions.scalaUtil
    val scalazon    = "io.github.cloudify"         %% "scalazon"        % Versions.scalazon
    val scalaz      = "org.scalaz.stream"          %% "scalaz-stream"   % Versions.scalaz

    // Scala compile only for sbt-thrift.
    val commonsLang3 = "org.apache.commons" % "commons-lang3" % Versions.commonsLang3 % "compile"
    val thrift =    "org.apache.thrift"     % "libthrift" % Versions.thrift % "compile"
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
        Libraries.jacksonCore,
        Libraries.argot,
        Libraries.config,
        Libraries.scalaUtil,
        Libraries.scalazon,
        Libraries.commonsLang3,
        Libraries.thrift,
        Libraries.slf4j,
        Libraries.awsSdk,
        Libraries.scalaz,
        Libraries.scalacheck
      )
    )
  )
}