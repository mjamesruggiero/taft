package com.mjamesruggiero

import scalaz.stream._
import scalaz.concurrent.Task

object Taft {
  def add100(n: Double) : Double = n + 100.00

  val runner: Task[Unit] = {
    val dir = "/Users/michaelruggiero/"

    io.linesR(dir + "/taft_tap.txt")
      .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
      .map(line => Taft.add100(line.toDouble).toString)
      .intersperse("\n")
      .pipe(text.utf8Encode)
      .to(io.fileChunkW(dir + "/taft_sink.txt"))
      .run
  }
}

