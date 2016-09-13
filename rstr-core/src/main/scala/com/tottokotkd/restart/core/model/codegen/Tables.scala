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
  lazy val schema: profile.SchemaDescription = Array(Accounts.schema, DefaultGainLogs.schema, Resources.schema, ShcemaVersion.schema, TimeZoneIds.schema, TwitterAccounts.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Accounts
   *  @param accountId Database column account_id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(text)
   *  @param zoneId Database column zone_id SqlType(int4) */
  case class AccountsRow(accountId: Int, name: String, zoneId: Int)
  /** GetResult implicit for fetching AccountsRow objects using plain SQL queries */
  implicit def GetResultAccountsRow(implicit e0: GR[Int], e1: GR[String]): GR[AccountsRow] = GR{
    prs => import prs._
    AccountsRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table accounts. Objects of this class serve as prototypes for rows in queries. */
  class Accounts(_tableTag: Tag) extends Table[AccountsRow](_tableTag, Some("rstr_account"), "accounts") {
    def * = (accountId, name, zoneId) <> (AccountsRow.tupled, AccountsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(name), Rep.Some(zoneId)).shaped.<>({r=>import r._; _1.map(_=> AccountsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(serial), AutoInc, PrimaryKey */
    val accountId: Rep[Int] = column[Int]("account_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name: Rep[String] = column[String]("name")
    /** Database column zone_id SqlType(int4) */
    val zoneId: Rep[Int] = column[Int]("zone_id")

    /** Foreign key referencing TimeZoneIds (database name accounts_zone_id_fkey) */
    lazy val timeZoneIdsFk = foreignKey("accounts_zone_id_fkey", zoneId, TimeZoneIds)(r => r.zoneId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Accounts */
  lazy val Accounts = new TableQuery(tag => new Accounts(tag))

  /** Entity class storing rows of table DefaultGainLogs
   *  @param accountId Database column account_id SqlType(int4), PrimaryKey
   *  @param lastUpdate Database column last_update SqlType(timestamptz) */
  case class DefaultGainLogsRow(accountId: Int, lastUpdate: java.sql.Timestamp)
  /** GetResult implicit for fetching DefaultGainLogsRow objects using plain SQL queries */
  implicit def GetResultDefaultGainLogsRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp]): GR[DefaultGainLogsRow] = GR{
    prs => import prs._
    DefaultGainLogsRow.tupled((<<[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table default_gain_logs. Objects of this class serve as prototypes for rows in queries. */
  class DefaultGainLogs(_tableTag: Tag) extends Table[DefaultGainLogsRow](_tableTag, Some("rstr_stamp"), "default_gain_logs") {
    def * = (accountId, lastUpdate) <> (DefaultGainLogsRow.tupled, DefaultGainLogsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(lastUpdate)).shaped.<>({r=>import r._; _1.map(_=> DefaultGainLogsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column account_id SqlType(int4), PrimaryKey */
    val accountId: Rep[Int] = column[Int]("account_id", O.PrimaryKey)
    /** Database column last_update SqlType(timestamptz) */
    val lastUpdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("last_update")

    /** Foreign key referencing Accounts (database name default_gain_logs_account_id_fkey) */
    lazy val accountsFk = foreignKey("default_gain_logs_account_id_fkey", accountId, Accounts)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table DefaultGainLogs */
  lazy val DefaultGainLogs = new TableQuery(tag => new DefaultGainLogs(tag))

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

  /** Entity class storing rows of table ShcemaVersion
   *  @param installedRank Database column installed_rank SqlType(int4), PrimaryKey
   *  @param version Database column version SqlType(varchar), Length(50,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(200,true)
   *  @param `type` Database column type SqlType(varchar), Length(20,true)
   *  @param script Database column script SqlType(varchar), Length(1000,true)
   *  @param checksum Database column checksum SqlType(int4), Default(None)
   *  @param installedBy Database column installed_by SqlType(varchar), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(timestamp)
   *  @param executionTime Database column execution_time SqlType(int4)
   *  @param success Database column success SqlType(bool) */
  case class ShcemaVersionRow(installedRank: Int, version: Option[String] = None, description: String, `type`: String, script: String, checksum: Option[Int] = None, installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching ShcemaVersionRow objects using plain SQL queries */
  implicit def GetResultShcemaVersionRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]], e4: GR[java.sql.Timestamp], e5: GR[Boolean]): GR[ShcemaVersionRow] = GR{
    prs => import prs._
    ShcemaVersionRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table shcema_version. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class ShcemaVersion(_tableTag: Tag) extends Table[ShcemaVersionRow](_tableTag, "shcema_version") {
    def * = (installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (ShcemaVersionRow.tupled, ShcemaVersionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(installedRank), version, Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> ShcemaVersionRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column installed_rank SqlType(int4), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)
    /** Database column version SqlType(varchar), Length(50,true), Default(None) */
    val version: Rep[Option[String]] = column[Option[String]]("version", O.Length(50,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(varchar), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(varchar), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(int4), Default(None) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum", O.Default(None))
    /** Database column installed_by SqlType(varchar), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(timestamp) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(int4) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(bool) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name shcema_version_s_idx) */
    val index1 = index("shcema_version_s_idx", success)
  }
  /** Collection-like TableQuery object for table ShcemaVersion */
  lazy val ShcemaVersion = new TableQuery(tag => new ShcemaVersion(tag))

  /** Entity class storing rows of table TimeZoneIds
   *  @param zoneId Database column zone_id SqlType(serial), AutoInc, PrimaryKey
   *  @param code Database column code SqlType(text) */
  case class TimeZoneIdsRow(zoneId: Int, code: String)
  /** GetResult implicit for fetching TimeZoneIdsRow objects using plain SQL queries */
  implicit def GetResultTimeZoneIdsRow(implicit e0: GR[Int], e1: GR[String]): GR[TimeZoneIdsRow] = GR{
    prs => import prs._
    TimeZoneIdsRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table time_zone_ids. Objects of this class serve as prototypes for rows in queries. */
  class TimeZoneIds(_tableTag: Tag) extends Table[TimeZoneIdsRow](_tableTag, Some("rstr_account"), "time_zone_ids") {
    def * = (zoneId, code) <> (TimeZoneIdsRow.tupled, TimeZoneIdsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(zoneId), Rep.Some(code)).shaped.<>({r=>import r._; _1.map(_=> TimeZoneIdsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column zone_id SqlType(serial), AutoInc, PrimaryKey */
    val zoneId: Rep[Int] = column[Int]("zone_id", O.AutoInc, O.PrimaryKey)
    /** Database column code SqlType(text) */
    val code: Rep[String] = column[String]("code")

    /** Uniqueness Index over (code) (database name time_zone_ids_code_key) */
    val index1 = index("time_zone_ids_code_key", code, unique=true)
  }
  /** Collection-like TableQuery object for table TimeZoneIds */
  lazy val TimeZoneIds = new TableQuery(tag => new TimeZoneIds(tag))

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
