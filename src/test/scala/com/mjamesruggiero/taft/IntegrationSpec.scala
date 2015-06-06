package com.mjamesruggiero.taft

import com.mjamesruggiero.taft._
import com.mjamesruggiero.taft.datatypes._
import spray.json._
import scala.io.Source
import org.scalatest._
import scalaz._, Scalaz._
import scalaz.concurrent.Task

class IntegrationSpec extends FlatSpec with Matchers {
  import TaftJsonProtocol._

  def fixture = new {
    val testInputFile = "/tweets.json"
    val fileString = Source.fromURL(getClass.getResource(testInputFile)).getLines.mkString
    val jsonVal = fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    val tweets = jsArr.convertTo[List[Tweet]]
  }

  "Search results" should "be parseable as tweets" in {
    val f = fixture
    val expected = 20
    f.tweets.size should be (expected)
  }

  "Search results" should "contain expected messages" in {
    val f = fixture
    val messages = f.tweets.map(_.text).mkString(",")
    "reactive streams".r.findAllIn(messages).length should be (1)
  }

  "Tasks" should "generate tokenized strings" in {
    val mr = TwitterUser("mjamesruggiero")
    val dateStr = "Sat May 09 05:22:23 +0000 2015"

    val statuses = List(
      "fake message",
      "another fake message",
      "yet another fake message",
      "fourth in a series of fake messages",
      "fifth in a series of fake messages"
    )

    val tweets = statuses.map { m => Tweet(mr, m, dateStr) }.toList
    val tasks = tweets.map(t => Task { Analyzer(t).tokenize })
    val tokens = Task.gatherUnordered(tasks).run.flatMap(identity)
    val expectedSize = 14
    tokens.size should be (expectedSize)
  }
}
