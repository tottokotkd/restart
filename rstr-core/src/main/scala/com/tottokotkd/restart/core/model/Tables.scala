package com.tottokotkd.restart.core.model

import java.sql.Timestamp
import java.time.{ZoneId, ZonedDateTime}

import com.tottokotkd.restart.core.domain.account.AccountInfo
import com.tottokotkd.restart.core.model

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait Tables extends codegen.Tables{
  val profile = model.profile
}

trait TablesComponent {
  val tables: Tables

  object Implicits {
    import scala.languageFeature._
    implicit def toSqlTimestamp(z: ZonedDateTime): Timestamp = Timestamp.from(z.toInstant)
    implicit def toZonedDateTime(t: Timestamp)(implicit accountInfo: AccountInfo): ZonedDateTime = ZonedDateTime.ofInstant(t.toInstant, accountInfo.zoneId)
  }

}

trait HasTables extends TablesComponent {
  val tables = new Tables {}
}
