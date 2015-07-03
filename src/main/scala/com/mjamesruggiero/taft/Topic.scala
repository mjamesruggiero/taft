package com.mjamesruggiero.taft

object Topic {
  def tf(word: String, tokens: List[String]): Double = {
    val count = tokens.count(_ == word).toDouble
    val len = tokens.length.toDouble
    val tf = count / tokens.length
    println(s"count is ${count} and length is ${len} and tf is ${tf}")
    tf
  }

  def nContaining(word: String, documents: List[List[String]]): Int  = {
    documents.filter(x => x contains word).length
  }

  def idf(word: String, documents: List[List[String]]): Double = {
    scala.math.log(documents.length / (1 + nContaining(word, documents)))
  }
}
