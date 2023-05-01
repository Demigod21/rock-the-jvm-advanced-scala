package lectures.part4

import scala.annotation.tailrec


object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val value: Int){
    def isEven: Boolean = value % 2 == 0
    def squareRoot: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      @tailrec
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
      timesAux(value)
    }
  }

  implicit class RicherInt(richInt: RichInt){
    def isOdd: Boolean = richInt.value % 2 != 0
  }


  new RichInt(36).squareRoot

  42.isEven
  // type enrichment = pimping

  1 to 10 // from implicit scala.RichInt

  import scala.concurrent.duration._
  2.seconds

  // compiler doesn't do multiple implicit searches
  // 42.isOdd

  /**
   * Enrich string class
   * - asInt
   * - encrypt (caesar cypher)
   *
   * Enrich int class
   * - times (function)
   * 3.times(() => ...)
   * 3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(string: String){
    def asInt: Int = Integer.valueOf(string)
    def encrypt(k: Int): String = string.map(c => (c + k).asInstanceOf[Char])
  }
}
