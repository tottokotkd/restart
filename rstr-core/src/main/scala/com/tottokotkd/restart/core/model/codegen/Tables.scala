package com.tottokotkd.restart.core.model.codegen
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Accounts.schema ++ Resources.schema ++ TwitterAccounts.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Accounts
   *  @param accountId Database column account_id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(text) */
  case class AccountsRow(accountId: Int, name: String)
  /** GetResult implicit for fetching AccountsRow objects using plain SQL queries */
  implicit def GetResultAccountsRow(implicit e0: GR[Int], e1: GR[String]): GR[AccountsRow] = GR{
    prs => import prs._
    AccountsRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table accounts. Objects of this class serve as prototypes for rows in queries. */
  class Accounts(_tableTag: Tag) extends Table[AccountsRow](_tableTag, Some("rstr_account"), "accounts") {
    def * = (accountId, name) <> (AccountsRow.tupled, AccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> AccountsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(serial), AutoInc, PrimaryKey */
    val accountId: Rep[Int] = column[Int]("account_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")
  }
  /** Collection-like TableQuery object for table Accounts */
  lazy val Accounts = new TableQuery(tag => new Accounts(tag))

  /** Entity class storing rows of table Resources
   *  @param accountId Database column account_id SqlType(int4), PrimaryKey
   *  @param money Database column money SqlType(int4)
   *  @param cc Database column cc SqlType(int4) */
  case class ResourcesRow(accountId: Int, money: Int, cc: Int)
  /** GetResult implicit for fetching ResourcesRow objects using plain SQL queries */
  implicit def GetResultResourcesRow(implicit e0: GR[Int]): GR[ResourcesRow] = GR{
    prs => import prs._
    ResourcesRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table resources. Objects of this class serve as prototypes for rows in queries. */
  class Resources(_tableTag: Tag) extends Table[ResourcesRow](_tableTag, Some("rstr_data"), "resources") {
    def * = (accountId, money, cc) <> (ResourcesRow.tupled, ResourcesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(money), Rep.Some(cc)).shaped.<>({r=>import r._; _1.map(_=> ResourcesRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(int4), PrimaryKey */
    val accountId: Rep[Int] = column[Int]("account_id", O.PrimaryKey)
    /** Database column money SqlType(int4) */
    val money: Rep[Int] = column[Int]("money")
    /** Database column cc SqlType(int4) */
    val cc: Rep[Int] = column[Int]("cc")

    /** Foreign key referencing Accounts (database name resources_account_id_fkey) */
    lazy val accountsFk = foreignKey("resources_account_id_fkey", accountId, Accounts)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Resources */
  lazy val Resources = new TableQuery(tag => new Resources(tag))

  /** Entity class storing rows of table TwitterAccounts
   *  @param twitterId Database column twitter_id SqlType(int4), PrimaryKey
   *  @param accountId Database column account_id SqlType(int4) */
  case class TwitterAccountsRow(twitterId: Int, accountId: Int)
  /** GetResult implicit for fetching TwitterAccountsRow objects using plain SQL queries */
  implicit def GetResultTwitterAccountsRow(implicit e0: GR[Int]): GR[TwitterAccountsRow] = GR{
    prs => import prs._
    TwitterAccountsRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table twitter_accounts. Objects of this class serve as prototypes for rows in queries. */
  class TwitterAccounts(_tableTag: Tag) extends Table[TwitterAccountsRow](_tableTag, Some("rstr_account"), "twitter_accounts") {
    def * = (twitterId, accountId) <> (TwitterAccountsRow.tupled, TwitterAccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(twitterId), Rep.Some(accountId)).shaped.<>({r=>import r._; _1.map(_=> TwitterAccountsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column twitter_id SqlType(int4), PrimaryKey */
    val twitterId: Rep[Int] = column[Int]("twitter_id", O.PrimaryKey)
    /** Database column account_id SqlType(int4) */
    val accountId: Rep[Int] = column[Int]("account_id")

    /** Foreign key referencing Accounts (database name twitter_accounts_account_id_fkey) */
    lazy val accountsFk = foreignKey("twitter_accounts_account_id_fkey", accountId, Accounts)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TwitterAccounts */
  lazy val TwitterAccounts = new TableQuery(tag => new TwitterAccounts(tag))
}
