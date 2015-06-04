package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.Analyzer
import org.scalacheck._
import spray.json._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import com.mjamesruggiero.taft.datatypes._

object AnalyzerSpec extends Properties("Analyzer") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen._
  import TaftJsonProtocol._
  import com.mjamesruggiero.taft.TwitterGenerators._

  val tweetsInASearch = 20

  val tweetSeq: Gen[SearchResults] = for {
    statuses <- Gen.listOfN(tweetsInASearch, genTweet)
  } yield (SearchResults(statuses))

  property("can tokenize a tweet") = forAll(genTweet) { t =>
    val tokens = Analyzer(t).tokenize.toSet
    val tweetWords = t.text.toLowerCase.split(" ").toSet
    val intersection = tokens & tweetWords
    tokens.size  match {
        case 0 => true
        case _ => 0 < intersection.size
    }
  }
}
