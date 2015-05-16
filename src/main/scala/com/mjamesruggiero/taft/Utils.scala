package com.mjamesruggiero.taft

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object Utils {
  def parseDate(dateString: String): Option[DateTime] = {
    val patString = "E MMM d HH:mm:ss Z yyyy"
    val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString)
    dtf.parseDateTime(dateString) match {
        case dt: DateTime => Some(dt)
        case _ => None
    }
  }
}

