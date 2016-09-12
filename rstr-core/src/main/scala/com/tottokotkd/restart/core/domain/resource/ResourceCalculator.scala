package com.tottokotkd.restart.core.domain.resource

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

import com.tottokotkd.restart.core.domain.GainPerMinute

import scala.util.{Failure, Try}

/**
  * Created by tottokotkd on 11/09/2016.
  */
trait ResourceCalculator {

  /*** calc CC gain (None if interval is null)
    *
    * @param start start of range
    * @param end end of range
    * @return cc gain value
    */
  def ccGain(current: Int, start: ZonedDateTime, end: ZonedDateTime): Option[Int] = gainByMinutes(current, ccGainPerMinute, start, end)

  private def gainByMinutes(current: Int, params: GainPerMinute, start: ZonedDateTime, end: ZonedDateTime): Option[Int] =
    if (current >= params.maximum) None
    else getInterval(start, end, ChronoUnit.MINUTES)
      .map{ minutes => math.min(params.maximum, current + params.value * minutes) }

  private def getInterval(a: ZonedDateTime, b: ZonedDateTime, c: ChronoUnit): Option[Int] =
    a.truncatedTo(c).until(b.truncatedTo(c), c).toInt match {
      case n if n <= 0 => None
      case m => Some(m)
    }

}

trait ResourceCalculatorComponent {
  val resourceCalculator: ResourceCalculator
}

trait HasResourceCalculator  extends ResourceCalculatorComponent{
  val resourceCalculator = new ResourceCalculator {}
}

