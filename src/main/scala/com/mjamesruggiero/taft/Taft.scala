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

  val persistTokens = (tokens: Map[String, Int]) => {
    val key = "words"
    tokens.toList.foreach { (p) =>
      Database.zincrby(key, p._2, p._1, rcp).run
    }
  }

  def saveTokens= (el: Tweet) => Task {
    val tokens = Analyzer(el).tokenize
    val tokenMap = countTokens(tokens)
    persistTokens(tokenMap)
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
