package lectures.part1

import lectures.part1.AdvPatternMatching.Persona

object AdvPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println("1 element")
    case _ =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic
   */

  class Persona(val name: String, val age: Int)

  object Persona{
    def unapply(person: Persona) : Option[(String, Int)] = Some((person.name, person.age))
  }

  val bob = new Persona("Bob", 22)
  val greeting = bob match {
    case Persona(n, a)  => s"Hi my name is $n"
  }

  /*
    Exercise
     custom pattern matching solution for this

   */
  val n: Int = 45
  val matchProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }

  // singleton objects for these conditions

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Option[Boolean] =
      if (arg < 10) Some(true) else None
  }

  val matchProperty2 = n match {
    case singleDigit(_) => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }

  //infix patterns
  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case Or(number, string) => s"$number is written as $string"
  }
  val humanDescription2 = either match {
    case number Or string => s"$number is written as $string"
  }

  // decomposing dequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }
  println(vararg)

  abstract class MyList[+A]{
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]) : Option[Seq[A]] =
      if(list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1 and 2"
    case _ => "something else"
  }
  println(decomposed)
  /*
    compiler looks for unapply method inside object MyList
    finds a function that decompose a MyList into a Seq and can use the _* on the Seq
   */

  // custom return types for unapply
  // isEmpty : Boolean, get: something
  abstract class Wrapper[T]{
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Persona): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false
      override def get: String = person.name
    }
  }

  val matchingBob = bob match {
    case PersonWrapper(n) => s"This person name is $n"
    case _ => "An alien"
  }
  println(matchingBob)
  // custom unapply doesn't have to return Option, but something that has "isEmpty" and "get"


}
