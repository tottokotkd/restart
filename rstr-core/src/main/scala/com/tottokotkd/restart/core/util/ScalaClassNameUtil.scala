package com.tottokotkd.restart.core.util

/**
  * Created by tottokotkd on 17/08/2016.
  */

trait ScalaClassNameUtil {
  def removeLastDollar(s: String) = s.replaceFirst("\\$$$$", "")
  def getName = removeLastDollar(this.getClass.getName)
  def getSimpleName = removeLastDollar(this.getClass.getSimpleName)
  def getTypeName = removeLastDollar(this.getClass.getTypeName)
  def getCanonicalName = removeLastDollar(this.getClass.getCanonicalName)
}
