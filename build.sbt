import language.postfixOps

name := "votee"

organization := "Hiis.io"

version := "0.1.0-SNAPSHOT"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "com.storm-enroute" %% "scalameter" % "0.8.2",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "org.scalactic" %% "scalactic" % "3.2.11",
  "org.typelevel" %% "spire" % "0.17.0",
  "org.scalanlp" %% "breeze" % "2.0.1-RC1"
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
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"