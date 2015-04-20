import com.mjamesruggiero.taft.Stemmer

import org.scalacheck._

object StemmerSpec extends Properties("Stemmer") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen

  val vowels = Gen.oneOf('a', 'e', 'i', 'o', 'u')

  val wordsWithVowels = Gen.oneOf("fool", "money", "monkey", "honey")

  val wordsWithoutVowels = Gen.oneOf("fff", "ggg", "ttt", "qqq")

  property("vowels") = forAll(vowels) { (c: Char) =>
    Stemmer.isVowel(c) == true
  }

  property("containsVowels sees vowels") = forAll(wordsWithVowels) { (w: String) =>
    Stemmer.containsVowel(w) == true
  }

  property("containsVowels doesn't see consonants") = forAll(wordsWithoutVowels) {
      (w: String) => Stemmer.containsVowel(w) == false
  }
}
