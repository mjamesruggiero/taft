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

  def step_1_c(str: String): String = {
    val candidate = str.endsWith("y") && containsVowel(str.substring(0, str.length() -1))
    candidate match {
        case true => str.substring(0, str.length()) + "i"
        case _ => str
    }
  }

  def step_1_b(str: String): String  = {
    if (str.endsWith("eed")) {
      if (stringMeasure(str.substring(0, str.length - 3)) > 0) {
          return str.substring(0, str.length - 1)
      }
    } else if ((str.endsWith("ed")) &&
               (containsVowel(str.substring(0, str.length - 2)))) {
                 return step_1_b_2(str.substring(0, str.length - 2))
    } else if ((str.endsWith("ing")) && (containsVowel(str.substring(0, str.length - 3)))) {
      return step_1_b_2(str.substring(0, str.length - 3))
    }
    str
  }

  def step_1_b_2(str: String): String = {
    if ((str.endsWith("at") ||
      str.endsWith("bl") ||
      str.endsWith("iz"))) {
        return str + "e"
    } else if ((str.length > 1) && (endsWithDoubleConsonant(str)) &&
        (!(str.endsWith("l")) || str.endsWith("s") || str.endsWith("z"))) {
        return str.substring(0, str.length - 1)
    } else if ((stringMeasure(str) == 1) && (endsWithConsonantVConsonant(str))) {
        return str + "e"
    }
    str
  }

  def endsWithDoubleConsonant(str: String): Boolean = {
    val c: Char = str.charAt(str.length - 1)
    if (c == str.charAt(str.length - 2)) {
        if (!containsVowel(str.substring(str.length - 2))) {
            return true
        }
    }
    false
  }

  def replacePatterns(
    str: String,
    patterns: List[(String, String)],
    comparer: Int => Boolean): String  = {
      for (pattern <-patterns) {
        if (str.endsWith(pattern._1)) {
            val res = replaceLast(str, pattern._1, pattern._2)
            if (comparer(stringMeasure(replaceLast(str, pattern._1, "")))) {
                return res
            } else {
              return str
            }
        }
      }
    str
  }

  def replacePatterns(str: String, patterns: List[(String, String)]): String =
    replacePatterns(str, patterns, _>0)

  def replaceLast(
    str: String,
    pattern: String,
    replacement: String) = new StringBuilder(str)
                                  .replace(str.lastIndexOf(pattern), str.lastIndexOf(pattern) + pattern.length, replacement)
                                  .toString


  def step_1_a(str: String): String = {
    replacePatterns(str, List(("sses", "ss"), ("ies", "i"), ("ss", "ss"), ("s", "")), _>=0)
  }

  def step_4(str: String): String = {
    val patterns: List[(String, String)] = List(
        ("al", ""), ("ance", ""), ("ence", ""), ("er", ""),
        ("ic", ""), ("able", ""), ("ible", ""), ("ant", ""),
        ("ement", ""), ("ment", ""), ("ent", ""), ("ou", ""),
        ("ism", ""), ("ate", ""), ("iti", ""), ("ous", ""),
        ("ive", ""), ("ize", "")
      )
    val res: String = replacePatterns(str, patterns, _>1)
    if (str == res) {
      val isLatinate = (str.endsWith("ision") || str.endsWith("tion")) && stringMeasure(str.substring(0, str.length - 3)) > 1
      if (isLatinate) return str.substring(0, str.length - 3)
      else return str
    }
    res
  }

  def step_3(str: String): String = {
    val patterns: List[(String, String)] = List(
      ("icate", "ic"),
      ("ative", ""),
      ("alize", "al"),
      ("iciti", "ic"),
      ("ical", "ic"),
      ("ful", ""),
      ("ness", "")//,
    )
    replacePatterns(str, patterns)
  }

  def step_2(str: String): String = {
    val patterns: List[(String, String)] = List(
     ("ational", "ate"),
     ("tional", "tion"),
     ("enci", "ence"),
     ("anci", "ance"),
     ("izer", "ize"),
     ("bli", "ble"),
     ("alli", "al"),
     ("entli", "ent"),
     ("eli", "e"),
     ("ousli", "ous"),
     ("ization", "ize"),
     ("ation", "ate"),
     ("ator", "ate"),
     ("alism", "al"),
     ("iveness", "ive"),
     ("fulness", "ful"),
     ("ousness", "ous"),
     ("fulness", "ful"),
     ("aliti", "al"),
     ("iviti", "ive"),
     ("biliti", "ble"),
     ("logi", "log")//,
    )
    replacePatterns(str, patterns)
  }
}
