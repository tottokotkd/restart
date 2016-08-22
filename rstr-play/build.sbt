scalaVersion := "2.11.8"

name := "restart-play-server"
version := "1.0"

libraryDependencies ++= Seq(
  cache , ws, filters, specs2 % Test,
  "commons-io" % "commons-io" % "2.4",
  "org.xerial" % "sqlite-jdbc" % "3.8.11.2",

  "ch.qos.logback" %  "logback-classic" % "1.1.7")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

/**
  * play-pac4j [2.5]
  */
resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")
libraryDependencies ++= Seq(
  "org.pac4j" % "play-pac4j" % "2.5.0-SNAPSHOT",
  "org.pac4j" % "pac4j-oauth" % "1.9.1"
)

/*
  WebJars
 */
libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "4.0.0-alpha.3",
  "org.webjars" % "react" % "15.2.1"
)