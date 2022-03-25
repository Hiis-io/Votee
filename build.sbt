import sbt.Resolver

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "votee",
    idePackagePrefix := Some("io.hiis.votee")
  )


resolvers += Resolver.sonatypeRepo("public")
resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "com.storm-enroute" %% "scalameter" % "0.8.2",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "org.scalactic" %% "scalactic" % "3.2.11",
  "org.typelevel" %% "spire" % "0.17.0",
  "org.scalanlp" %% "breeze" % "2.0.1-RC1"
)
