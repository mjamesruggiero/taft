import com.mjamesruggiero.taft.datatypes._

import org.scalacheck._
import spray.json._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

object TweetsSpec extends Properties("Tweets") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen._
  import TaftJsonProtocol._

  def word(n: Int): Gen[String] = listOfN(n, alphaChar) map {_.mkString}

  val tweetsInASearch = 20

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

  object DateFns {
    def dateTimeToTwitterString(dateTime: DateTime): String = {
      val patString = "E MMM d HH:mm:ss Z yyyy"
      val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString);
      dtf.print(dateTime)
    }
  }

  import DateFns._

  val genDate = for {
    n <- Gen.choose(1, 100000)
    v <- new DateTime(n)
    ds <- dateTimeToTwitterString(v)
  } yield ds

  val tweet: Gen[Tweet] = for {
    user <- twitterUser
    text <- tweetText
    date <- genDate
  } yield Tweet(user, text, date)

  val tweetSeq: Gen[SearchResults] = for {
    statuses <- Gen.listOfN(tweetsInASearch, tweet)
  } yield (SearchResults(statuses))

  property("can parse tweets") = forAll(tweet) { t =>
    val asString = s"""{ "user": { "screen_name": "${t.user}" }, "text": "${t.text}", "date": "${t.date}" }"""
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
