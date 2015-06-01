import com.mjamesruggiero.taft._

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

  val tweetsInASearch = 20

  val tweetSeq: Gen[SearchResults] = for {
    statuses <- Gen.listOfN(tweetsInASearch, tweet)
  } yield (SearchResults(statuses))

  property("can build tweet keys") = forAll(tweet) { t =>
    val date = DateTime.parse(t.created_at).getMillis
    val expected = s"${t.user.screen_name}:${date.toString}"
    val ret = com.mjamesruggiero.taft.Keys.tweetKey(t)
    ret == expected
  }
}
