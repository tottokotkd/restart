package com.tottokotkd.restart.core.model.driver

import com.tottokotkd.restart.core.model.{Driver, DriverComponent, HasTables}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

/**
  * Created by tottokotkd on 20/08/2016.
  */
trait HasTestDriver extends DriverComponent
  with HasTables {
  val driver = new TestDriver {}
//  import tables._
//  import driver.profile.api._
//  import driver._

//  private val schema = Accounts.schema++  Users.schema ++ CognitosIds.schema
//  Try(run(schema.create))
//
//  def createTestAccount: AuthInfo = {
//    val c = RandomStringUtils.randomAscii(256)
//    val a = "createTestAccountA_" + c
//    val u = "createTestAccountU_" + c
//    val i = "createTestAccountI_" + c
//    run(accountManager.createAccount(accountName = a, userName = u, identity = i))
//  }
}

trait TestDriver extends Driver {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("sqliteTestDb")
}
