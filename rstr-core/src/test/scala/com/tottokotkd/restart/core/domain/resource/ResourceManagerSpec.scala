package com.tottokotkd.restart.core.domain.resource

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

    "initResource test" >> {

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

    "getResource test" >> {

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

  }

}