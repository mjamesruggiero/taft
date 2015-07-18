package com.mjamesruggiero.taft

import spray.json._
import scala.io._
import DefaultJsonProtocol._
import java.io.IOException


trait Fetcher {
  def fetchBody(url: String, params: Map[String, String]): String
}

case class AccessToken(consumerKey: String, consumerSecret: String, userKey: String, userSecret: String)

object AccessToken {
  import spray.json._
  import DefaultJsonProtocol._
  implicit val format: JsonFormat[AccessToken] = jsonFormat4(apply)
}

object TwitterAuthorization {
  val keysFile = "/keys.json"
  def accessSourceTokens: Option[AccessToken] = {
    val resource = getClass.getResourceAsStream(keysFile)
    val stream = scala.io.Source.fromInputStream(resource)
    try {
      val text: String = stream.getLines.mkString("")
      val tokens = text.asJson.convertTo[AccessToken]
      Some(tokens)
    } catch  {
      case e: IOException => None
    }
  }
}

class TweetFetcher extends Fetcher {
  import org.scribe.builder._
  import org.scribe.builder.api._
  import org.scribe.model._
  import org.scribe.oauth._

  def fetchBody(url: String, params: Map[String, String]): String = {
    import TwitterAuthorization._
    accessSourceTokens match {
        case Some(accessTokens) => {
          val service = new ServiceBuilder().provider(classOf[TwitterApi]).
            apiKey(accessTokens.consumerKey).
            apiSecret(accessTokens.consumerSecret).
            build()

          val taftToken = new Token(accessTokens.userKey, accessTokens.userSecret)
          val request = new OAuthRequest(Verb.GET, url)
          params.foreach { case (key, value) => request.addQuerystringParameter(key, value) }
          service.signRequest(taftToken, request)
          val response = request.send()
          response.getBody()
        }
      case _ => ""
    }
  }
}

object SearchInput {
  def apply(queryString: String, fetcher: Fetcher = new TweetFetcher): String = {
    val searchUrl = "https://api.twitter.com/1.1/search/tweets.json"
    def go(params: Map[String, String]): String = {
      fetcher.fetchBody(searchUrl, params)
    }
    go(Map("q" -> queryString, "result_type" -> "recent"))
  }
}

object TimelineInput {
  def apply(queryString: String, fetcher: Fetcher = new TweetFetcher): String = {
    val timelineUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json"
    val maxCount: String = "200"
    def go(params: Map[String, String]): String = {
      fetcher.fetchBody(timelineUrl, params)
    }
    go(Map("q" -> queryString, "count" -> maxCount, "exclude_replies" -> "true"))
  }
}


