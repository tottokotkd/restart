package com.tottokotkd.restart.core.model

import com.tottokotkd.restart.core.model
import slick.backend.DatabaseConfig
import slick.dbio.DBIO
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by tottokotkd on 20/08/2016.
  */

trait Driver {

  val dbConfig: DatabaseConfig[JdbcProfile]

  val profile = model.profile
  lazy val db = dbConfig.db
  def run[T](query: DBIO[T]): T = Await.result(db.run(query), Duration.Inf)
}

trait DriverComponent {
  val driver: Driver
}
