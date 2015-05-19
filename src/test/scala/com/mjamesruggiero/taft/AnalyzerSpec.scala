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

  property("can tokenize a tweet") = forAll(tweet) { t =>
    val tokens = Analyzer(t).tokenize.toSet
    val tweetWords = t.text.toLowerCase.split(" ").toSet
    val intersection = tokens & tweetWords
    tokens.size  match {
        case 0 => true
        case _ => 0 < intersection.size
    }
  }
}
