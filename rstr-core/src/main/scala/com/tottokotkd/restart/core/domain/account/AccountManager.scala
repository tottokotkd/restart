package com.tottokotkd.restart.core.domain.account

import java.time.ZoneId
import java.util.TimeZone

import com.tottokotkd.restart.core.domain.account.auth.{AuthProvidersComponent, HasAuthProviders}
import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 20/08/2016.
  */

case class AccountInfo(id: Int, name: String, zoneId: ZoneId)

trait AccountManager extends AuthProvidersComponent with TablesComponent {

  import tables._
  import tables.profile.api._

  /*** create account
    *
    * @param provider auth provider type
    * @param identity identity from auth provider
    * @param name account name
    * @return account id
    *
    * @throws AuthProviderNotFoundException auth provider is not found by name.
    */
  def createAccount(provider: AuthProviderType, identity: String, name: String, zoneId: ZoneId): DBIO[AccountInfo] = {

    val autoInc = Accounts.map{r => (r.name, r.zoneId)} returning Accounts.map(_.accountId)

    authProviders.get(provider) match {
      case Some(p) => (for {
        dbZoneId <- getDbTimeZoneId(zoneId)
        accountId <- autoInc += (name, dbZoneId)
        _  <- p.registerAccount(accountId = accountId, identity = identity)
        account <- getAccount(provider = provider, identity = identity)
      } yield account).transactionally
      case None => throw AuthProviderNotFoundException
    }
  }

  /*** get pk id for zone id
    *
    * @param zoneId
    * @return data id for ZoneId
    */
  def getDbTimeZoneId(zoneId: ZoneId): DBIO[Int] = {
    val autoInc = TimeZoneIds.map(_.code) returning TimeZoneIds.map(_.zoneId)
    for {
      row <- TimeZoneIds.filter(_.code === zoneId.getId).result.headOption
      id <- row match {
        case None => autoInc += zoneId.getId
        case Some(r) => DBIO.successful(r.zoneId)
      }
    } yield id
  }

  /*** get account
    *
    * @param provider auth provider type
    * @param identity identity from auth provider
    * @return account data
    *
    * @throws AuthProviderNotFoundException auth provider is not found by name.
    * @throws AccountNotFoundException account not found
    */
  def getAccount(provider: AuthProviderType, identity: String): DBIO[AccountInfo] = {

    val joined = Accounts join TimeZoneIds on(_.zoneId === _.zoneId)

    authProviders.get(provider) match {
      case Some(p) => for {
        id <- p.getAccount(identity = identity)
        account <- joined.filter(_._1.accountId === id).result.headOption
      } yield account.map(a => AccountInfo(id = a._1.accountId, name = a._1.name, zoneId = ZoneId.of(a._2.code))).getOrElse(throw AccountNotFoundException)
      case None => throw AuthProviderNotFoundException
    }
  }

//  private def toAccountInfo(accountsRow: AccountsRow): AccountInfo = AccountInfo(id = accountsRow.accountId, name = accountsRow.name, zoneId = ZoneId.of(accountsRow.zoneId))
}

trait AccountManagerComponent {
  val accountManager: AccountManager
}

trait HasAccountManager extends AccountManagerComponent{
  val accountManager: AccountManager = new AccountManager with HasAuthProviders with HasTables
}