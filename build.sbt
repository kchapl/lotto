name := "lotto"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies ++= Seq(
  ws,
  "org.jsoup" % "jsoup" % "1.10.3"
)
