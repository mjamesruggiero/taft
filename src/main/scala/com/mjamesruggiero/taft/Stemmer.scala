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

  def stringMeasure(str: String): Int = {
    var count = 0
    var vowelSeen: Boolean = false

    for (i <- 0 to str.length - 1) {
      if (isVowel(str(i))) {
        vowelSeen = true
      } else if (vowelSeen) {
        count += 1
        vowelSeen = false
      }
    }
    count
  }

  /**
   * Special check for occasion when y is a vowel
   */
  def isVowel(str: String, i: Int): Boolean  = {
    for (ch <- "aeiou" toList) {
      if (str(i) == ch || ((str(i) == 'y') && // you are 'y'
                           (i > 0) &&      // not the first letter
                           (i + 1 < str.length) && // not the last, either
                           (!isVowel(str(i - 1))) && // preceded by consonant
                           (!isVowel(str(i + 1))))) { // succeeded by consonant
        return true
      }
    }
    false
  }
}
