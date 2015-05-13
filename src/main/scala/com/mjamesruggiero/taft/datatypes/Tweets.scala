package com.mjamesruggiero.taft.datatypes

import spray.json._

case class TwitterUser(screen_name: String)
case class Tweet(user: TwitterUser, text: String)

object TaftJsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat1(TwitterUser)
  implicit val tweetFormat = jsonFormat2(Tweet)
}
