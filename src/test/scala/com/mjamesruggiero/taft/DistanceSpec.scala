import com.mjamesruggiero.taft.Distance

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

  property("manhattan") = forAll(doubleArrGen) { arrayTup =>
    arrayTup match {
      case (arr1, arr2) => Distance.manhattan(arr1, arr2) =? (arr1, arr2).zipped.foldLeft(0.0)((s, t) => s + Math.abs(t._1 - t._2))
    }
  }

  property("euclidean") = forAll(doubleArrGen) { arrayTup =>
    arrayTup match {
      case (arr1, arr2) => Distance.euclidean(arr1, arr2) =? Math.sqrt( (arr1, arr2).zipped.foldLeft(0.0)((s, t) => { val d = t._1 - t._2; s + d * d }))
    }
  }

  property("cosine") = forAll(doubleArrGen) { arrayTup =>
      arrayTup match {
        case (arr1, arr2) => {
          val ret = Distance.cosine(arr1, arr2)
          val norms = (arr1, arr2).zipped.foldLeft((0.0, 0.0, 0.0)) { (s, t) =>
              (s._1 + t._1*t._2, s._2 + t._1*t._1, s._3 + t._2*t._2)
          }
          val expected = norms._1/Math.sqrt(norms._2*norms._3)
          ret =? expected
      }
    }
  }
}

