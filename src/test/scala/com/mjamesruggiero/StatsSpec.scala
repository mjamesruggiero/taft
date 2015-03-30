import com.mjamesruggiero.Stats

import org.scalacheck._

object StatsSpec extends Properties("Stats") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Prop.AnyOperators
  import org.scalacheck.Prop.BooleanOperators
  import org.scalacheck.Gen.choose

  val floatGen = for {
    n <- choose(-100.0, 100.0)
    m <- choose(-100.0, 100.0)
    o <- choose(-100.0, 100.0)
  } yield (m, m, o)

  property("mean") = forAll {
    (one: Double, two: Double, three: Double) =>
    (Stats(Vector(one, two, three)).mean == ((one + two + three) / 3))
  }

  property("variance") = forAll(floatGen) { case (one, two, three) =>
    val s = Stats(Vector(one, two, three))
    val sumOfSq = List(one, two, three).foldLeft(0.0) {(a,x) => a + x*x}
    val mean = s.mean
    val size = 3

    val expected = (sumOfSq - mean * mean * size) / (size - 1)

    (s.variance ?= expected)
  }


  property("stdDev") = forAll(floatGen) { case (one, two, three) =>
    val s = Stats(Vector(one, two, three))
    (s.stdDev ?= Math.sqrt(s.variance))
  }
}
