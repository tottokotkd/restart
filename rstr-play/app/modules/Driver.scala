package modules

import com.tottokotkd.restart.core.model.{Driver, DriverComponent}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait SQLiteDriver extends Driver {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("sqliteDb")
}

trait HasSQLiteDriver extends DriverComponent {
  val driver = new SQLiteDriver {}
}
