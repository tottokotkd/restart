name := """restart-db"""
version := "1.0"

/*
  flyway
 */
// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "9.4.1209"

flywayUrl := "jdbc:postgresql://localhost:5432/rstr_play"
flywayUser := "rstr_admin"
flywayPassword := "sXMYq7ez5fZZnstyXcEkLpYdhfmW37Ud"
flywayLocations := Seq("filesystem:rstr-db/src/main/resources/db/migration")