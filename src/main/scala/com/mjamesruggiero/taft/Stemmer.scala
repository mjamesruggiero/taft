package com.mjamesruggiero.taft

object Stemmer {

  def isVowel(c: Char): Boolean = {
    "aeiou".toList.contains(c)
  }

  def containsVowel(str: String): Boolean = {
    if (str.toList.exists(isVowel(_))) return true
    if(str.toList.contains('y')) return true
    false
  }
}
