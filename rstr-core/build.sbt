scalaVersion := "2.11.8"

name := "restart-core"

version := "1.0"
val slickVersion = "3.1.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4" % "runtime",

  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" % "slick-hikaricp_2.11" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,

  "org.scalaz" % "scalaz-core_2.11" % "7.2.4",
  "commons-codec" % "commons-codec" % "1.10",
  "org.apache.shiro" % "shiro-core" % "1.3.0",

  "org.specs2" %% "specs2-core" % "3.8.4" % "test",
  "org.apache.commons" % "commons-lang3" % "3.4" % "test",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.2" % "compile"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

/**
  * logger
  */

libraryDependencies ++= Seq (
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0" % "compile",
  "ch.qos.logback" %  "logback-classic" % "1.1.7" % "runtime",
  "org.slf4j" % "slf4j-nop" % "1.6.4" % "test"
)

/*
  slick codegen
 */
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1209"


sourceManaged <<= baseDirectory
lazy val slickCodeGen = TaskKey[Seq[File]]("slick-codegen")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>

  val outputDir = (dir / "src/main/scala").getPath
  val url = "jdbc:postgresql://localhost:5432/rstr_test"
  val jdbcDriver = "org.postgresql.Driver"
  val slickDriver = "slick.driver.PostgresDriver"
  val pkg = "com.tottokotkd.restart.core.model.codegen"
  val user = "rstr_admin"
  val password = "sXMYq7ez5fZZnstyXcEkLpYdhfmW37Ud"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, password), s.log))
  Seq[File]()
}
slickCodeGen <<= slickCodeGenTask