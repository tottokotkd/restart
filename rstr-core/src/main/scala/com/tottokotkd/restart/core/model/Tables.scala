package com.tottokotkd.restart.core.model

import com.tottokotkd.restart.core.model

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait Tables extends codegen.Tables{
  val profile = model.profile
}

trait TablesComponent {
  val tables: Tables
}

trait HasTables extends TablesComponent {
  val tables = new Tables {}
}
