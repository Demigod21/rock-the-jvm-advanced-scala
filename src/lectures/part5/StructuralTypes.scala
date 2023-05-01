package lectures.part5

object StructuralTypes extends App {

  // structural types
  type JavaClosable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("Closing...")

    def closeSilently(): Unit = println("Java closes silently")
  }

  // def closeQuitly(closable: JavaClosable OR HipsterCloseable)

  type UnifiedClosable = {
    def close(): Unit
  } // structural type

  def closeQuietly(unifiedClosable: UnifiedClosable): Unit = unifiedClosable.close()

  closeQuietly(new JavaClosable {
    override def close(): Unit = ???
  })

  closeQuietly(new HipsterCloseable)

  // TYPE REFINEMENTS

  type AdvancedCloseable = JavaClosable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaClosable {
    override def close(): Unit = println("Java closes")

    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit = advancedCloseable.closeSilently()

  closeShh(new AdvancedJavaCloseable)
  // closeShh(new HipsterCloseable) // NOT OK

  // using structural types as standalone types
  def altClose(closeable: {def close(): Unit}): Unit = closeable.close()

  // type-checking => duck typing
  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vroom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car
  // static duck typing
  // CAVEAT: based on reflection

  /**
   * Exercises
   */
  trait CBL[+T] {
    def head: T

    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAIN"
  }

  def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

  /*
    Q: f is compatible with CBL and with a human?
    A: compatible with both
   */

  // 2
  object HeadEqualizer {
    type Headable[T] = {def head: T}

    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  /*
  Q: is it compatible with CBL and with a human?
  A: compatible with both
   */


}
