package com.mjamesruggiero.taft

import com.redis.{ RedisClientPool => Pool }
import scalaz.{ concurrent, Kleisli }, concurrent.Task

object Storage {
  def get(key: String): Kleisli[Task, Pool, Option[String]] =
    Kleisli { (pool: Pool) => Task {
      pool.withClient { _.get(key) }
    }}

  def set(key: String, value: String): Kleisli[Task, Pool, Boolean] =
    Kleisli { (pool: Pool) => Task {
      pool.withClient { _.set(key, value) }
    }}

  def delete(key: String): Kleisli[Task, Pool, Option[Long]] =
    Kleisli { (pool: Pool) => Task {
      pool.withClient { _.del(key) }
    }}
}
