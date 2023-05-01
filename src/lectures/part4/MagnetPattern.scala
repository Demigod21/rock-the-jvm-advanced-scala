package lectures.part4

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  //solve problem of METHOD OVERLOADING
  class P2PRequest

  class P2PResponse

  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int

    def receive(request: P2PRequest): Int

    def receive(request: P2PResponse): Int

    def receive[T: Serializer](message: T): Int

    def receive[T: Serializer](message: T, statusCode: Int): Int

    def receive(future: Future[P2PRequest]): Int
  }

  /**
   * Problems
   * 1 - type erasure
   * 2 - lifting doesn't work for all overloads
   * val receiveFV = receive _ // ?!
   * 3 - code duplication
   * 4 - type inferrence and deafult args
   *
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling p2p request
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling p2p response
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  /**
   * 1 - no more type erasure problems
   */
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling future p2p response
      println("Handling FUTURE P2P response")
      2
    }
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling future p2p request
      println("Handling FUTURE P2P request")
      3
    }
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  /**
   * 2 - lifting WORKS
   */
  trait MathLib {
    def add1(x: Int) = x + 1

    def add1(s: String) = s.toInt + 1
  }

  //"magnetize"
  // we don't have type paramether and we cannot lift it in val addFV = add1 _
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("3"))

  /**
   * DRAWBACKS
   * 1 - verbose
   * 2 - harder to read
   * 3 - you can't name or place default args
   * 4 - call by name doesn't work correctly
   */

  class Handler {
    def handle(s: => String) = {
      println(s)
      println(s)
    }
  }

  //magnetizing
  trait HandleMagnet {
    def apply(): Unit
  }

  //handle method
  def handle(magnet: HandleMagnet): Unit = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello scala")
    "hahaha"
  }

  handle(sideEffectMethod())
  println("***DIFFERENCE***")
  handle {
    println("Hello scala")
    "MAGNET" // new StringHandle("MAGNET") you can miss some logs
  }

}
