package com.mjamesruggiero

object Distance {

  def manhattan(x: Array[Double], y: Array[Double]) : Double = {
    (x, y).zipped.foldLeft(0.0)((s, t) => s + Math.abs(t._1 - t._2))
  }

  def euclidean(x: Array[Double], y: Array[Double]): Double = {
    val dist = (x, y).zipped.foldLeft(0.0){ (s, t) => {
        val innerDistance = t._1 - t._2
        val squared = innerDistance * innerDistance
        s + squared
      }
    }
    Math.sqrt(dist)
  }

  def cosine(x: Array[Double], y: Array[Double]): Double  = {
    val zeros = (0.0, 0.0, 0.0)

    def sq(x: Double): Double = x * x

    val norms = (x, y).zipped.foldLeft(zeros) { (s, t) =>
      (s._1 + t._1 * t._2, s._2 + sq(t._1), s._3 + sq(t._2))
    }

    norms._1 / Math.sqrt(norms._2 * norms._3)
  }
}
