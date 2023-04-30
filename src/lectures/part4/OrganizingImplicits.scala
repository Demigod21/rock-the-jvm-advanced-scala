package lectures.part4


object OrganizingImplicits extends App {

  implicit val reverse: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 3, 4, 5, 2).sorted)

  /*
  *
  * Implicits
  *   - val/var
  *   - obj
  *   - accessor methods = def with no parentheses
   */


  // Exercise
  case class Person(name: String, age: Int)

  object Person {
    implicit val reversePerson: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 31),
    Person("Ciccio", 66)
  )
  println(persons.sorted)

  /*
  *
  * Implicit scope
  * - normal scope = local scope
  * - imported scope
  * - companion of all types involved in method signature
  *   - List
  *   - Ordering companion object
  *   - all the types invlved = A or any SuperType
  *
  * sorted[B >: A](implicit ord: Ordering[B]): List
  *
   */

  /*
  *
  * Rule #1
  * if there is a single possible value for it
  * and you can edit the code for the type
  * ===> define the implicit in the companion
  *
  * Rule #2
  * if there are many possible values for it
  * but a GOOD ONE
  * and you can edit
  * ===> define the good one in the companion (other in local scope or other objects)
   */


  // Exercise
  /*
    * - totalPrice order = most Used (50%)
    * - unitCount = 25%
    * - unit price = 25%
    *
   */
  case class Purchase(nUnits: Int, unitPrice: Int)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice * a.nUnits < b.unitPrice * b.nUnits)
  }

  object UnitPriceOrdering{
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }
  
  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }



}
