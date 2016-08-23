package com.tottokotkd.restart.core.util

/**
  * Created by tottokotkd on 16/08/2016.
  */

trait RstrResult {
  def getResultName: String
}

trait RstrFailure extends RstrResult with ScalaClassNameUtil { this: Throwable =>
  def getResultName: String = getSimpleName
}

class RstrRuntimeException(message: String = null, cause: Throwable = null) extends RuntimeException(message, cause) with RstrFailure

