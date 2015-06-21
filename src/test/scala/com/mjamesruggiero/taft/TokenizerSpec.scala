package com.mjamesruggiero.taft

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
    "MY YOUTH IS BENT BY THE SAME WINTRY FEVER.",
    "FOR ONE THROB OF THE ARTERY",
    "WHILE ON THAT OLD GREY STONE I SAT",
    "UNDER THE OLD WIND-BROKEN TREE",
    "I KNEW THAT ONE IS ANIMATE",
    "MANKIND INANIMATE PHANTASY."
  )

  val singleCharacterWords = Gen.oneOf(
    ('a' to 'z')
  )

  val poems: Gen[String] = for {
    one <- genLine
    two <- genLine
    three <- genLine
  } yield List(one, two, three).mkString("\n")

  val singleChars: Gen[String] = for {
    one <- singleCharacterWords
    two <- singleCharacterWords
    three <- singleCharacterWords
  } yield List(one, two, three).mkString(" ")

  property("stopwords") = forAll(poems) { (fakePoem: String) =>
    val t = Tokenizer(fakePoem)
    val stops = Tokenizer.stopWords.toSet
    stops.intersect(t.eachWord.toSet).size == 0
  }

  property("lowercase") = forAll(poems) { (fakePoem: String) =>
    val chars = Tokenizer(fakePoem).eachWord.mkString("").toSet
    chars.intersect(('A' to 'Z').toSet).size == 0
  }

  property("filters out single chars") = forAll(singleChars) { (test: String) =>
    val chars = Tokenizer(test).eachWord.mkString("").toSet
    chars.toSet.size == 0
  }
}
