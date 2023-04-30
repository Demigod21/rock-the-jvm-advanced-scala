package lectures.part4

object ImplicitsIntro extends App {

  val pair = "Davide" -> "666"

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(str: String) : Person = Person(str)

  println("Peter".greet)  // println(fromStringToPerson("Peter").greet)

  def increment(x: Int)(implicit amount: Int)= x + amount
  implicit val defaultAmount: Int = 10

  println(increment(2))
  println(increment(2)(20))
  // NOT default args -> the implicit is found by compiler from "search scope"

}
