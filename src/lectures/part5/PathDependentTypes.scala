package lectures.part5

object PathDependentTypes extends App {

  class Outer{
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner): Unit = println(i)

    def printGeneral(i: Outer#Inner): Unit = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val o = new Outer
  // val inner = new Inner -- NOPE
  // val inner = new Outer.Inner -- NOPE
  val inner = new o.Inner // you need an instance of Outer

  val oo = new Outer
  // val otherInner: oo.Inner = new o.Inner -- NOPE

  o.print(inner)
  // oo.print(inner)

  // path dependant type

  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)

  /**
   * Exercvise
   * Db keyed by Int or string, but maybe others
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike{
    type Key = K
  }

  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("home") // ok
  // get[IntItem]("home") // NOT OK


}
