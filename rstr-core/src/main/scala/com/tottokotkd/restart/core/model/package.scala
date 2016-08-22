package com.tottokotkd.restart.core

import slick.driver.JdbcDriver

/**
  * Created by tottokotkd on 21/08/2016.
  */
package object model {
  val profile: JdbcDriver = slick.driver.SQLiteDriver
}
