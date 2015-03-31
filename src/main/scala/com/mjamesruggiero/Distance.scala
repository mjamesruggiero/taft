package com.mjamesruggiero

object Distance {

  def manhattan(x: Array[Double], y: Array[Double]) : Double = {
    (x, y).zipped.foldLeft(0.0)((s, t) => s + Math.abs(t._1 - t._2))
  }
}
