name := "lotto"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  ws,
  "org.jsoup" % "jsoup" % "1.10.3"
)
