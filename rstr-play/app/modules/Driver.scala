package modules

import com.tottokotkd.restart.core.model.{Driver, DriverComponent}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait PostgresDriver extends Driver {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("postgresDb")
}

trait HasPsqlDriver extends DriverComponent {
  val driver = new PostgresDriver {}
}
