import sbt._
import sbt.Keys._

object TaftBuild extends Build {

  lazy val taft = Project(
    id = "taft",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "taft",
      organization := "sharethrough",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1"
    )
  )
}
