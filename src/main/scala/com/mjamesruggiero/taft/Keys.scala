package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._
import org.joda.time.DateTime

object Keys {
  def tweetKey(tweet: Tweet): String  = {
    val tweetDate = Utils.parseDate(tweet.created_at)
    val inMillis = tweetDate match {
      case Some(dt: DateTime) => dt.getMillis
      case _ => new DateTime().getMillis
    }
    s"${tweet.user.screen_name}:${inMillis.toString}"
  }
}
