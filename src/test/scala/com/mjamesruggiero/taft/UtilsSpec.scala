package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.Stemmer
import com.mjamesruggiero.taft.Utils
import org.scalacheck._
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object UtilsSpec extends Properties("Utils") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen
  import org.scalacheck._
  import Gen._

  object DateFns {
    def dateTimeToTwitterString(dateTime: DateTime): String = {
      val patString = "E MMM d HH:mm:ss Z yyyy"
      val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString);
      dtf.print(dateTime)
    }
  }

  val genDate = for {
    n <- Gen.choose(1, 100000)
    v <- new DateTime(n)
  } yield v

  import DateFns._

  property("can parse a date") = forAll(genDate) { arbitraryDate =>
    val twitterString = dateTimeToTwitterString(arbitraryDate)
    val ret: String = Utils.parseDate(twitterString) match {
        case Some(dt) => dateTimeToTwitterString(dt)
        case _  => dateTimeToTwitterString(DateTime.now)
    }
    ret.equals(twitterString)
  }
}
