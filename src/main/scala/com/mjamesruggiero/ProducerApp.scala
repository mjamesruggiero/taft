package com.mjamesruggiero

import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import org.clapper.argot._

object ProducerApp {
  import ArgotConverters._

  val parser = new ArgotParser(
    programName = "Taft",
    compactUsage = true,
    preUsage = Some("%s, %s".format("Taft", "0.1"))
  )

  val defaultMsg = "Configuration file. Defaults to \"resources/default.conf\" (within .jar) if not set"
  val config = parser.option[Config](List("config"),
                                     "filename",
                                     defaultMsg) {
    (c, opt) =>
      val file = new File(c)
      if (file.exists) {
          ConfigFactory.parseFile(file)
      } else {
        parser.usage("Configuration file \"%s\" not found".format(c))
        ConfigFactory.empty()
      }
  }

  def main(args: Array[String]) {
    parser.parse(args)
    val conf = config.value.getOrElse(ConfigFactory.load("default"))

    val sp = StreamProducer(conf)

    if (!sp.createStream()) {
        return
    }

    sp.produceStream()
  }
}
