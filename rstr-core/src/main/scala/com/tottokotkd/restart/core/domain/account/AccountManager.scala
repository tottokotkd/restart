package com.tottokotkd.restart.core.domain.account

import slick.dbio.DBIO

/**
  * Created by tottokotkd on 20/08/2016.
  */
trait AccountManager extends AuthProvidersComponent {

  /*** create account
    *
    * @param provider auth provider type
    * @param identity identity from auth provider
    * @param name account name
    * @return account id
    *
    * @throws AuthProviderNotFoundException auth provider is not found by name.
    * @throws AlreadyUsedAuthIdException used auth id
    * @throws InvalidAuthIdException invalid auth id
    */
  def createAccount(provider: AuthProviderType, identity: String, name: String): DBIO[AccountId] = {
    authProviders.get(provider) match {
      case Some(p) => p.createAccount(identity = identity, name = name)
      case None => throw AuthProviderNotFoundException
    }
  }
  /*** get account
    *
    * @param provider auth provider type
    * @param identity identity from auth provider
    * @return account id
    *
    * @throws AuthProviderNotFoundException auth provider is not found by name.
    * @throws AccountNotFoundException account not found
    * @throws InvalidAuthIdException invalid auth id
    */
  def getAccount(provider: AuthProviderType, identity: String): DBIO[AccountId] = {
    authProviders.get(provider) match {
      case Some(p) => p.getAccount(identity = identity)
      case None => throw AuthProviderNotFoundException
    }
  }
}

trait AccountManagerComponent {
  val accountManager: AccountManager
}

trait HasAccountManager extends AccountManagerComponent{
  val accountManager: AccountManager = new AccountManager with HasAuthProviders
}