
name := "Votee"

version := "0.1.0-RC1"

organization := "io.hiis"

organizationName := "Hiis"

organizationHomepage := Some(new URL("https://www.hiis.io"))

scalaVersion := "3.1.0"

libraryDependencies ++= dependencies

assemblyJarName := "votee-0.1.0-RC1.jar"

lazy val dependencies = Seq(
  "org.scalanlp" %% "breeze" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.2.14" % "test",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6" % "test"
)