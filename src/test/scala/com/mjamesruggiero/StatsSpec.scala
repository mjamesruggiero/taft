import com.mjamesruggiero.Stats

import org.scalacheck._

object StatsSpec extends Properties("Stats") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Prop.AnyOperators
  import org.scalacheck.Prop.BooleanOperators
  import org.scalacheck.Gen.choose

  val doubleTupGen = for {
    n <- choose(-100.0, 100.0)
    m <- choose(-100.0, 100.0)
    o <- choose(-100.0, 100.0)
  } yield (m, m, o)

  property("mean") = forAll {
    (one: Double, two: Double, three: Double) =>
    (Stats(Vector(one, two, three)).mean == ((one + two + three) / 3))
  }

  property("variance") = forAll(doubleTupGen) { case (one, two, three) =>
    val s = Stats(Vector(one, two, three))
    val sumOfSq = List(one, two, three).foldLeft(0.0) {(a,x) => a + x*x}
    val mean = s.mean
    val size = 3

    val expected = (sumOfSq - mean * mean * size) / (size - 1)

    (s.variance ?= expected)
  }

  property("stdDev") = forAll(doubleTupGen) { case (one, two, three) =>
    val s = Stats(Vector(one, two, three))
    (s.stdDev ?= Math.sqrt(s.variance))
  }

  property("gauss") = forAll(doubleTupGen) { case (one, two, three) =>
    val inv_square_root_2pi = 1.0 / Math.sqrt(2.0 * Math.PI)
    val stats = Stats(Vector(one, two, three))
    val vec = Vector(one, two, three)
    val g = Stats.gauss(stats.mean, stats.stdDev, vec)
    val variance = stats.variance
    val mean = stats.mean
    val expected = vec.map ( v => {
      val y = v - mean
      inv_square_root_2pi * Math.exp(-0.5 * y * y / variance ) / stats.stdDev
    })
    val tolerancesAreAcceptable = (g, expected).zipped.map { (expected: Double, actual: Double) =>
      deltaWithinTolerance(expected, actual, 0.0000000000001)
    }.foldLeft(true)(_ && _)
    (tolerancesAreAcceptable == true)
  }

  def deltaWithinTolerance(expected: Double, actual: Double, tolerance: Double): Boolean = {
    Math.abs(expected - actual) < tolerance
  }
}
