name := "votee"

version := sys.env.getOrElse("VERSION", "0.1.1")

organization := "io.hiis"

organizationName := "Hiis"

organizationHomepage := Some(new URL("https://www.hiis.io"))

scalaVersion := "3.2.0"

libraryDependencies ++= dependencies

githubOwner := "hiis-io"

githubRepository := "Votee"

githubTokenSource := TokenSource.Environment("PACKAGE_TOKEN")

lazy val dependencies = Seq(
  "org.scalanlp"      %% "breeze"    % "2.1.0",
  "org.scalatest"     %% "scalatest" % "3.2.14"     % "test",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6" % "test"
)
