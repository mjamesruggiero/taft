package com.mjamesruggiero.taft

import scalaz._, Scalaz._
import com.mjamesruggiero.taft.datatypes._
import scala.concurrent.duration._
import scalaz.concurrent.{Task, Strategy}
import scalaz.stream.{time, Process, async}

object Taft extends App {
  import TaftJsonProtocol._

  implicit val scheduler = Strategy.DefaultTimeoutScheduler

  val queue = async.boundedQueue[Tweet](100)

  def mkRandomTweet: Tweet = {
    lazy val twit = TwitterUser("mjamesruggiero")
    lazy val dt = "Sat May 09 05:22:23 +0000 2015"

    def randomString(length: Int, strings: Seq[String]): String = {
      val tmpList = List.range(0, length)
      tmpList
        .map{ e => strings(util.Random.nextInt(strings.length)) }
        .mkString(" ")
    }

    val words: List[String] = List("be", "happy", "monkey", "the", "wonderful", "world")
    Tweet(twit, randomString(10, words), dt)
  }

  val enqueueProcess = time
                        .awakeEvery(6000.millis)
                        .map(_ => mkRandomTweet)
                        .to(queue.enqueue)

  val dequeueProcess = queue
                        .dequeueAvailable
                        .flatMap(el => Process.eval_(Task {
                          println(el.flatMap(Analyzer(_).tokenize))
                        }))


  (enqueueProcess merge dequeueProcess).run.run
}
