name := "Votee"

version := "0.1.0"

organization := "io.hiis"

organizationName := "Hiis"

organizationHomepage := Some(new URL("https://www.hiis.io"))

scalaVersion := "3.2.0"

libraryDependencies ++= dependencies

assemblyJarName := "votee-0.1.0.jar"

githubOwner := "icemc"

githubRepository := "Votee"

githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")

lazy val dependencies = Seq(
  "org.scalanlp"      %% "breeze"    % "2.1.0",
  "org.scalatest"     %% "scalatest" % "3.2.14"     % "test",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6" % "test"
)
