import com.mjamesruggiero.Stats

import org.scalacheck._

object StatsSpec extends Properties("Stats") {
  import org.scalacheck.Prop.forAll

  property("mean") = forAll {
    (one: Double, two: Double, three: Double) =>
    (Stats(Vector(one, two, three)).mean == ((one + two + three) / 3))
  }

  property("sumOfSquares") = forAll {
    (one: Double, two: Double, three: Double) =>
    (Stats(Vector(one, two, three)).sumOfSquares == ((one * one + two * two + three * three)))
  }

  /**
  property("variance") = forAll {
    (one: Double, two: Double, three: Double) =>
    val sumOfSq = (one * one + two * two + three * three)
    val mean = ((one + two + three) / 3)
    val size = 3

    val expected = (sumOfSq - mean * mean * size) / (size - 1)

    (Stats(Vector(one, two, three)).variance == expected)
  }
  **/
}
