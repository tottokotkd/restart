package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime

import com.tottokotkd.restart.core.domain.account.AccountInfo
import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by tottokotkd on 27/08/2016.
  */

case class ResourceInfo(money: Int, cc: Int)

trait ResourceManager extends TablesComponent {

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
    * @throws ResourceNotInitializedError resouce data is not yet initialized
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
    * @throws ResourceNotInitializedError resouce data is not yet initialized
    */
  def overwriteResoruce(data: ResourceInfo)(implicit accountInfo: AccountInfo): DBIO[Int] = {
    def toRow(r: ResourceInfo): ResourcesRow = ResourcesRow(accountId = accountInfo.id, money = r.money, cc = r.cc)
    val q = for {
      _ <- getResource
      result <- Resources.filter(_.accountId === accountInfo.id).update(toRow(data))
    } yield result
    q.transactionally
  }

}

trait ResourceManagerComponent {
  val resourceManager: ResourceManager
}

trait HasResourceManager extends ResourceManagerComponent {
  val resourceManager = new ResourceManager with HasTables
}