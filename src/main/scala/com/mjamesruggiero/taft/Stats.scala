package com.mjamesruggiero.taft

class Stats(values: Vector[Double]) {
  final val ZERO_EPS = 1e-12

  /**
   * inner class validates entries and employs lazy methods
   * */
  private class Statz(
    var minValue: Double,
    var maxValue: Double,
    var sum: Double,
    var sumSqr: Double
  )

  private[this] val _stats = {
    val _stats = new Statz(Double.MaxValue, Double.MinValue, 0.0, 0.0)

    values.foreach(x => {
      if (x < _stats.minValue) _stats.minValue = x
      if (x > _stats.minValue) _stats.maxValue = x
      _stats.sum += x
      _stats.sumSqr += x * x
    })
    _stats
  }

  @inline
	lazy val mean = _stats.sum / values.size

	lazy val variance = (_stats.sumSqr - mean*mean*values.size) / (values.size - 1)

	lazy val stdDev = if(variance < ZERO_EPS) ZERO_EPS else Math.sqrt(variance)

	lazy val min = _stats.minValue

	lazy val max = _stats.maxValue
}

object Stats {
  def apply(v: Vector[Double]) = new Stats(v)

  final val ZERO_EPS = 1e-12
  final val INV_SQUARE_ROOT_2PI = 1.0 / Math.sqrt(2.0 * Math.PI)

  def gauss(
    mean: Double,
    standardDeviation: Double,
    values: Vector[Double]
  ): Vector[Double] = values.map { x =>
      val y = x - mean
      val variance = standardDeviation * standardDeviation
      INV_SQUARE_ROOT_2PI * Math.exp(-0.5 * y * y / variance) / standardDeviation
    }

}
