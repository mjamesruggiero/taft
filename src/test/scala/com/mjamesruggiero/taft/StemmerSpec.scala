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

  val wordsWithYVowel = Gen.oneOf("cyst", "nymph", "lynch", "myth", "hymns")

  val wordsEndingInY = Gen.oneOf("happy", "lucky", "sorry")

  val basicWordsAndStems = Gen.oneOf(
      ("bled", "bled"),
      ("feed", "feed"),
      ("plastered", "plaster"),
      ("agreed", "agree"),
      ("tabled", "table"),
      ("fabled", "fable"),
      ("troubled", "trouble"),
      ("conflated", "conflate"),
      ("troubled", "trouble"),
      ("motoring", "motor"),
      ("sing", "sing"),
      ("waiting", "wait")
    )

  val harderWordsAndStems = Gen.oneOf(
      ("tanned", "tan"),
      ("hopping", "hop"),
      ("falling", "fall"),
      ("kissing", "kiss")
    )

  val sPlurals = Gen.oneOf(
      ("caresses", "caress"),
      ("cats", "cat"),
      ("elms", "elm")//,
    )

  property("vowels") = forAll(vowels) { (c: Char) =>
    Stemmer.isVowel(c) == true
  }

  property("containsVowel sees vowels") = forAll(wordsWithVowels) { (w: String) =>
    Stemmer.containsVowel(w) == true
  }

  property("containsVowel doesn't see consonants") = forAll(wordsWithoutVowels) {
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

  property("isVowel (overloaded) looks at y") = forAll(wordsWithYVowel) {
    (w: String) => Stemmer.isVowel(w, 1) == true
  }

  property("step_1_c changes words ending in y") = forAll(wordsEndingInY) {
    (w: String) => Stemmer.step_1_c(w).endsWith("i")
  }

  property("step_1_c changes words ending in y") = forAll(wordsEndingInY) {
    (w: String) => Stemmer.step_1_c(w).endsWith("i")
  }

  property("step_1_b stems basic words") = forAll(basicWordsAndStems) { tup =>
    val (w, stem) = tup
    Stemmer.step_1_b(w) == stem
  }

  property("step_1_a limits s-ending plurals") = forAll(sPlurals) { tup =>
    val (w, stem) = tup
    Stemmer.step_1_a(w) == stem
  }
}
