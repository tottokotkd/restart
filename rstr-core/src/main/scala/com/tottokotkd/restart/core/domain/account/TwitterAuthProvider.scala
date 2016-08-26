package com.tottokotkd.restart.core.domain.account

import com.tottokotkd.restart.core.model.TablesComponent
import com.tottokotkd.restart.core.model.codegen.Tables
import slick.dbio._

import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 21/08/2016.
  */

trait TwitterAuthProvider extends AuthProvider with TablesComponent {

  import tables._
  import tables.profile.api._

  def createAccount(identity: String, name: String): DBIO[AccountId] = {
    val twitterId: Int = Try(identity.toInt).getOrElse(throw InvalidAuthIdException)
    val autoIncrements = (AutoInc.Accounts.map(_.name)) returning AutoInc.Accounts.map(_.accountId)

    val q = for {
      isUsed <- TwitterAccounts.filter(_.twitterId === twitterId).exists.result
      result <- if (isUsed) DBIO.failed(AlreadyUsedAuthIdException) else for {
        accountId <- autoIncrements += name
        _ <- TwitterAccounts += TwitterAccountsRow(twitterId = Some(twitterId), accountId = accountId.get)
      } yield accountId.get
    } yield result
    q.transactionally
  }

  def getAccount(identity: String): DBIO[AccountId] = {
    val twitterId: Int = Try(identity.toInt).getOrElse(throw InvalidAuthIdException)
    for {
      account <- TwitterAccounts.filter(_.twitterId === twitterId).map(_.accountId).result.headOption
    } yield account.getOrElse(throw AccountNotFoundException)
  }
}