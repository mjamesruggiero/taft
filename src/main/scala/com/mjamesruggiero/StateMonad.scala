package com.mjamesruggiero

import scalaz.State

object StateMonad {

  type StateCache[+A] = State[Cache, A]

  trait ConfigService {
    def value(k: String): StateCache[ConfigValue]
  }

  case class ConfigValue(
    key: String,
    value: String)

  case class Timestamped[A](value: A, timestamp: Long)

  case class Cache(
    vals: Map[String, Timestamped[ConfigValue]],
    hits: Int,
    misses: Int)
    {
      def get(key: String): Option[Timestamped[ConfigValue]] = {
        val desired = vals.get(key)
        desired
      }

      def update(k: String, s: Timestamped[ConfigValue]): Cache = {
        Cache(vals + (k -> s), hits, misses)
      }
  }

  object FakeConfigService extends ConfigService {

    def value(k: String) = for {
      oConfigV <- checkCache(k)
      configV <- oConfigV match {
         case Some(configV) => State.state[Cache, ConfigValue](configV)
         case None => retrieve(k)
        }
      } yield configV

    private def checkCache(k: String): StateCache[Option[ConfigValue]] = State { c =>
      c.get(k) match {
        case Some(Timestamped(configV, ts))
          if (!stale(ts)) => {
            (c.copy(hits = c.hits + 1), Some(configV))
          }
        case other => {
          (c.copy(misses = c.misses + 1), None)
        }
      }
    }

    private def stale(ts: Long): Boolean = {
      val fiveMinutes = 5 * 60 * 1000L
      System.currentTimeMillis - ts > fiveMinutes
    }

    private def retrieve(k: String): StateCache[ConfigValue] = for {
      configV <- State.state(callWebService(k))
      tConfigV = Timestamped(configV, System.currentTimeMillis)
    } yield configV

    private def callWebService(k: String): ConfigValue = {
      ConfigValue(k, "fakeValue")
    }
  }
}
