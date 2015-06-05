package com.mjamesruggiero.taft

import com.redis.RedisClientPool
import scalaz.{ concurrent, Kleisli }, concurrent.Task
import scala.util.{ Success, Failure }

object Storage {
  def get(key: String, pool: RedisClientPool): Task[Option[String]] =
    Task { pool.withClient { _.get(key) } }

  def set(key: String, value: String, pool: RedisClientPool): Task[Unit] =
    Task { pool.withClient { _.set(key, value) } }

  def delete(key: String, pool: RedisClientPool): Task[Unit] =
    Task { pool.withClient { _.del(key) } }
}
