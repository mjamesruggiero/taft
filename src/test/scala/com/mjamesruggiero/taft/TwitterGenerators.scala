package com.mjamesruggiero.taft

import org.scalacheck._
import spray.json._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import com.mjamesruggiero.taft.datatypes._

object TwitterGenerators {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen._

  def word(n: Int): Gen[String] = listOfN(n, alphaChar) map {_.mkString}

  val smallInt = choose(1, 7)

  val username: Gen[String] = for {
    i <- choose(2, 15)
    str <- word(i)
  } yield str

  val twitterUser: Gen[TwitterUser] = username map {TwitterUser(_)}

  val tweetText: Gen[String] = for {
    n <- choose(1, 20)
    words <- listOfN(n, wordOfRandomLength)
  } yield words.mkString(" ")

  def wordOfRandomLength = for {
    n <- smallInt
    w <- word(n)
  } yield w

  lazy val earliestDate = new DateTime(2005, 3, 26, 12, 0, 0, 0).getMillis
  lazy val latestDate = new DateTime(2015, 3, 26, 12, 0, 0, 0).getMillis

  val genDate = for {
    n <- Gen.choose(earliestDate, latestDate)
    v <- new DateTime(n)
    ds <- Utils.dateTimeToTwitterString(v)
  } yield ds

  val genTweet: Gen[Tweet] = for {
    user <- twitterUser
    text <- tweetText
    date <- genDate
  } yield Tweet(user, text, date)
}

