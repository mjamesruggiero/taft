package com.mjamesruggiero

object LinearAlgebra {
  implicit class Matrix2D(a: Vector[Vector[Double]]) {
    def X(b: Matrix2D) = {
      val a = this
      require(a.cols == b.rows)
      Vector.tabulate(a.rows, b.cols) {
        (i, j) => ((0 to cols) map {k => a(i, k) * b(k, j)}).sum
      }
    }

    def apply(i: Int, j: Int) = a(i)(j)

    def rows = a.size

    def cols = a(0).size
  }
}
