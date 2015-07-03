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

  "nContaining" should "count the number of token lists containing word" in {
    val documents = List(
      List("car", "boat", "train"),
      List("car", "plane", "train"),
      List("car", "boat", "hovercraft"))
    nContaining("boat", documents) should equal(2)
  }

  "idf" should "count the log of documents over 1 + nContaining" in {
    val documents = List(
      List("car", "boat", "train"),
      List("car", "plane", "train"),
      List("car", "plane", "train"),
      List("car", "plane", "train"),
      List("car", "plane", "train"),
      List("car", "plane", "train"),
      List("car", "plane", "train"),
      List("car", "boat", "hovercraft"))
    val expected = 0.6931471805599453
    idf("boat", documents) should equal(expected)
  }
}
