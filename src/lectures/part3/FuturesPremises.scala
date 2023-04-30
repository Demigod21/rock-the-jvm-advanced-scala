package lectures.part3

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FuturesPremises extends App {

  def calculateMeaningLife: Int = {
    Thread.sleep(3000)
    42
  }

  val aFuture = Future{
    calculateMeaningLife
  }

  println(aFuture.value) // Option[Try[Int]]
  println("Waiting on the future")
  aFuture.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"Meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I have failed with $exception")
  })
  Thread.sleep(3100)

}
