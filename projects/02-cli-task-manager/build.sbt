ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
    .settings(
        name := "cli-task-manager",
        libraryDependencies ++= Seq(
            "dev.zio" %% "zio" % "2.1.17"
        )
    )