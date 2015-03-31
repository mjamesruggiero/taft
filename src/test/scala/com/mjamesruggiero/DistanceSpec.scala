import com.mjamesruggiero.Distance

import org.scalacheck._

object DistanceSpec extends Properties("Distance") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Prop.AnyOperators
  import org.scalacheck.Prop.BooleanOperators
  import org.scalacheck.Gen.choose

  val doubleArrGen = for {
    n <- choose(-100.0, 100.0)
    m <- choose(-100.0, 100.0)
    o <- choose(-100.0, 100.0)
  } yield Array(m, m, o)

  property("manhattan") = forAll(doubleArrGen) { a =>
    Distance.manhattan(a, a) ?= 0.0
  }
}

