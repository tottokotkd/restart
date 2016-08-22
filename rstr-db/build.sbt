name := """restart-db"""
version := "1.0"

/*
  flyway
 */

libraryDependencies += "com.h2database" % "h2" % "1.4.192"
flywayUrl := "jdbc:h2:file:./target/h2/rstr_db"
flywayUser := "sa"
flywaySchemas := Seq("rstr_account", "rstr_data")
flywayLocations := Seq("filesystem:rstr-db/src/main/resources/db/migration")