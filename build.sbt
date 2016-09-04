scalaVersion := "2.11.8"

name := "restart-project"
version := "1.0"

// projects
lazy val `play` = (project in file("rstr-play")).enablePlugins(PlayScala).dependsOn(core)
lazy val `core` = (project in file("rstr-core"))
lazy val db = project in file("rstr-db")