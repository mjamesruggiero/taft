import com.mjamesruggiero.taft.datatypes._

import spray.json._
import scala.io.Source
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {
  import TaftJsonProtocol._

  def fixture = new {
    val testInputFile = "/tweets.json"
    val fileString = Source.fromURL(getClass.getResource(testInputFile)).getLines.mkString
  }

  "Search results" should "be parseable as tweets" in {
    val f = fixture
    val jsonVal = f.fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    val tweets = jsArr.convertTo[List[Tweet]]
    val expected = 20
    tweets.size should be (expected)
  }

  "Search results" should "contain expected messages" in {
    val f = fixture
    val jsonVal = f.fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    val tweets = jsArr.convertTo[List[Tweet]]
    val messages = tweets.map(_.text).mkString(",")
    "reactive streams".r.findAllIn(messages).length should be (1)
  }
}
