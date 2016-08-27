package com.tottokotkd.restart.core.domain.resource

import com.tottokotkd.restart.core.model.{HasTables, TablesComponent}

/**
  * Created by tottokotkd on 27/08/2016.
  */
trait ResourceManager extends TablesComponent{

}

trait ResourceManagerComponent {
  val resourceManager: ResourceManager
}

trait HasResourceManager extends ResourceManagerComponent {
  val resourceManager = new ResourceManager with HasTables
}