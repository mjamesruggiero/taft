package com.mjamesruggiero

class TSeries[T](val label: String, arr: Array[T]) {

	final def toArray: Array[T] = arr

	final val size: Int = arr.size

  def == (that: TSeries[T]): Boolean = {
    size == that.size && arr.equals(that.toArray)
  }
}

object TSeries {
	def apply[T](label: String, arr: Array[T]): TSeries[T] = new TSeries[T](label, arr)
}
