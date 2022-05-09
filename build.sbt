
name := "votee"

organization := "Hiis.io"

version := "0.1.0-SNAPSHOT"

scalaVersion := "3.1.0"

exportJars := true

Compile / packageBin / publishArtifact := true

Compile / packageDoc / publishArtifact := true

Compile / packageSrc / publishArtifact := true

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "org.scalanlp" %% "breeze" % "2.0.1-RC1",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6"
)

Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.ScalaLibrary