package com.tottokotkd.restart.core.domain.account

import com.tottokotkd.restart.core.model.driver.HasTestDriver
import org.apache.commons.lang3.RandomStringUtils
import org.specs2._
import scala.util.Try

/**
  * Created by tottokotkd on 22/08/2016.
  */

class AccountManagerSpec extends mutable.Specification with HasAccountManager with HasTestDriver {

  import driver.run
  import tables._
  import tables.profile.api._


  "Account Manager should" >> {

    "create account with auth id" >> {

      "0. invalid provider" >> {
        val provider = NotImplementedProvider
        Try(run(accountManager.createAccount(provider = provider, identity = "", name = ""))) must beFailedTry(AuthProviderNotFoundException)
      }

      "1. twitter" >> {
        val provider = Twitter

        val testId = generateTestTwitterId.toString
        val testName = generateTestName

        val accountInfo = run(accountManager.createAccount(provider = provider, identity = testId, name = testName))

        "a. to create provider specified data" >> {
          val data = run(TwitterAccounts.filter(_.twitterId === testId.toInt).result)
          data must be length 1
          data.head.accountId must_== accountInfo.id.toInt
        }
        "b. to create account data" >> {
          val data = run(Accounts.filter(_.accountId === accountInfo.id).result)
          data must be length 1
          data.head.name must_== testName
        }

        "c. to throw expected exceptions" >> {
          val overlapCase = Try(run(accountManager.createAccount(provider = provider, identity = testId, name = testName)))
          overlapCase must beFailedTry(AlreadyUsedAuthIdException)

          val invalidCase = Try(run(accountManager.createAccount(provider = provider, identity = "id must be numeric", name = testName)))
          invalidCase must beFailedTry(InvalidAuthIdException)
        }

      }
    }

    "return account data" >> {
      "0. invalid provider" >> {
        val provider = NotImplementedProvider
        Try(run(accountManager.getAccount(provider = provider, identity = ""))) must beFailedTry(AuthProviderNotFoundException)
      }

      "1. twitter" >> {

        val provider = Twitter
        val testId = generateTestTwitterId
        val testName = generateTestName

        val account = run(accountManager.createAccount(provider = provider, identity = testId, name = testName))

        "a. to return correct data" >> {
          val data = run(accountManager.getAccount(provider = provider, identity = testId))
          data must_== account
        }

        "b. to throw expected exceptions" >> {
          val invalidCase = Try(run(accountManager.getAccount(provider = provider, identity = "id must be numeric")))
          invalidCase must beFailedTry(InvalidAuthIdException)
        }

      }

    }

  }

}