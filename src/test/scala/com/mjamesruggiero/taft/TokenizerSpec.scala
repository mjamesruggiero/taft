import com.mjamesruggiero.taft.Tokenizer

import org.scalacheck._

object TokenizerSpec extends Properties("Tokenizer") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Gen

  val genLine = Gen.oneOf(
    "NEVER UNTIL THE MANKIND MAKING",
    "BIRD BEAST AND FLOWER",
    "FATHERING AND ALL HUMBLING DARKNESS",
    "TELLS WITH SILENCE THE LAST LIGHT BREAKING",
    "THE FORCE THAT THROUGH THE GREEN FUSE DRIVES THE FLOWER",
    "DRIVES MY GREEN AGE; THAT BLASTS THE ROOTS OF TREES",
    "IS MY DESTROYER.",
    "AND I AM DUMB TO TELL THE CROOKED ROSE",
    "MY YOUTH IS BENT BY THE SAME WINTRY FEVER."
  )

  val poems: Gen[String] = for {
    one <- genLine
    two <- genLine
    three <- genLine
  } yield List(one, two, three).mkString("\n")

  property("stopwords") = forAll(poems) { (fakePoem: String) =>
    val t = new Tokenizer(fakePoem)
    val stops = Tokenizer.stopWords.toSet
    stops.intersect(t.eachWord.toSet).size == 0
  }

  property("lowercase") = forAll(poems) { (fakePoem: String) =>
    val t = new Tokenizer(fakePoem)
    val chars = t.eachWord.mkString("").toSet
    chars.intersect(('A' to 'Z').toSet).size == 0
  }
}

