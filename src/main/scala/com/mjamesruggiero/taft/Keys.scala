package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._
import org.joda.time.DateTime

object Keys {
  def tweetKey(tweet: Tweet): String  = {
    val inMillis = DateTime.parse(tweet.created_at).getMillis
    s"${tweet.user.screen_name}:${inMillis.toString}"
  }
}
