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
    val expected = 0.9808292530117262
    idf("boat", documents) should equal(expected)
  }

  "tfidf" should "calculate the product of tf and if" in {
    val doc1 = Tokenizer("""Python is a 2000 made-for-TV horror movie directed by Richard
    Clabaugh. The film features several cult favorite actors, including William
    Zabka of The Karate Kid fame, Wil Wheaton, Casper Van Dien, Jenny McCarthy,
    Keith Coogan, Robert Englund (best known for his role as Freddy Krueger in the
    A Nightmare on Elm Street series of films), Dana Barron, David Bowe, and Sean
    Whalen. The film concerns a genetically engineered snake, a python, that
    escapes and unleashes itself on a small town. It includes the classic final
    girl scenario evident in films like Friday the 13th. It was filmed in Los Angeles,
    California and Malibu, California. Python was followed by two sequels: Python
    II (2002) and Boa vs. Python (2004), both also made-for-TV films.""").eachWord

    val doc2 = Tokenizer("""Python, from the Greek word (πύθων/πύθωνας), is a genus of
    nonvenomous pythons[2] found in Africa and Asia. Currently, 7 species are
    recognised.[2] A member of this genus, P. reticulatus, is among the longest
    snakes known.""").eachWord

    val doc3 = Tokenizer("""The Colt Python is a .357 Magnum caliber revolver formerly
    manufactured by Colt's Manufacturing Company of Hartford, Connecticut.
    It is sometimes referred to as a "Combat Magnum".[1] It was first introduced
    in 1955, the same year as Smith &amp; Wesson's M29 .44 Magnum. The now discontinued
    Colt Python targeted the premium revolver market segment. Some firearm
    collectors and writers such as Jeff Cooper, Ian V. Hogg, Chuck Hawks, Leroy
    Thompson, Renee Smeets and Martin Dougherty have described the Python as the
    finest production revolver ever made.""").eachWord

    val doc1Scores = doc1.map { word =>
      val score = tfidf(word, doc1, List(doc1, doc2, doc3))
      WordScore(word, score)
    }.toList

    val sorted = doc1Scores.sortWith(_.score < _.score)
    val highest = sorted.last.word
    highest should equal("films")
  }
}
