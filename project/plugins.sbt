logLevel := Level.Warn


/*
  deploy utils
 */
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

/*
  flyway [DB migration]
 */
resolvers += "Flyway" at "https://flywaydb.org/repo"
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")

/*
  Play Framework
 */
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")
