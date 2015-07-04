package com.mjamesruggiero.taft

case class WordScore(word: String, score: Double)

object Topic {
  def tf(word: String, tokens: List[String]): Double = {
    val count = tokens.count(_ == word).toDouble
    val len = tokens.length.toDouble
    count / tokens.length
  }

  def nContaining(word: String, documents: List[List[String]]): Double = {
    documents.map { doc =>
      if (doc contains word) { 1.0 } else { 0.0 }
    }.foldLeft(0.0)(_ + _)
  }

  def idf(word: String, documents: List[List[String]]): Double = {
    val containing = nContaining(word, documents)
    scala.math.log(documents.length.toDouble / (1.0 + containing))
  }

  def tfidf(word: String, tokens: List[String], documents: List[List[String]]): Double = {
    tf(word, tokens) * idf(word, documents)
  }

  def sortedWordScores(scores: List[WordScore]): List[WordScore] = {
    scores.sortWith(_.score < _.score)
  }
}
