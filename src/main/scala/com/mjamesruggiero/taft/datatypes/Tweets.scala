package com.mjamesruggiero.taft.datatypes

import spray.json._

case class TwitterUser(screen_name: String)
case class Tweet(user: TwitterUser, text: String)
case class SearchResults(statuses: List[Tweet])

object TaftJsonProtocol extends DefaultJsonProtocol {
  implicit val userFormat = jsonFormat1(TwitterUser)
  implicit val tweetFormat = jsonFormat2(Tweet)
  implicit val searchResultsFormat = jsonFormat1(SearchResults.apply)
}
