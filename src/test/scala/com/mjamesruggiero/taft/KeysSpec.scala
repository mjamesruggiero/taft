package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.Utils
import com.mjamesruggiero.taft.datatypes._
import org.scalacheck._
import spray.json._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import scala.io.Source
import Prop._

object KeysSpec extends Properties("Keys") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen._
  import TaftJsonProtocol._
  import TwitterGenerators._

  property("can build tweet keys") = forAll(genTweet) { t =>
    val dt = Utils.parseDate(t.created_at)
    val millis = dt match {
        case Some(dateTime) => dateTime.getMillis
        case _ => 0
    }
    val expected = s"${t.user.screen_name}:${millis.toString}"
    val ret = com.mjamesruggiero.taft.Keys.tweetKey(t)
    ret == expected
  }
}
