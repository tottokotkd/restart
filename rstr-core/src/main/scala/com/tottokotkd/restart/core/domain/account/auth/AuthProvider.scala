package com.tottokotkd.restart.core.domain.account.auth

import java.time.ZoneId

import com.tottokotkd.restart.core.domain.account._
import com.tottokotkd.restart.core.model.HasTables
import slick.dbio.DBIO

/**
  * Created by tottokotkd on 20/08/2016.
  */
trait AuthProvider {

  /***
    * register account identity string
    * @param identity auth identity
    * @return inserted row count
    *
    * @throws AlreadyUsedAuthIdException used id
    * @throws InvalidAuthIdException invalid id
    */
  def registerAccount(accountId: AccountId, identity: String): DBIO[Int]

  /***
    * return account
    * @param identity auth identity
    * @return account id
    *
    * @throws InvalidAuthIdException invalid id
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