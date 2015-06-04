package com.mjamesruggiero.taft

import scalaz._, Scalaz._
import spray.json._
import com.mjamesruggiero.taft.datatypes._
import scala.concurrent.duration._
import scalaz.concurrent.{Task, Strategy}
import scalaz.stream.{time, Process, async}
import org.joda.time.DateTime
import com.redis.RedisClientPool

object Taft extends App {
  import TaftJsonProtocol._

  implicit val scheduler = Strategy.DefaultTimeoutScheduler

  val queue = async.boundedQueue[Tweet](100)

  val rcp = new RedisClientPool("localhost", 6379)

  def mkRandomTweet: Tweet = {
    lazy val twit = TwitterUser("mjamesruggiero")
    lazy val dt = new DateTime

    def randomString(length: Int, strings: Seq[String]): String = {
      val tmpList = List.range(0, length)
      tmpList
        .map{ e => strings(util.Random.nextInt(strings.length)) }
        .mkString(" ")
    }

    val words: List[String] = List(
      "be", "happy", "car", "boat", "wondering",
      "love", "monkey", "fish", "swim", "act",
      "think", "the", "wonderful", "world",
      "adrenaline", "distillery", "affecter", "bipod",
      "bombproof", "centerline", "cloudlike", "commercialist",
      "criticised", "deadfall", "decontamination", "defoliating",
      "finding", "fixedly", "genolla", "homewood",
      "imputative", "ineducability", "inferrible", "informativeness",
      "intonate", "litoral", "madrilenian", "maybe",
      "mayflower", "mousetrap", "nonactual", "nonchalance",
      "nonofficial", "origin", "presswork",
      "pustulated", "realter", "reapportion", "stubby",
      "syndicate", "theosophical", "truehearted", "tryst",
      "turgently", "unspellable", "unveneered", "valiant", "whir"
    )
    Tweet(twit, randomString(50, words), Utils.dateTimeToTwitterString(dt))
  }

  def countTokens(l: List[String]) = l.groupBy(identity).mapValues(_.length)

  val saveTweet = (el: Tweet) =>
    for {
      success <- Storage.set(Keys.tweetKey(el), el.toJson.toString, rcp)
    } yield(el)

  val tokenizeTweet = (el: Tweet) => Task {
    Analyzer(el).tokenize
  }

  val processTokens = (l: List[String]) => Task {
    println(countTokens(l))
  }

  val enqueueProcess = time
                        .awakeEvery(60.millis)
                        .map(_ => mkRandomTweet)
                        .to(queue.enqueue)


  val dequeueProcess = queue
                        .dequeue
                        .evalMap(saveTweet)
                        .evalMap(tokenizeTweet)
                        .evalMap(processTokens)

  (enqueueProcess merge dequeueProcess).run.run
}
