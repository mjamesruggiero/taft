package com.mjamesruggiero.taft

object Stemmer {

  def isVowel(c: Char): Boolean = "aeiou".toList.foldLeft(false)(_ && _==c)

}
