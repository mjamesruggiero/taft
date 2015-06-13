package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._
import com.redis.RedisClientPool
import org.joda.time.DateTime
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random
import scalaz._, Scalaz._
import scalaz.concurrent.{Task, Strategy}
import scalaz.stream.{time, Process, Process1, async}
import spray.json._

object Taft extends App {
  import TaftJsonProtocol._

  implicit val scheduler = Strategy.DefaultTimeoutScheduler

  val staticFile = "/Users/michaelruggiero/code/mr/taft/src/test/resources/tweets.json"

  case class SearchResults(tweets: Seq[Tweet])

  def resultsToTweets: Process1[SearchResults, Tweet] =
    Process.await1 flatMap { results: SearchResults =>
      Process.emitAll(results.tweets)
    }

  def getRandomTweet(lot: List[Tweet]): Tweet = {
    val random = new Random
    lot(random.nextInt(lot.length))
  }

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

  def countTokens(l: List[String]) = l.groupBy(identity).mapValues(_.length)

  val tokenizeTweet = (el: Tweet) => Task {
    Analyzer(el).tokenize
  }

  val processTokens = (l: List[String]) => Task { println(countTokens(l)) }

  val queue = async.boundedQueue[Tweet](100)

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
