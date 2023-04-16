package lectures.part1

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  //Unit=void
  val theUnit = println("Hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec def factorial(n: Int, acc: Int) : Int =
    if (n <= 0) acc
    else factorial(n-1, acc * n)

  //object-orientation
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog //subtyping polymorphism

  trait Carnivor{
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivor {
    override def eat(a: Animal): Unit = println("cruhncy")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog //natural language

  1.+(2)
  1+2

  //anon
  val aCarnivore = new Carnivor{
    override def eat(a: Animal): Unit = println("roar")
  }

  //generics
  abstract class MyList[+A]
  //singletons and companions
  object MyList

  //case classes
  case class Person(name: String, age: Int)

  //exceptions and try/catch/finally
  val throwsException = throw new RuntimeException //type: Nothing
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "Caught"
  } finally {
    println("logs")
  }

  // functional programming
  val incrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 +1
  }

  incrementer(1)

  val anonIncremeenter = (x: Int) => x + 1
  List(1,2,3).map(anonIncremeenter)

  //for comp
  val pairs = for {
    num <- List(1, 2, 3)
    char <- List ('a', 'b', 'c')
  } yield num + "-" + char



}
