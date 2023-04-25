package lectures.part2

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y => 3 +y
  println(add3(4))
  println(superAdder(3)(4)) // curried function - multiple paramether functions

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  //we converted a method into a function values (to use high order function)
  val add4: Int => Int = curriedAdder(4) // without Int => Int it breaks

  //lifting = ETA-EXPANSION
  // functions != method (JVM limiation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) //behind the scens we have ETA-expansions turns method into function
  List(1,2,3).map(x => inc(x)) // that's what compiler does

  // Partial function applications
  val add5 = curriedAdder(5) _ // do an eta expansion for me and convert to an Int => Int

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x:Int)(y: Int) = x + y

  // define add7  Int => Int = y => 7+y out of these 3 version
  // as many different as you can

  val add7: Int => Int = curriedAddMethod(7)
  val add7_2 = curriedAddMethod(7) _
  val add7_3 = (x: Int) => simpleAddFunction(7, x)
  val add7_4 = (x: Int) => simpleAddMethod(7, x)
  val add7_5 = (x: Int) => curriedAddMethod(7)(x)
  val add7_6 = curriedAddMethod(7)(_)
  val add7_7 = simpleAddMethod(7, _: Int)

  def concatenator(a: String, b: String, c:String) = a + b + c
  val insertName = concatenator("Hello I'm ", _: String, ", how are you")
  println(insertName("Davide"))

  // Exercises
  /*
  * 1. Process a list of numbers and return their string represntation with different formats
  *    Use %4.2f %8.6f %14.12f with a curried formatter
  *
  * 2. difference between
  * - functions vs method
  * - paramter: by-name vs 0-lamba
  *
  * define 2 methods
  * def byName(n => Int) = n +1
  * def byFunction(f: () =>Int) = f() + 1
  *
  * def method: Int = 42
  * def parentMethod(): Int = 42
  *
  * calling byName and byFunction with
  * - int
  * - method
  * - parentMethod
  * - lambda
  * - PAF
  *
   */
  println("%4.2f".format(Math.PI))

  def curriedFormatter(number: Double)(repr: String): String = repr.format(number)
  val simpleFormat = curriedFormatter(_)("%4.2f")
  val seriousFormat = curriedFormatter(_)("%8.6f")
  val preciseFormat = curriedFormatter(_)("%14.12f")

  //2
  def byName(n: => Int) = n +1
  def byFunction(f: () =>Int) = f() + 1

  def method: Int = 42
  def parentMethod(): Int = 42

  byName(23)
  byName(method) //ok, evalauted
  byName(parentMethod())
  byName(parentMethod) //ok but beware, this is called, ==> byName(parentMethod())
  // byName(() => 42 ) // NOT OK
  byName((()=>42)()) //this is ok, I'm calling the lambda
  // byName(parentMethod _ ) NOT OK

  // byFunction(45) // NOT OK
  // byFunction(method) // NOT OK!!!! evaluated to his value, 42 - DOES NOT DO ETA EXPANSION
  byFunction(parentMethod) //ok compiler does ETA expansion
  byFunction(() => 42) // OK function value,
  byFunction(parentMethod _) //ok


}
