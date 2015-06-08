package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._
import com.redis.RedisClientPool
import org.joda.time.DateTime
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random
import scalaz._, Scalaz._
import scalaz.concurrent.{Task, Strategy}
import scalaz.stream.{time, Process, async}
import spray.json._

object Taft extends App {
  import TaftJsonProtocol._

  implicit val scheduler = Strategy.DefaultTimeoutScheduler

  val staticFile = "/Users/michaelruggiero/code/mr/taft/src/test/resources/tweets.json"

  val queue = async.boundedQueue[Tweet](100)

  def getRandomTweet(lot: List[Tweet]): Tweet = {
    val random = new Random
    lot(random.nextInt(lot.length))
  }

  val rcp = new RedisClientPool("localhost", 6379)

  def tweetList: List[Tweet] = {
    val fileString = Source.fromFile(staticFile).getLines.mkString
    val jsonVal = fileString.asJson
    val jsArr = jsonVal.asInstanceOf[JsArray]
    jsArr.convertTo[List[Tweet]]
  }

  val saveTweet = (el: Tweet) =>
    for {
      _ <- Storage.set(Keys.tweetKey(el), el.toJson.toString, rcp)
    } yield(el)

  def countTokens(l: List[String]) = l.groupBy(identity).mapValues(_.length)

  val tokenizeTweet = (el: Tweet) => Task {
    Analyzer(el).tokenize
  }

  val processTokens = (l: List[String]) => Task { println(countTokens(l)) }

  val enqueueProcess = time
                        .awakeEvery(60.millis)
                        .map(_ => getRandomTweet(tweetList))
                        .to(queue.enqueue)

  val dequeueProcess = queue
                        .dequeue
                        .evalMap(saveTweet)
                        .evalMap(tokenizeTweet)
                        .evalMap(processTokens)

  (enqueueProcess merge dequeueProcess).run.run
}
