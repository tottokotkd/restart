package com.tottokotkd.restart.core.model.codegen
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.SQLiteDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Accounts.schema ++ Test.schema ++ TwitterAccounts.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Accounts
   *  @param accountId Database column account_id SqlType(INT), PrimaryKey
   *  @param name Database column name SqlType(TEXT) */
  case class AccountsRow(accountId: Option[Int], name: String)
  /** GetResult implicit for fetching AccountsRow objects using plain SQL queries */
  implicit def GetResultAccountsRow(implicit e0: GR[Option[Int]], e1: GR[String]): GR[AccountsRow] = GR{
    prs => import prs._
    AccountsRow.tupled((<<?[Int], <<[String]))
  }
  /** Table description of table accounts. Objects of this class serve as prototypes for rows in queries. */
  class Accounts(_tableTag: Tag) extends Table[AccountsRow](_tableTag, "accounts") {
    def * = (accountId, name) <> (AccountsRow.tupled, AccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (accountId, Rep.Some(name)).shaped.<>({r=>import r._; _2.map(_=> AccountsRow.tupled((_1, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(INT), PrimaryKey */
    val accountId: Rep[Option[Int]] = column[Option[Int]]("account_id", O.PrimaryKey)
    /** Database column name SqlType(TEXT) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Accounts */
  lazy val Accounts = new TableQuery(tag => new Accounts(tag))

  /** Entity class storing rows of table Test
   *  @param id Database column id SqlType(INT), PrimaryKey */
  case class TestRow(id: Option[Int])
  /** GetResult implicit for fetching TestRow objects using plain SQL queries */
  implicit def GetResultTestRow(implicit e0: GR[Option[Int]]): GR[TestRow] = GR{
    prs => import prs._
    TestRow(<<?[Int])
  }
  /** Table description of table test. Objects of this class serve as prototypes for rows in queries. */
  class Test(_tableTag: Tag) extends Table[TestRow](_tableTag, "test") {
    def * = id <> (TestRow, TestRow.unapply)

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Option[Int]] = column[Option[Int]]("id", O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Test */
  lazy val Test = new TableQuery(tag => new Test(tag))

  /** Entity class storing rows of table TwitterAccounts
   *  @param twitterId Database column twitter_id SqlType(INT), PrimaryKey
   *  @param accountId Database column account_id SqlType(INT) */
  case class TwitterAccountsRow(twitterId: Option[Int], accountId: Int)
  /** GetResult implicit for fetching TwitterAccountsRow objects using plain SQL queries */
  implicit def GetResultTwitterAccountsRow(implicit e0: GR[Option[Int]], e1: GR[Int]): GR[TwitterAccountsRow] = GR{
    prs => import prs._
    TwitterAccountsRow.tupled((<<?[Int], <<[Int]))
  }
  /** Table description of table twitter_accounts. Objects of this class serve as prototypes for rows in queries. */
  class TwitterAccounts(_tableTag: Tag) extends Table[TwitterAccountsRow](_tableTag, "twitter_accounts") {
    def * = (twitterId, accountId) <> (TwitterAccountsRow.tupled, TwitterAccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (twitterId, Rep.Some(accountId)).shaped.<>({r=>import r._; _2.map(_=> TwitterAccountsRow.tupled((_1, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column twitter_id SqlType(INT), PrimaryKey */
    val twitterId: Rep[Option[Int]] = column[Option[Int]]("twitter_id", O.PrimaryKey)
    /** Database column account_id SqlType(INT) */
    val accountId: Rep[Int] = column[Int]("account_id")

    /** Foreign key referencing Accounts (database name accounts_FK_1) */
    lazy val accountsFk = foreignKey("accounts_FK_1", Rep.Some(accountId), Accounts)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TwitterAccounts */
  lazy val TwitterAccounts = new TableQuery(tag => new TwitterAccounts(tag))
}
