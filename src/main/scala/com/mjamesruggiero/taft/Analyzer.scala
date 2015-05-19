package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._

class Analyzer(tweet: Tweet) {
  def tokenize: List[String] = Tokenizer(tweet.text).eachWord

  def stem: List[String] = Tokenizer(tweet.text).eachWord.map(Stemmer.stem(_))
}

object Analyzer {
  def apply(tweet: Tweet): Analyzer = new Analyzer(tweet)
}
