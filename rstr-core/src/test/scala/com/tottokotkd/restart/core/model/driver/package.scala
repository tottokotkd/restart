package com.tottokotkd.restart.core.model

/**
  * Created by tottokotkd on 26/08/2016.
  */
package object driver extends HasTestDriver{

  import driver.run
  import tables._
  import tables.profile.api._

  val twiIdGen = new TwitterIdGenerator(current = run(TwitterAccounts.map(_.twitterId).min.result).map(_ - 1).getOrElse(-1))
}

class TwitterIdGenerator(var current: Int) {
  def generate() = {
    current = current - 1
    current.toString
  }
}