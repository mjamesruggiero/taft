package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._
import com.redis.RedisClientPool
import org.joda.time.DateTime
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random
import scalaz._, Scalaz._
import scalaz.concurrent.{Task, Strategy}
import scalaz.stream._
import spray.json._

object Taft extends App {
  import TaftJsonProtocol._

  implicit val scheduler = Strategy.DefaultTimeoutScheduler

  val rcp = new RedisClientPool("localhost", 6379)

  def tweetList: List[Tweet] = {
    import com.mjamesruggiero.taft.TimelineInput
    val fileString = TimelineInput("limit=100")
    val jsonVal = fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    jsArr.convertTo[List[Tweet]]
  }

  val saveTweet = (el: Tweet) =>
    for {
      _ <- Database.set(Keys.tweetKey(el), el.toJson.toString, rcp)
    } yield(el)

  val tokenizeTweet = (el: Tweet) => Task {
    Analyzer(el).tokenize
  }

  def process: Unit = {
    for (tweet <- tweetList) {
      saveTweet(tweet).run
    }
  }
  process
}
