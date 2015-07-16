package com.mjamesruggiero.taft

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import scala.math._

object Utils {

  def parseDate(dateString: String): Option[DateTime] = {
    val patString = "E MMM d HH:mm:ss Z yyyy"
    val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString)
    dtf.parseDateTime(dateString) match {
        case dt: DateTime => Some(dt)
        case _ => None
    }
  }

  def dateTimeToTwitterString(dateTime: DateTime): String = {
    val patString = "E MMM d HH:mm:ss Z yyyy"
    val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString);
    dtf.print(dateTime)
  }

  def distance(first: String, second: String) = {
    def minimum(i: Int, j: Int, k: Int) = min(min(i, j), k)

    val dist =
      Array.tabulate(second.length + 1, first.length + 1) {

        (j, i) => if (j==0) i
                  else if (i==0) j
                  else 0
      }

    for(j<-1 to second.length; i<-1 to first.length) {
      dist(j)(i) = if(second(j - 1) == first(i - 1)) {
                      dist(j - 1)(i - 1)
                   } else {
                      minimum(dist(j - 1)(i) + 1,
                              dist(j)(i - 1) + 1,
                              dist(j - 1)(i - 1) + 1)
                   }

    }

    dist(second.length)(first.length)
  }
}

