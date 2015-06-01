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

  object DateFns {
    lazy val patString = "E MMM d HH:mm:ss Z yyyy"

    def dateTimeToTwitterString(dateTime: DateTime): String = {
      val dtf: DateTimeFormatter = DateTimeFormat.forPattern(patString);
      dtf.print(dateTime)
    }
  }

  import DateFns._

  lazy val earliestDate = new DateTime(2005, 3, 26, 12, 0, 0, 0).getMillis

  val genDate = for {
    n <- Gen.choose(1, 100000)
    v <- new DateTime(n) suchThat (_.getMillis > earliestDate)
    ds <- dateTimeToTwitterString(v)
  } yield ds

  val tweet: Gen[Tweet] = for {
    user <- twitterUser
    text <- tweetText
    date <- genDate
  } yield Tweet(user, text, date)
}

