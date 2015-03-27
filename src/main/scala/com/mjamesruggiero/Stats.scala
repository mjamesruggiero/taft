package com.mjamesruggiero

class Stats(values: Vector[Double]) {
  require( values.forall(x => x >= Double.MinValue) )
  require( values.forall(x => x < Double.MaxValue) )
  require( ! values.isEmpty, "Cannot initialize with undefined values.")

  final val ZERO_EPS = 1e-12

	val mean = values.sum / values.size

  def sumOfSquares = values.foldLeft(0.0) {(a,x) => a + x*x}

	val variance = (sumOfSquares - mean * mean * values.size) / (values.size - 1)

	val stdDev = if(variance < ZERO_EPS) ZERO_EPS else Math.sqrt(variance)
}

object Stats {
  def apply(v: Vector[Double]) = new Stats(v)
}
