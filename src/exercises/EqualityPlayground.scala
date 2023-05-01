package exercises

import lectures.part4.TypeClasses.{User, john}

object EqualityPlayground extends App {

  /**
   * Equality
   */
  trait Equal[T] {
    def apply(value: T, value2: T): Boolean
  }

  implicit object UserEqualizerName extends Equal[User] {
    override def apply(value: User, value2: User): Boolean = value.name.equals(value2.name)
  }

  object UserEqualizerAge extends Equal[User] {
    override def apply(value: User, value2: User): Boolean = value.age == value2.age
  }

  /**
   * Exercise : implement type class pattern for the equality
   */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
  }

  val anotherJohn = User("John", 45, "nono@gmail.com")

  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism

  /**
   * Exercise - Improve the Equal Type Class with an implicit conversion class
   * ===(anotherValue: T) -> equal from value and anotherValue
   * !==(anotherValue: T) -> opposite
   */

  implicit class EqualEnrichment[T](value: T){
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, anotherValue)
  }

  /**
   * Type Safe
   */
  // println(john === 42) CAN'T DO
}
