scalaVersion := "2.11.8"

name := "restart-play-server"
version := "1.0"

libraryDependencies ++= Seq(
  cache , ws, filters, specs2 % Test,
  "commons-io" % "commons-io" % "2.4")

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

/*
  flyway
 */
flywayUrl := "jdbc:postgresql://localhost:5432/rstr_play"
flywayUser := "rstr_admin"
flywayPassword := "sXMYq7ez5fZZnstyXcEkLpYdhfmW37Ud"
flywayLocations := Seq("filesystem:rstr-db/src/main/resources/db/migration")
flywaySchemas := Seq("public", "rstr_account", "rstr_data", "rstr_stamp")
flywayTable := "shcema_version"
