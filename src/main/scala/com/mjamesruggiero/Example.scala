package com.mjamesruggiero

import scalaz.Monoid
import scalaz.syntax.traverse._
import scalaz.std.list._

object Example {

  import StateMonad._

  implicit val CacheMonoid = new Monoid[Cache] {
    override def zero = Cache(Map.empty, 0, 0)
    override def append(a: Cache, b: => Cache) =
      Cache(a.vals  ++ b.vals, a.hits + b.hits, a.misses + b.misses)
  }

  val s = FakeConfigService
  val listOfState: List[StateCache[ConfigValue]] = List(s.value("u1"), s.value("u2"), s.value("u1"))
  val stateOfList: StateCache[List[ConfigValue]] = listOfState.sequence[StateCache, ConfigValue]

}
