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
}

object TaftBuild extends Build {
  lazy val sbtAssemblySettings = assemblySettings ++ Seq(
    // Slightly cleaner jar name
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

      libraryDependencies ++= Seq("com.typesafe.akka" % "akka-actor" % "2.0.1",
        // Against Scalaz 7.0.6, available for Scala 2.10.4 and 2.11.2
        "org.scalaz.stream" %% "scalaz-stream" % "0.5",
        // Against Scalaz 7.1, available for Scala 2.10.4 and 2.11.2
        "org.scalaz.stream" %% "scalaz-stream" % "0.5a",
        // AWS
        "com.github.seratch" %% "awscala" % "0.4.+")
    )
  )
}
