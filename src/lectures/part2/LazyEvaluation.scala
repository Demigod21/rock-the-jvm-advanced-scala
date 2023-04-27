package lectures.part2

object LazyEvaluation extends App {

  //delays evaluation
  lazy val x: Int = {
    println("hello")
    42
  } //evaluated only when used for the first time and only once

  println(x)
  println(x) //we only see one hello

  //example of implications:
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if(simpleCondition && lazyCondition) "yes" else "no") //we will not see "Boo"
  //lazyCondition never evaluated

  // in concjuction with call by name
  def byNameMethod(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n // ONLY EVALUATED ONCE
    t + t + t + 1
  }
  def retrieveMagicGalue = {
    //side effect long computation
    println("Waiting /n")
    Thread.sleep(1500)
    42
  }

  //print(byNameMethod(retrieveMagicGalue))

  // use lazy vals

  // filtering with lazy val
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThen20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 22, 40, 5, 24)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThen20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30)
  val gt20Lazy = lt30Lazy.withFilter(greaterThen20)
  println
  println(gt20Lazy)
  gt20Lazy.foreach(println)


  // for-compr use withFilter with guards
  for {
    a <- List(1,2,3) if a % 2 == 0
  } yield a + 1

  List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1)
  /*
  * Exercise : lazilu evaluated singly linked STREAM of elements
  * Head always evaluated
  * Tail lazy
  *
  *
   */
  abstract class MyStream[+A] {
    def isEmpty : Boolean
    def head : A
    def tail : MyStream[A]

    def #::[B >: A](element: B): MyStream[B] //prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B) : MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] //takes the first n elements out of this stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream{
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }




}
