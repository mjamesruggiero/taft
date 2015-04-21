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

  def endsWithConsonantVConsonant(str: String): Boolean = {
    if (str.length >= 3) {
        val (last, nextToLast, thirdFromLast)  = (
          str(str.length - 1),
          str(str.length - 2),
          str(str.length - 3)
        )

        if ((last == 'w') || (last == 'x') || (last == 'y'))
          false
        else if (!isVowel(last) && isVowel(nextToLast) && !isVowel(thirdFromLast) )
          true
        else
          false
    }
    else false
  }
}
