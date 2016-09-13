package com.tottokotkd.restart.core.domain.account.auth

import java.time.ZoneId

import com.tottokotkd.restart.core.domain.account._
import com.tottokotkd.restart.core.model.TablesComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait TwitterAuthProvider extends AuthProvider with TablesComponent {

  import tables._
  import tables.profile.api._

  def registerAccount(accountId: AccountId, identity: String): DBIO[Int] = {
    val twitterId: Int = Try(identity.toInt).getOrElse(throw InvalidAuthIdException)

    val q = for {
      isUsed <- TwitterAccounts.filter(_.twitterId === twitterId).exists.result
      result <- if (isUsed) DBIO.failed(AlreadyUsedAuthIdException) else TwitterAccounts += TwitterAccountsRow(twitterId = twitterId, accountId = accountId)
    } yield result
    q.transactionally
  }

  def getAccount(identity: String): DBIO[AccountId] = {
    val twitterId: Int = Try(identity.toInt).getOrElse(throw InvalidAuthIdException)
    val q = for {
      account <- TwitterAccounts.filter(_.twitterId === twitterId).map(_.accountId).result.headOption
    } yield account.getOrElse(throw AccountNotFoundException)
    q.transactionally
  }
}