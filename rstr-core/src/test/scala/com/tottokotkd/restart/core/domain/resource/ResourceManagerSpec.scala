package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime

import com.tottokotkd.restart.core.model.driver.HasTestDriver
import org.specs2._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
  * Created by tottokotkd on 04/09/2016.
  */

class ResourceManagerSpec extends mutable.Specification with HasTestDriver with HasResourceManager {

  import driver.run
  import tables._
  import tables.profile.api._

  "ResourceManagerSpec" >> {

    "initResource" >> {

      "1. default param" >> {
        implicit val auth = createTestTwitterAccount

        val resource = run(resourceManager.initResource())
        resource must_== initialResource

        val data = run(Resources.filter(_.accountId === auth.id).result)
        data must be length 1
        data.head.money must_== initialResource.money
        data.head.cc must_== initialResource.cc
      }

      "2. specified param" >> {
        implicit val auth = createTestTwitterAccount
        val initData = ResourceInfo(money = 1, cc = 2)

        val resource = run(resourceManager.initResource(initData))
        resource must_== initData

        val data = run(Resources.filter(_.accountId === auth.id).result)
        data must be length 1
        data.head.money must_== initData.money
        data.head.cc must_== initData.cc

      }

      "expected errors" >> {
        implicit val auth = createTestTwitterAccount
        val resource = run(resourceManager.initResource())
        "ResourceAlreadyInitializedError" >> {
          Try(run(resourceManager.initResource())) must beFailedTry(ResourceAlreadyInitializedError)
        }
      }
    }

    "getResource" >> {

      "1. success pattern" >> {
        implicit val auth = createTestTwitterAccount

        val initData = ResourceInfo(money = 1, cc = 2)

        val resource = run(for {
          _ <- resourceManager.initResource(initData)
          r <- resourceManager.getResource
        } yield r)
        resource must_== initData
      }

      "expected errors" >> {
        "ResourceNotInitializedError" >> {
          implicit val auth = createTestTwitterAccount
          Try(run(resourceManager.getResource)) must beFailedTry(ResourceNotInitializedError)
        }
      }
    }

    "saveResoruce" >> {

      "1. success pattern" >> {
        implicit val auth = createTestTwitterAccount

        val initData = ResourceInfo(money = 1, cc = 2)
        val latestData = ResourceInfo(money = 10001, cc = 10002)

        val (count, resource) = run(for {
          _ <- resourceManager.initResource(initData)
          c <- resourceManager.overwriteResoruce(latestData)
          r <- resourceManager.getResource
        } yield (c, r))
        count must_== 1
        resource must_== latestData
      }

      "expected errors" >> {
        "ResourceNotInitializedError" >> {
          implicit val auth = createTestTwitterAccount
          val data = ResourceInfo(money = 1, cc = 2)
          Try(run(resourceManager.overwriteResoruce(data))) must beFailedTry(ResourceNotInitializedError)
        }
      }
    }

    "applyCcGain" >> {

      val start = ZonedDateTime.now

      "1. initial" >> {
        implicit val auth = createTestTwitterAccount
        val end = start.plusMinutes(5)

        // cc param.: init. 200, gain 200/min., max 1000
        val q = for {
          initData <- resourceManager.initResource()
          result <- resourceManager.applyCcGain(data = initData, start = start, end = end)
        } yield (initData, result)
        val (initData, result) = run(q)
        result.cc must_== 700
        result must_== initData.copy(cc = result.cc)
      }

      "2. repeat" >> {
        implicit val auth = createTestTwitterAccount
        // cc param.: init. 200, gain 200/min., max 1000
        val q = for {
          initData <- resourceManager.initResource()
          first <- resourceManager.applyCcGain(initData, start, start.plusMinutes(5))
          lastUpdate <- CcGains.filter(_.accountId === auth.id).map(_.lastUpdate).result.head.map(Implicits.toZonedDateTime)
          second <- resourceManager.applyCcGain(first, lastUpdate, lastUpdate.plusMinutes(2))
        } yield (initData, second)

        val (initData, result) = run(q)
        result.cc must_== 900
        result must_== initData.copy(cc = result.cc)

      }
    }

  }
}