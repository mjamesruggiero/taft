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

object Taft {
  import TaftJsonProtocol._

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
      _ <- saveTokens(el)
    } yield(el)

  def saveTokens= (el: Tweet) => Task {
    val key = "words"
    countTokens(Analyzer(el).tokenize).foreach {
      case (token, count) => Database.zincrby(key, count.toDouble, token, rcp).run
    }
  }

  def saveTopicScore = (token: String, score: Double) => Task {
    val key = "topics"
    Database.zincrby(key, score, token, rcp).run
  }

  def calculateTerm(tweet: Tweet, tweets: List[Tweet], threshold: Double): Unit = {
    val docs = tweets.map(Analyzer(_).tokenize).toList
    val tokens = Analyzer(tweet).tokenize
    tokens.foreach { t =>
      val score = Topic.tfidf(t, tokens, docs)
      if (score >= threshold) {
        saveTopicScore(t, score)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val ts = tweetList
    for (tweet <- ts) {
      saveTweet(tweet).run
      calculateTerm(tweet, ts, 0.06)
    }
  }
}
