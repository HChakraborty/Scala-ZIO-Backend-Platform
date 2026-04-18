ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "task-manager",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.17",
      "dev.zio" %% "zio-http" % "3.10.1",
      "dev.zio" %% "zio-json" % "0.9.0"
    )
  )