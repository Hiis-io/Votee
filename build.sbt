import language.postfixOps

name := "votee"

organization := "Hiis.io"

version := "0.1.0-SNAPSHOT"

//crossScalaVersions ++= Seq("3.1.0", "2.13.6")
scalaVersion := "3.1.0"

val circeVersion = "0.14.1"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "org.scalactic" %% "scalactic" % "3.2.11",
  "org.scalanlp" %% "breeze" % "2.0.1-RC1",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6"
)

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

logBuffered := false

parallelExecution in Test := false

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalacOptions in (Compile, doc) ++= Seq("-diagrams","-implicits")

scalacOptions in Test ++= Seq("-Yrangepos")

val allSettings = Defaults.coreDefaultSettings

lazy val project = Project("votee", file("."))
  .configs(Testing.configs: _*)
  .settings(allSettings, Testing.settings)

resolvers += Resolver.sonatypeRepo("public")
resolvers += Resolver.typesafeRepo("releases")
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"