package com.tottokotkd.restart.core.domain.account.auth

import com.tottokotkd.restart.core.domain.account._
import com.tottokotkd.restart.core.model.HasTables
import slick.dbio.DBIO

/**
  * Created by tottokotkd on 20/08/2016.
  */
trait AuthProvider {

  /***
    * create account
    * @param identity auth identity
    * @return account id
    *
    * @throws AlreadyUsedAuthIdException used auth id
    * @throws InvalidAuthIdException invalid auth id
    */
  def createAccount(identity: String, name: String): DBIO[AccountId]

  /***
    * return account
    * @param identity auth identity
    * @return account id
    *
    * @throws InvalidAuthIdException invalid auth id
    * @throws AccountNotFoundException account not found
    */
  def getAccount(identity: String): DBIO[AccountId]
}

trait AuthProvidersComponent {
  val authProviders: Map[AuthProviderType, AuthProvider]
}

trait HasAuthProviders extends AuthProvidersComponent {

  private val twitter = new TwitterAuthProvider with HasTables

  val authProviders = Map[AuthProviderType, AuthProvider](
    Twitter -> twitter
  )
}