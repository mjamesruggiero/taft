import com.mjamesruggiero.taft.datatypes._

import spray.json._
import scala.io.Source
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {

  "Search results" should "be parseable as tweets" in {
    val testInputFile = "/tweets.json"
    val fileString = Source.fromURL(getClass.getResource(testInputFile)).getLines.mkString
    val jsonVal = fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    val expected = 20
    jsArr.elements.size should be (expected)
  }

}
