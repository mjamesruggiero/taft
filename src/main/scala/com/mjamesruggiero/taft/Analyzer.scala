package com.mjamesruggiero.taft

import com.mjamesruggiero.taft.datatypes._

class Analyzer(tweet: Tweet) {
  def tokenize: List[String] = Tokenizer(tweet.text).eachWord

  def stem: List[String] = Tokenizer(tweet.text).eachWord.map(Stemmer.stem(_))
}

object Analyzer {
  def apply(tweet: Tweet): Analyzer = new Analyzer(tweet)

  class TweetPool(size: Int, contents: Seq[Tweet] = Vector()) {

    private def copy(contents: Seq[Tweet]) = new TweetPool(size, contents)

    def findBest: Option[(Tweet, TweetPool)] =
      contents sortBy {_.text.length} match {
        case Seq() => None
        case Seq(head, tail@ _*) => Some((head, copy(tail)))
      }
  }
}
