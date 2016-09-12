package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import org.specs2._

/**
  * Created by tottokotkd on 11/09/2016.
  */

class ResourceCalculatorSpec extends mutable.Specification with HasResourceCalculator {

  "ResourceCalculatorSpec" >> {

    "ccGain" >> {

      val start = ZonedDateTime.now.truncatedTo(ChronoUnit.MINUTES)

      "(1) 0 cc + 5 min. = 500 cc" >> {
        val result = resourceCalculator.ccGain(current = 0, start = start, end = start.plusMinutes(5))
        result must beSome(500)
      }
      "(2.1) 0 cc + 5 min. 1 sec. = 500 cc" >> {
        val result = resourceCalculator.ccGain(current = 0, start = start, end = start.plusMinutes(5).plusSeconds(59))
        result must beSome(500)
      }
      "(2.2) 0 cc + 5 min. 59 sec. = 500 cc" >> {
        val result = resourceCalculator.ccGain(current = 0, start = start, end = start.plusMinutes(5).plusSeconds(59))
        result must beSome(500)
      }
      "(3) 0 cc + 0 min. = None" >> {
        val result = resourceCalculator.ccGain(current = 0, start = start, end = start)
        result must beNone
      }
      "(4) 1000 cc + 5 min. = None" >> {
        val result = resourceCalculator.ccGain(current = 1000, start = start, end = start)
        result must beNone
      }
      "(5) 1200 cc + 9 min. = None" >> {
        val result = resourceCalculator.ccGain(current = 1200, start = start, end = start)
        result must beNone
      }
      "(6) 725 cc + 2 min. = 925" >> {
        val result = resourceCalculator.ccGain(current = 725, start = start, end = start.plusMinutes(2))
        result must beSome(925)
      }
      "(7) 725 cc + 3 min. = 1000" >> {
        val result = resourceCalculator.ccGain(current = 725, start = start, end = start.plusMinutes(3))
        result must beSome(1000)
      }
      "(8) 725 cc + 1 h. 3 min. = 1000" >> {
        val result = resourceCalculator.ccGain(current = 725, start = start, end = start.plusHours(1).plusMinutes(3))
        result must beSome(1000)
      }

    }
  }

}