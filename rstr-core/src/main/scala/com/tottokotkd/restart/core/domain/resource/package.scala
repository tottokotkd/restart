package com.tottokotkd.restart.core.domain

import com.tottokotkd.restart.core.util.RstrRuntimeException

/**
  * Created by tottokotkd on 08/09/2016.
  */
package object resource {

  val initialResource = ResourceInfo(money = 200, cc = 200)

  val ccPerMinute = 100

  sealed class ResourceManagerException(message: String = null, cause: Throwable = null) extends RstrRuntimeException(message, cause)
  object ResourceAlreadyInitializedError extends ResourceManagerException(message = "resource data is already initialized")
  object ResourceNotInitializedError extends ResourceManagerException(message = "resource data is not yet initialized")

}
