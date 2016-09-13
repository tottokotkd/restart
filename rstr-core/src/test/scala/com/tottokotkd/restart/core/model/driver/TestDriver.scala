package com.tottokotkd.restart.core.model.driver

import java.time.ZoneId

import com.tottokotkd.restart.core.domain.account._
import com.tottokotkd.restart.core.model.{Driver, DriverComponent, HasTables}
import org.apache.commons.lang3.RandomStringUtils
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.util.Try

/**
  * Created by tottokotkd on 20/08/2016.
  */
trait HasTestDriver extends DriverComponent
  with HasTables with HasAccountManager {
  val driver = new TestDriver {}

  import tables._
  import tables.profile.api._
  import driver._

  def createTestTwitterAccount: AccountInfo = {
    val twitterId = twiIdGen.generate
    val accoutName = RandomStringUtils.randomAscii(64)
    run(accountManager.createAccount(provider = Twitter, identity = twitterId, name = accoutName, zoneId = ZoneId.systemDefault))
  }
  def generateTestTwitterId = twiIdGen.generate()
  def generateRandomString(key: String = "") = s"$key ${RandomStringUtils.randomAscii(32)}"
}

trait TestDriver extends Driver {
  val dbConfig = DatabaseConfig.forConfig[JdbcProfile]("postgresTestDb")
}
