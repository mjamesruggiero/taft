import com.mjamesruggiero.taft.Stemmer

import org.scalacheck._

object StemmerSpec extends Properties("Stemmer") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen

  val vowels = Gen.oneOf('a', 'e', 'i', 'o', 'u')

  property("vowels") = forAll(vowels) { (c: Char) =>
    Stemmer.isVowel(c) == false
  }
}
