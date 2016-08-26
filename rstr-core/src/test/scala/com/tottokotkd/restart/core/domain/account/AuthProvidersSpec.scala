package com.tottokotkd.restart.core.domain.account

import com.tottokotkd.restart.core.model.driver.HasTestDriver
import org.apache.commons.lang3.RandomStringUtils
import org.specs2._

import scala.util.Try

/**
  * Created by tottokotkd on 22/08/2016.
  */

class AuthProvidersSpec extends mutable.Specification with HasAuthProviders with HasTestDriver {

  import driver._
  import tables._
  import tables.profile.api._



  "auth providers should" >> {

    "create account" >> {

      "1. twitter " >> {
        val provider = authProviders(Twitter)

        val testId = generateTestTwitterId.toString
        val name = s"test name ${RandomStringUtils.randomAscii(32)}"

        val accountId = run(provider.createAccount(identity = testId, name = name))

        "a. to create provider specified data" >> {
          val data = run(TwitterAccounts.filter(_.twitterId === testId.toInt).result)
          data must be length 1
          data.head.accountId must_== accountId
        }
        "b. to create account data" >> {
          val data = run(Accounts.filter(_.accountId === accountId).result)
          data must be length 1
          data.head.name must_== name
        }

        "c. to throw expected exceptions" >> {
          val overlapCase = Try(run(provider.createAccount(identity = testId, name = name)))
          overlapCase must beFailedTry(AlreadyUsedAuthIdException)

          val invalidCase = Try(run(provider.createAccount(identity = "id must be numeric", name = name)))
          invalidCase must beFailedTry(InvalidAuthIdException)
        }

      }

    }

    "return account" >> {
      "1. twitter" >> {
        val provider = authProviders(Twitter)

        val testId = generateTestTwitterId
        val testName = generateTestName
        val accountId = run(provider.createAccount(identity = testId, name = testName))

        val result = run(provider.getAccount(testId))
        result must_== accountId

        val invalidId = "-0"
        Try(run(provider.getAccount(invalidId))) must beFailedTry(AccountNotFoundException)

      }
    }

  }

}