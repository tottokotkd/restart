scalaVersion := "2.11.8"

name := "restart-core"

version := "1.0"
val slickVersion = "3.1.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.6.4" % "runtime",

  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" % "slick-hikaricp_2.11" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "org.postgresql" % "postgresql" % "9.4.1208",
  "com.h2database" % "h2" % "1.4.192",

  "org.scalaz" % "scalaz-core_2.11" % "7.2.4",
  "commons-codec" % "commons-codec" % "1.10",

  "org.specs2" %% "specs2-core" % "3.8.4" % "test",
  "org.apache.commons" % "commons-lang3" % "3.4" % "test"


)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
