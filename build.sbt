scalaVersion := "2.11.8"

name := "restart-project"
version := "1.0"

// projects
lazy val root = project
lazy val `play` = (project in file("play")).enablePlugins(PlayScala).dependsOn(core)
lazy val `core` = (project in file("core"))
