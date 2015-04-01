import com.mjamesruggiero.Distance

import org.scalacheck._

object DistanceSpec extends Properties("Distance") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Prop.AnyOperators
  import org.scalacheck.Prop.BooleanOperators
  import org.scalacheck.Gen.choose

  val doubleArrGen = for {
    m <- choose(-100.0, 100.0)
    n <- choose(-100.0, 100.0)
    o <- choose(-100.0, 100.0)
    p <- choose(-100.0, 100.0)
    q <- choose(-100.0, 100.0)
    r <- choose(-100.0, 100.0)
  } yield (Array(m, m, o), Array(p, q, r))

  property("manhattan") = forAll(doubleArrGen) { a =>
    a match {
      case (l, r) => Distance.manhattan(l, r) =? (l, r).zipped.foldLeft(0.0)((s, t) => s + Math.abs(t._1 - t._2))
    }
  }
}

