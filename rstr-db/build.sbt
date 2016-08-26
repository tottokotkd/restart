name := """restart-db"""
version := "1.0"

/*
  flyway
 */

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.11.2"
flywayUrl := "jdbc:sqlite:C:/Users/tottokotkd/rstr-data/db"
flywayUser := ""
flywayLocations := Seq("filesystem:rstr-db/src/main/resources/db/migration")