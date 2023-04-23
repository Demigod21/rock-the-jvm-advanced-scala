package lectures.part2

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int) === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x ==2) 56
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
  }

  //{1,2} => Int
  // Partial function from int to int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
  } // partial function value

  println(aPartialFunction(2)) // 56
  //println(aPartialFunction(8)) // match error

  //PF utilities
  println(aPartialFunction.isDefinedAt(4)) //false

  // lift
  val lifted = aPartialFunction.lift //Int => Option[Int]
  println(lifted(2)) // Some(56)
  println(lifted(5)) // None

  //chain
  val pfChain = aPartialFunction.orElse[Int, Int]{
    case 45 => 67
  }
  println(pfChain(2)) // 56
  println(pfChain(45)) // 67

  // PF extends normal functions

  val aTotalFunction : Int => Int = {
    case 1 => 999
  }

  // HOFs accept partial function as well
  val aMappedList = List(1,2,3).map{
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note : partial functions can only have ONE param type
   */


  /*
    * Exercises
    * 1 - construct PF instance yourself (anon class)
    * 2 - dumb chat bot as a PF
   */


  val aManualFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 78
    }
    override def isDefinedAt(x: Int) : Boolean = x == 1 || x == 2
  }

  val chatBot: PartialFunction[String, String] = {
    case "Ciao" => "Ciao a te"
    case "Addio" => "Arrivederci"
  }

  scala.io.Source.stdin.getLines().foreach(line => println("chatbot dice: "+ chatBot(line)))
  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
  

}
