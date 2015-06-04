package com.mjamesruggiero.taft

import com.redis.RedisClientPool
import scalaz.{ concurrent, Kleisli }, concurrent.Task

object Storage {
  def get(key: String): Kleisli[Task, RedisClientPool, Option[String]] =
    Kleisli { (pool: RedisClientPool) => Task {
      pool.withClient { _.get(key) }
    }}

  def set(key: String, value: String, pool: RedisClientPool): Task[Boolean] =
    Task {
      try {
        pool.withClient { _.set(key, value) }
        true
      }
      catch {
        case e : Exception => false
      }
    }

  def delete(key: String): Kleisli[Task, RedisClientPool, Option[Long]] =
    Kleisli { (pool: RedisClientPool) => Task {
      pool.withClient { _.del(key) }
    }}
}
