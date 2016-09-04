package com.tottokotkd.restart.core.domain.account

import com.tottokotkd.restart.core.domain.account.auth.{AuthProvidersComponent, HasAuthProviders}
import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 20/08/2016.
  */

case class AccountInfo(id: Int, name: String)

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
  def createAccount(provider: AuthProviderType, identity: String, name: String): DBIO[AccountInfo] = {
    authProviders.get(provider) match {
      case Some(p) => for {
        accountId <- p.createAccount(identity = identity, name = name)
        account <- getAccount(provider = provider, identity = identity)
      } yield account
      case None => throw AuthProviderNotFoundException
    }
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
    authProviders.get(provider) match {
      case Some(p) => for {
        id <- p.getAccount(identity = identity)
        account <- Accounts.filter(_.accountId === id).result.headOption
      } yield account.map(toAccountInfo).getOrElse(throw AccountNotFoundException)
      case None => throw AuthProviderNotFoundException
    }
  }

  private def toAccountInfo(accountsRow: AccountsRow): AccountInfo = AccountInfo(id = accountsRow.accountId.get, name = accountsRow.name)
}

trait AccountManagerComponent {
  val accountManager: AccountManager
}

trait HasAccountManager extends AccountManagerComponent{
  val accountManager: AccountManager = new AccountManager with HasAuthProviders with HasTables
}