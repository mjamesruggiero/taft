package com.mjamesruggiero

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.{ BasicAWSCredentials, ClasspathPropertiesFileCredentialsProvider }
import com.snowplowanalytics.util.Tap._
import com.typesafe.config.Config

import io.github.cloudify.scala.aws.kinesis.Client
import io.github.cloudify.scala.aws.kinesis.Client.ImplicitExecution._
import io.github.cloudify.scala.aws.kinesis.Definitions.{Stream, PutResult}
import io.github.cloudify.scala.aws.kinesis.KinesisDsl._

import java.nio.ByteBuffer
import org.apache.thrift.TSerializer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, Await, TimeoutException}

case class StreamProducer(config: Config) {
  private object ProducerConfig {
    private val producer = config.getConfig("producer")
    val logging = producer.getBoolean("logging")

    private val aws = producer.getConfig("aws")
    val awsAccessKey = aws.getString("access-key")
    val awsSecretKey = aws.getString("secret-key")

    private val stream = producer.getConfig("stream")
    val streamName = stream.getString("name")
    val streamSize = stream.getInt("size")
    val streamDataType = stream.getString("data-type")

    private val events = producer.getConfig("events")
    val eventsOrdered = events.getBoolean("ordered")
    val eventsLimit = {
      val l = events.getInt("limit")
      if (l == 0) None else Some(l)
    }

    private val ap = producer.getConfig("active-polling")
    val apDuration = ap.getInt("duration")
    val apInterval= ap.getInt("interval")
  }

  private implicit val kinesis = createKinesisClient(
    ProducerConfig.awsAccessKey,
    ProducerConfig.awsSecretKey)

  private var stream: Option[Stream] = None
  private val thriftSerializer = new TSerializer()

  /**
   * Creates new stream if one doesn't exist.
   *
   * @param name The name of the stream to create
   * @param size The number of shards to support for this stream
   * @param duration How long to keep checking if stream became active, in seconds
   * @param interval How frequently to check if stream has become active, in seconds
   *
   * @return Boolean, where:
   * 1. true means the stream was successfully created or already exists
   * 2. false means an error occurred
   */
  def createStream(
    name: String = ProducerConfig.streamName,
    size: Int = ProducerConfig.streamSize,
    duration: Int = ProducerConfig.apDuration,
    interval: Int = ProducerConfig.apInterval) : Boolean = {

      if (ProducerConfig.logging) {
          println(s"Checking streams for $name")
      }

      val streamListFuture = for {
       s <- Kinesis.streams.list
      } yield s

      val streamList: Iterable[String] = Await.result(streamListFuture, Duration(duration, SECONDS))

      for (stream <- streamList) {
        if (stream == name) {
            if (ProducerConfig.logging) {
                println(s"Stream $name already exists!")
            }
            return true
        }
      }

      if (ProducerConfig.logging) {
          println(s"Stream $name _does not_ exist; creating stream $name of size $size")
      }

      val createStream = for {
        s <- Kinesis.stream.create(name)
      } yield s

      try {
          stream = Some(Await.result(createStream, Duration(duration, SECONDS)))
          Await.result(stream.get.waitActive.retrying(duration),
            Duration(duration, SECONDS))
      }
      catch {
          case _ : TimeoutException =>
            if (ProducerConfig.logging) println("Error; timed out")
            false
      }
      if (ProducerConfig.logging) println("Successfully created stream")
      true
  }

  def produceStream(
    name: String = ProducerConfig.streamName,
    ordered: Boolean = ProducerConfig.eventsOrdered,
    limit: Option[Int] = ProducerConfig.eventsLimit) {

    if (stream.isEmpty) {
      stream = Some(Kinesis.stream(name))
    }

    var writeExampleRecord: (String, Long) => PutResult =
      if (ProducerConfig.streamDataType == "string") {
        writeExampleStringRecord
      } else if (ProducerConfig.streamDataType == "thrift") {
        writeExampleThriftRecord
      } else {
        throw new RuntimeException("data type configuration must be 'string' or 'thrift'")
      }

    def write() = writeExampleRecord(name, System.currentTimeMillis())
    (ordered, limit) match {
        case (false, None) => while(true) { write() } // forevah
        case (true, None) => throw new RuntimeException("Ordered stream support not yet implemented") //TODO
        case (false, Some(c)) => (1 to c).foreach(_ => write())
        case (true, Some(c)) => throw new RuntimeException("Ordered stream support not yet implemented") //TODO
    }
  }

  def createKinesisClient(accessKey: String, secretKey: String): Client =
    if (isCpf(accessKey) && isCpf(secretKey)) {
        Client.fromCredentials(new ClasspathPropertiesFileCredentialsProvider())
    } else if (isCpf(accessKey) || isCpf(secretKey)) {
        throw new RuntimeException("access key and secret key must both be set to 'cpf' or neither")
    } else {
      Client.fromCredentials(accessKey, secretKey)
    }

  def writeExampleStringRecord(stream: String, timestamp: Long): PutResult = {
    if (ProducerConfig.logging) println("Writing string record")

    val stringData = s"example-record-$timestamp"
    val partitionKey = s"partition-key-${timestamp % 1000000}"
    if (ProducerConfig.logging) {
      println(s"  -> data: $stringData")
      println(s"  -> key: $partitionKey")
    }

    val result = writeRecord(ByteBuffer.wrap(stringData.getBytes), partitionKey)
    result
  }

  def writeExampleThriftRecord(stream: String, timestamp: Long) :  = {
    if (ProducerConfig.logging) println("Writing thrift record")

  }

  def isCpf(key: String) : Boolean = (key == "cpf")
}
