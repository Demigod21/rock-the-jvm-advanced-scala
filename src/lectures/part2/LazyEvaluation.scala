package lectures.part2

object LazyEvaluation extends App {

  //delays evaluation
  lazy val x: Int = {
    println("hello")
    42
  } //evaluated only when used for the first time and only once

  println(x)
  println(x) //we only see one hello

  //example of implications:
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if(simpleCondition && lazyCondition) "yes" else "no") //we will not see "Boo"
  //lazyCondition never evaluated

}
