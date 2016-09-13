package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime

import com.tottokotkd.restart.core.domain.account.AccountInfo
import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 27/08/2016.
  */

case class ResourceInfo(money: Int, cc: Int)

trait ResourceManager extends TablesComponent with ResourceCalculatorComponent {

  import tables._
  import tables.profile.api._

  /*** initialize resource data
    *
    * @return created data
    *
    * @throws ResourceAlreadyInitializedError resouce data is already initialized
    */
  def initResource(data: ResourceInfo = initialResource)(implicit accountInfo: AccountInfo): DBIO[ResourceInfo] = {
    val dbData = ResourcesRow(accountId = accountInfo.id, money = data.money, cc = data.cc)
    val q = for {
      isExist <- Resources.filter(_.accountId === accountInfo.id).exists.result
      _ <- if (isExist) DBIO.failed(ResourceAlreadyInitializedError) else Resources += dbData
      d <- getResource
    } yield d
    q.transactionally
  }

  /*** get resource
    *
    * @return resource data
    *
    * @throws ResourceNotInitializedError resource data is not yet initialized
    */
  def getResource(implicit accountInfo: AccountInfo): DBIO[ResourceInfo] = {
    def toInfo(r: ResourcesRow): ResourceInfo = ResourceInfo(money = r.money, cc = r.cc)
    val q = for {
      data <- Resources.filter(_.accountId === accountInfo.id).result.headOption
      result <- if (data.isEmpty) DBIO.failed(ResourceNotInitializedError) else DBIO.successful(data.get)
    } yield toInfo(result)
    q.transactionally
  }

  /*** save resource (sql update)
    *
    * @param data resource data
    * @return update count
    *
    * @throws ResourceNotInitializedError resource data is not yet initialized
    */
  def overwriteResource(data: ResourceInfo)(implicit accountInfo: AccountInfo): DBIO[Int] = {
    def toRow(r: ResourceInfo): ResourcesRow = ResourcesRow(accountId = accountInfo.id, money = r.money, cc = r.cc)
    val q = for {
      _ <- getResource
      result <- Resources.filter(_.accountId === accountInfo.id).update(toRow(data))
    } yield result
    q.transactionally
  }

  /*** apply CC gain
    *
    * @param data current resource data
    * @param start start of target span
    * @param end end of target span
    * @return applied resource info data
    */
  def applyCcGain(data: ResourceInfo, start: ZonedDateTime, end: ZonedDateTime)(implicit accountInfo: AccountInfo): DBIO[ResourceInfo]= {

    import Implicits._

    def saveStamp(id: Int, time: ZonedDateTime): DBIO[Int] = for {
      exists <- CcGains.filter(_.accountId === id).exists.result
      result <- if (exists) CcGains.filter(_.accountId === id).map(_.lastUpdate).update(time) else CcGains += CcGainsRow(accountId = id, lastUpdate = time)
    } yield result

    resourceCalculator.ccGain(current = data.cc , start = start, end = end).map { ccValue =>
      import Implicits._
      val newData = data.copy(cc = ccValue)
      for {
        _ <- Resources.filter(_.accountId === accountInfo.id).update(toResourceRow(newData))
        _ <- saveStamp(id = accountInfo.id, time = end)
      } yield newData
    }.getOrElse(DBIO.successful(data))
  }

  private def toResourceRow(r: ResourceInfo)(implicit accountInfo: AccountInfo): ResourcesRow = ResourcesRow(accountId = accountInfo.id, money = r.money, cc = r.cc)


}

trait ResourceManagerComponent {
  val resourceManager: ResourceManager
}

trait HasResourceManager extends ResourceManagerComponent {
  val resourceManager = new ResourceManager
    with HasResourceCalculator
    with HasTables
}