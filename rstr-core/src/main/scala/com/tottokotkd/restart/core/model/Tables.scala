package com.tottokotkd.restart.core.model

import com.tottokotkd.restart.core.model

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait Tables extends codegen.Tables{
  val profile = model.profile

  /***
    * Slick codegenがSQLiteにおいてAutoInc属性を付与しないため手動で補正したデーた
    */
  object AutoInc {

    import profile.api._
    import slick.model.ForeignKeyAction
    // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
    import slick.jdbc.{GetResult => GR}

    /** Table description of table accounts. Objects of this class serve as prototypes for rows in queries. */
    class Accounts(_tableTag: Tag) extends Table[AccountsRow](_tableTag, "accounts") {
      def * = (accountId, name) <> (AccountsRow.tupled, AccountsRow.unapply)
      /** Maps whole row to an option. Useful for outer joins. */
      def ? = (accountId, Rep.Some(name)).shaped.<>({r=>import r._; _2.map(_=> AccountsRow.tupled((_1, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

      /** Database column account_id SqlType(INTEGER), PrimaryKey */
      val accountId: Rep[Option[Int]] = column[Option[Int]]("account_id", O.PrimaryKey, O.AutoInc)
      /** Database column name SqlType(TEXT) */
      val name: Rep[String] = column[String]("name")
    }
    /** Collection-like TableQuery object for table Accounts */
    lazy val Accounts = new TableQuery(tag => new Accounts(tag))
  }
}

trait TablesComponent {
  val tables: Tables
}

trait HasTables extends TablesComponent {
  val tables = new Tables {}
}
