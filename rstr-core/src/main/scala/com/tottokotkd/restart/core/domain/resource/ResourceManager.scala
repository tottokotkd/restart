package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime

import com.tottokotkd.restart.core.domain.account.AccountInfo
import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}
import slick.lifted.TableQuery

import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 27/08/2016.
  */

case class ResourceInfo(money: Int, cc: Int)
case class ResourceUpdateData(resource: ResourceInfo, stamps: HashMap[QueryTable, slick.dbio.DBIO[Int]] = HashMap())

trait ResourceManager extends TablesComponent with ResourceCalculatorComponent {

  import tables._
  import tables.profile.api._

  /*** initialize resource data
    *
    * @return created data
    *
    * @throws ResourceAlreadyInitializedError resouce data is already initialized
    */
  def initResource(stamp: ZonedDateTime, data: ResourceInfo = initialResource)(implicit accountInfo: AccountInfo): DBIO[ResourceInfo] = {
    import Implicits.toSqlTimestamp
    val q = for {
      isExist <- Resources.filter(_.accountId === accountInfo.id).exists.result
      _ <- if (isExist) DBIO.failed(ResourceAlreadyInitializedError) else for {
        _ <- Resources += ResourcesRow(accountId = accountInfo.id, money = data.money, cc = data.cc)
        _ <- DefaultGainLogs += DefaultGainLogsRow(accountId = accountInfo.id, lastUpdate = stamp)
      } yield ()
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

  /*** save resource
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

  /*** default routine to update resource (this DOES NOT initialize resouce data)
    *
    * @return latest resource data
    */
  def defaultUpdate(target: ZonedDateTime)(implicit accountInfo: AccountInfo): DBIO[ResourceInfo] = {
    import Implicits.toZonedDateTime

    val q = for {
      resource <- getResource
      lastUpdate <- DefaultGainLogs.filter(_.accountId === accountInfo.id).map(_.lastUpdate).result.head
      latestData <- {
        val ccUpdated = gainCc(data = ResourceUpdateData(resource = resource), start = lastUpdate, end = target)
        applyUpdate(ccUpdated)
      }
    } yield latestData
    q.transactionally
  }

  /*** apply CC gain
    *
    * @param data current resource data
    * @param start start of target span
    * @param end end of target span
    * @return applied resource info data
    */
  def gainCc(data: ResourceUpdateData, start: ZonedDateTime, end: ZonedDateTime)(implicit accountInfo: AccountInfo): ResourceUpdateData =
    resourceCalculator.ccGain(current = data.resource.cc, start = start, end = end).map { ccValue =>
      data.copy(
        resource = data.resource.copy(cc = ccValue),
        stamps = data.stamps + (DefaultGainLogTable -> saveDefaultGainLog(time = end))
      )
    }.getOrElse(data)

  def applyUpdate(data: ResourceUpdateData)(implicit accountInfo: AccountInfo): DBIO[ResourceInfo] = for {
    _ <- DBIO.sequence(data.stamps.values.toSeq)
    result <- overwriteResource(data.resource)
  } yield data.resource

  private def toResourceRow(r: ResourceInfo)(implicit accountInfo: AccountInfo): ResourcesRow = ResourcesRow(accountId = accountInfo.id, money = r.money, cc = r.cc)

  private def saveDefaultGainLog(time: ZonedDateTime)(implicit accountInfo: AccountInfo): DBIO[Int] = {
    import Implicits.toSqlTimestamp
    for {
      exists <- DefaultGainLogs.filter(_.accountId === accountInfo.id).exists.result
      result <- if (exists) DefaultGainLogs.filter(_.accountId === accountInfo.id).map(_.lastUpdate).update(time) else DefaultGainLogs += DefaultGainLogsRow(accountId = accountInfo.id, lastUpdate = time)
    } yield result
  }

}

trait ResourceManagerComponent {
  val resourceManager: ResourceManager
}

trait HasResourceManager extends ResourceManagerComponent {
  val resourceManager = new ResourceManager
    with HasResourceCalculator
    with HasTables
}