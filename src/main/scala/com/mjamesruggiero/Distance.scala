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
}
