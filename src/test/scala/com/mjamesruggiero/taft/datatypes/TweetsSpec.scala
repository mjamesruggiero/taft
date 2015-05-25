import com.mjamesruggiero.taft.datatypes._

import org.scalacheck._
import spray.json._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import scala.io.Source
import Prop._

object TweetsSpec extends Properties("Tweets") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen._
  import TaftJsonProtocol._
  import TwitterGenerators._

  val tweetsInASearch = 20

  val tweetSeq: Gen[SearchResults] = for {
    statuses <- Gen.listOfN(tweetsInASearch, tweet)
  } yield (SearchResults(statuses))

  property("can parse tweets") = forAll(tweet) { t =>
    val asString = s"""{ "user": { "screen_name": "${t.user}" }, "text": "${t.text}", "created_at": "${t.created_at}" }"""
    val testTweet = asString.asJson.convertTo[Tweet]
    testTweet.user.screen_name == t.user.screen_name
    testTweet.text == t.text
  }

  property("can parse a seq of tweets") = forAll(tweetSeq) { seqOfTweets =>
    val testJson = seqOfTweets.toJson
    val res = testJson.convertTo[SearchResults]
    res.statuses.size == tweetsInASearch
  }
}
