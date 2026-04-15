name := "practice"

version := "0.1"

scalaVersion := "3.3.7"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.21",
  "dev.zio" %% "zio-test" % "2.0.21" % Test
)