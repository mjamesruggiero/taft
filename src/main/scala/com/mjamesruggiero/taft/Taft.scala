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
    val searchResult = TimelineInput("count=200")
    val jsonVal = searchResult.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    jsArr.convertTo[List[Tweet]]
  }

  def countTokens(l: List[String]): Map[String, Int] =
    l.groupBy(identity).mapValues(_.length)

  val saveTweet = (el: Tweet) =>
    for {
      _ <- Database.set(Keys.tweetKey(el), el.toJson.toString, rcp)
    } yield(el)

  val persistToken = (token: String, count: Int) => {
    for {
      _ <- Database.zincrby("words", count.toDouble, token, rcp)
    } yield((token, count))
  }

  def saveTokens= (el: Tweet) => Task {
    val tokens = Analyzer(el).tokenize
    val tokenMap = countTokens(tokens)
    tokenMap.map { e =>
      persistToken(e._1, e._2)
    }
  }

  def process: Unit = {
    val ts = tweetList
    for (tweet <- ts) {
      saveTokens(tweet).run
      saveTweet(tweet).run
    }
  }
  process
}
