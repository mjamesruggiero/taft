import com.mjamesruggiero.taft.Stemmer

import org.scalacheck._

object StemmerSpec extends Properties("Stemmer") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen

  val vowels = Gen.oneOf('a', 'e', 'i', 'o', 'u')

  // TODO make these generators that actually
  // represent a property
  val wordsWithVowels = Gen.oneOf("fool", "money", "monkey", "honey")

  val wordsWithoutVowels = Gen.oneOf("fff", "ggg", "ttt", "qqq")

  val wordsThatAreCVC = Gen.oneOf("habit", "pet", "bergamot", "ergot", "lap")

  val wordsThatAreNotCVC = Gen.oneOf("money", "rent", "fool", "desk", "if")

  val cvcTwos = Gen.oneOf("lantern", "dipthong", "leapfrog", "sheepskin")

  property("vowels") = forAll(vowels) { (c: Char) =>
    Stemmer.isVowel(c) == true
  }

  property("containsVowels sees vowels") = forAll(wordsWithVowels) { (w: String) =>
    Stemmer.containsVowel(w) == true
  }

  property("containsVowels doesn't see consonants") = forAll(wordsWithoutVowels) {
    (w: String) => Stemmer.containsVowel(w) == false
  }

  property("endsWithConsonantVConsonant finds CVC") = forAll(wordsThatAreCVC) {
    (w: String) => Stemmer.endsWithConsonantVConsonant(w) == true
  }

  property("endsWithConsonantVConsonant finds CVC") = forAll(wordsThatAreNotCVC) {
    (w: String) => Stemmer.endsWithConsonantVConsonant(w) == false
  }

  property("stringMeasure finds CVC counts") = forAll(cvcTwos) {
    (w: String) => Stemmer.stringMeasure(w) == 2
  }
}
