package com.mjamesruggiero.taft

import com.redis.RedisClient.DESC
import com.redis.RedisClientPool
import scalaz.{ concurrent, Kleisli }, concurrent.Task
import scala.util.{ Success, Failure }
import scala.util.Random

object Database {
  def get(key: String, pool: RedisClientPool): Task[Option[String]] =
    Task { pool.withClient { _.get(key) } }

  def set(key: String, value: String, pool: RedisClientPool): Task[Unit] =
    Task { pool.withClient { _.set(key, value) } }

  def delete(key: String, pool: RedisClientPool): Task[Unit] =
    Task { pool.withClient { _.del(key) } }

  def sadd(set: String, value: String, pool: RedisClientPool): Task[Unit] = {
    Task { pool.withClient { _.sadd(set, value) } }
  }

  def zadd(key: String, score: Double, member: String, pool: RedisClientPool): Task[Unit] = {
    Task { pool.withClient { _.zadd(key, score, member) } }
  }

  def zincrby(key: String, incr: Double, member: String, pool: RedisClientPool): Task[Unit] = {
    Task { pool.withClient { _.zincrby(key, incr, member) } }
  }

  def sample(key: String, start: Int, end: Int, size: Int, pool: RedisClientPool): Task[Option[List[String]]] = {
    Task {
      val values = pool.withClient {
        _.zrangebyscore(key, start, true, end, true, None)
      }
      values match {
          case Some(values) => Some(Random.shuffle(values.toSet).take(size).toList)
          case _ => None
      }
    }
  }
}
