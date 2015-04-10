import com.mjamesruggiero.taft.TSeries

import org.scalacheck._

object TSeriesSpec extends Properties("TSeries") {
  import org.scalacheck.Prop.forAll
  import org.scalacheck.Prop.AnyOperators
  import org.scalacheck.Prop.BooleanOperators
  import org.scalacheck.Gen.choose

  val doubleArrGen = for {
    m <- choose(-100.0, 100.0)
    n <- choose(-100.0, 100.0)
    o <- choose(-100.0, 100.0)
  } yield (Array(m, m, o))

  property("equals") = forAll(doubleArrGen) { arr =>
    val ts1 = TSeries("foo", arr)
    val ts2 = TSeries("bar", arr)
    ts1 == ts2
  }

  property("take") = forAll { (n1: Float, n2: Float) =>
    val ts1 = TSeries("foo", Array(n1, n2))
    ts1.take(1).toArray.length == 1
  }
}
