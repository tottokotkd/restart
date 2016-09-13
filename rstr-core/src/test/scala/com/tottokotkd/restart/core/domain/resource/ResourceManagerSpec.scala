package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime

import com.tottokotkd.restart.core.model.driver.HasTestDriver
import org.specs2._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
  * Created by tottokotkd on 04/09/2016.
  */

class ResourceManagerSpec extends mutable.Specification with HasTestDriver with HasResourceManager with HasResourceCalculator {

  import driver.run
  import tables._
  import tables.profile.api._

  "ResourceManagerSpec" >> {

    "initResource" >> {

      "1. default param" >> {
        implicit val auth = createTestTwitterAccount
        import Implicits.toSqlTimestamp
        val stamp = ZonedDateTime.now

        val resource = run(resourceManager.initResource(stamp = stamp))
        resource must_== initialResource

        val resourceRow = run(Resources.filter(_.accountId === auth.id).result)
        resourceRow must be length 1
        resourceRow.head must_== ResourcesRow(accountId = auth.id, money = initialResource.money, cc = initialResource.cc)

        val defaultLog = run(DefaultGainLogs.filter(_.accountId === auth.id).result)
        defaultLog must be length 1
        defaultLog.head must_== DefaultGainLogsRow(accountId = auth.id, lastUpdate = stamp)
      }

      "2. specified param" >> {
        implicit val auth = createTestTwitterAccount
        val initData = ResourceInfo(money = 1, cc = 2)
        val stamp = ZonedDateTime.now

        val resource = run(resourceManager.initResource(stamp = stamp, data = initData))
        resource must_== initData

        val resourceRow = run(Resources.filter(_.accountId === auth.id).result)
        resourceRow must be length 1
        resourceRow.head must_== ResourcesRow(accountId = auth.id, money = initData.money, cc = initData.cc)
      }

      "expected errors" >> {
        implicit val auth = createTestTwitterAccount
        val resource = run(resourceManager.initResource(stamp = ZonedDateTime.now))
        "ResourceAlreadyInitializedError" >> {
          Try(run(resourceManager.initResource(stamp = ZonedDateTime.now))) must beFailedTry(ResourceAlreadyInitializedError)
        }
      }
    }

    "getResource" >> {

      "1. success pattern" >> {
        implicit val auth = createTestTwitterAccount

        val initData = ResourceInfo(money = 1, cc = 2)

        val resource = run(for {
          _ <- resourceManager.initResource(stamp = ZonedDateTime.now, initData)
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
          _ <- resourceManager.initResource(stamp = ZonedDateTime.now, initData)
          c <- resourceManager.overwriteResource(latestData)
          r <- resourceManager.getResource
        } yield (c, r))
        count must_== 1
        resource must_== latestData
      }

      "expected errors" >> {
        "ResourceNotInitializedError" >> {
          implicit val auth = createTestTwitterAccount
          val data = ResourceInfo(money = 1, cc = 2)
          Try(run(resourceManager.overwriteResource(data))) must beFailedTry(ResourceNotInitializedError)
        }
      }
    }

    "applyCcGain" >> {

      val start = ZonedDateTime.now

      "1. initial" >> {
        implicit val auth = createTestTwitterAccount
        val end = start.plusMinutes(5)
        val initData = run(resourceManager.initResource(stamp = ZonedDateTime.now))

        // cc param.: init. 200, gain 200/min., max 1000
        val update = resourceManager.gainCc(data = ResourceUpdateData(initData), start = start, end = end)
        val result = run(resourceManager.applyUpdate(update))
        result.cc must_== 700
        result must_== initData.copy(cc = result.cc)

        val log = run(DefaultGainLogs.filter(_.accountId === auth.id).result.head)
        Implicits.toZonedDateTime(log.lastUpdate) must_== end
      }

      "2. repeat" >> {
        implicit val auth = createTestTwitterAccount
        val initData = run(resourceManager.initResource(stamp = ZonedDateTime.now))

        // cc param.: init. 200, gain 200/min., max 1000
        val midTime = start.plusMinutes(5)
        val endTime = midTime.plusMinutes(2)
        val first = resourceManager.gainCc(ResourceUpdateData(initData), start, midTime)
        val second = resourceManager.gainCc(first, midTime, endTime)
        val result = run(resourceManager.applyUpdate(second))

        result.cc must_== 900
        result must_== initData.copy(cc = result.cc)

        val log = run(DefaultGainLogs.filter(_.accountId === auth.id).result.head)
        Implicits.toZonedDateTime(log.lastUpdate) must_== endTime

      }
    }

    "defaultUpdate" >>  {

      implicit val auth = createTestTwitterAccount
      val initTime = ZonedDateTime.now
      val target = initTime.plusHours(3)

      val q = for {
        initData <- resourceManager.initResource(stamp = initTime)
        latestData <- resourceManager.defaultUpdate(target = target)
      } yield (initData, latestData)
      val (initData, latestData) = run(q)

      "cc update" >> {
        latestData.cc must_== resourceCalculator.ccGain(current = initData.cc, start = initTime, end = target).get
      }

    }
  }
}