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

}
