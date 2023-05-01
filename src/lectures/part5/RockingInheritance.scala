package lectures.part5

object RockingInheritance extends App {

  // convenience
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem
  trait Animal {
    def name: String
  }

  trait Lion extends Animal {
    override def name: String = "Lion"
  }

  trait Tiger extends Animal {
    override def name: String = "Tiger"
  }

  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name) // "tiger"

  /** WHY?!
   * Mutant extends Animal with { override def name: String = "Lion" }
   * with Animal with { override def name: String = "Tiger" }
   *
   * LAST OVERRIDE GET PICKED -> DIAMOND PROBLEM
   */

  // the super problem + type linearization
  trait Cold {
    def print: Unit = println("cold")
  }

  /*
  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print(): Unit = println("red")
  }

  class White extends Red with Green with Blue{
    override def toString: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  color.print // white blue green cold -> NOT RED, WHY */
  // COLD = ANYREF WITH COLD
  // GREEN = COLD WITH GREEN
  // = ANY REF WITH COLD WITH GREEN
  // BLUE = COLD WITH BLUE
  // = ANY REF WITH COLD WITH BLUE
  // RED = ANYREF WITH RED

  /**
   * WHITE = RED WITH GREED WITH BLUE WITH WHITE
   * ANY REF WITH RED
   * WITH ANYREF WITH COLD AND GREEN
   * WITH ANYREF WITH COLD AND BLUE
   * WITH WHITE
   *
   * JUMPS OVER DUPLICATES (JUMP OVER ANYREF AND COULD TWO TIMES
   *
   * = ANY REF WITH RED WITH COLD WITH GREEN WITH BLUE WITH WHITE
   * TYPE LINEARIZATION!
   * SUPER TAKES THE ELEMENT TO THE LEFT
   * THERE IS NO SUPER IN COLD, THE "CHAIN" GETS BROKEN THERE
   *
   */
}
