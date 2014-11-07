import sbt._
import sbt.Keys._

object TaftBuild extends Build {
  val ScalaVersion = "2.10.3"

  lazy val taft = Project(
    id = "taft",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "taft",
      organization := "com.sharethrough",
      version := "0.1-SNAPSHOT",
      scalaVersion := ScalaVersion,

      resolvers ++= Seq(
        "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
        "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"),

      libraryDependencies ++= Seq("com.typesafe.akka" % "akka-actor" % "2.0.1",
        // Against Scalaz 7.0.6, available for Scala 2.10.4 and 2.11.2
        "org.scalaz.stream" %% "scalaz-stream" % "0.5",
        // Against Scalaz 7.1, available for Scala 2.10.4 and 2.11.2
        "org.scalaz.stream" %% "scalaz-stream" % "0.5a")
    )
  )
}
