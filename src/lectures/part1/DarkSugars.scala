package lectures.part1

import scala.util.Try

object DarkSugars extends App {

  // syntax suger #1 : method with single param
  def singleArgMet(arg: Int): String = s"$arg little ducks"

  val description = singleArgMet {
    //write some complex code
    42
  }

  val aTryInt = Try {
    throw new RuntimeException
  }

  List(1,2,3).map{ x=>
    x + 3
  }

  //syntax sugar #2: single abstract method
  trait Action {
    def act(x: Int): Int
  }

  def anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  def aFunkyInstance: Action = (x: Int) => x + 1 // magic

  //example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello Scala")
  })

  val aSweetherThread = new Thread(() => println("Sweet Scala"))

  abstract class AnAbstratType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstace: AnAbstratType = (a: Int) => println("sweet")

  val prependedList = 2 :: List(3, 4)
  //2.::(List(3,4))
  // List(3,4).::(2)
  //?!?!?!

  // scala spec : last char declares associatevy method of method

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  def myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  //syntax sugar #4 multi word method naming

  class TeenGirl(name: String){
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val Lilly = new TeenGirl("Lilly")
  Lilly `and then said` "scala is so sweet"

  //syntax sugar #5 infix types
  class Composite[A, B]
  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  //syntax sugar #6 update() is very special, much like apply
  val anArray = Array(1,2,3)
  anArray(2) = 7 //rewritten anArray.update(2, 7)
  //used in mutable collections

  //syntax sugar #7 setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0
    def member = internalMember //getter
    def member_=(value: Int): Unit = internalMember = value //setter
  }



}
