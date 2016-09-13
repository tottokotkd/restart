package com.tottokotkd.restart.core.domain.account

import java.time.ZoneId

import com.tottokotkd.restart.core.domain.account.auth.HasAuthProviders
import com.tottokotkd.restart.core.model.driver.HasTestDriver
import org.apache.commons.lang3.RandomStringUtils
import org.specs2._

import scala.util.{Random, Try}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 22/08/2016.
  */

class AuthProvidersSpec extends mutable.Specification with HasAuthProviders with HasTestDriver with HasAccountManager {

  import driver._
  import tables._
  import tables.profile.api._

  def generateTestName = generateRandomString("auth providers spec")
  def getRandomeZoneId = {
    import scala.collection.JavaConverters._
    val IDs = ZoneId.getAvailableZoneIds.asScala.toSeq
    val randomId = IDs(Random.nextInt(IDs.length))
    run(accountManager.getDbTimeZoneId(ZoneId.of(randomId)))
  }
  def getTestAccountId(): AccountId = run((Accounts returning Accounts.map(_.accountId)) += AccountsRow(accountId = 0, name = generateTestName, zoneId = getRandomeZoneId))

  "auth providers should" >> {

    "create account" >> {

      "1. twitter " >> {
        val provider = authProviders(Twitter)

        val testId = generateTestTwitterId.toString
        val accountId = getTestAccountId

        val result = run(provider.registerAccount(accountId = accountId, identity = testId))

        "1. to create provider specified data" >> {
          val data = run(TwitterAccounts.filter(_.twitterId === testId.toInt).result)
          data must be length 1
          data.head.accountId must_== accountId
        }

        "2. expected errors" >> {
          val overlapCase = Try(run(provider.registerAccount(accountId = accountId, identity = testId)))
          overlapCase must beFailedTry(AlreadyUsedAuthIdException)

          val invalidCase = Try(run(provider.registerAccount(accountId = accountId, identity = "id must be numeric")))
          invalidCase must beFailedTry(InvalidAuthIdException)
        }

      }
    }
  }
}