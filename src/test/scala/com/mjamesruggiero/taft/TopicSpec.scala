package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.Topic._
import org.scalatest._

class TopicSpec extends FlatSpec with Matchers {
  "tf" should "count term frequency" in {
    val words = List(
      "bear", "bear",
      "goat", "goat", "goat", "goat", "goat", "goat",
      "monkey",
      "elephant"
    )
    val expected = 0.6
    tf("goat", words) should equal(expected)
  }


}
