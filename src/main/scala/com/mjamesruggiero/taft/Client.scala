package com.mjamesruggiero.taft

object Client {
  import org.scribe.builder._
  import org.scribe.builder.api._
  import org.scribe.model._
  import org.scribe.oauth._

  def getClient(apiKey: String, apiSecret: String): Token = {
    val service = new ServiceBuilder().provider(classOf[TwitterApi]).
                                       apiKey(apiKey).
                                       apiSecret(apiSecret).
                                       build()

    service.getRequestToken();
  }
}

