package lectures.part2

object Monads extends App {

  //our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]) : Attempt[B]
  }

  object Attempt{
    def apply[A]( a: => A): Attempt[A] = {
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
    }
  }


  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] = try{
      f(value)
    }catch {
      case e: Throwable => Fail(e)
    }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  * 1) left-identity
  *
  * unit.flatMap(f) = f(x)
  * Attempt(x).flatMap(f) = f(x)
  * Success(x).flatmap(f) = f(x) //Proved
  *
  * 2) right-identity
  *
  * Attempt.flatmap(unit) = attempt
  * Success(x).flatmap(x => Attempt(x)) = Attempt(x) = Success(x) // Proved for success
  * Fail(_).flatMap(...) = Fail(e) // Proved for failure
  *
  * 3) associativity
  * attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g)))
  * Fail(e).flatMap(f).flatMap(g) = Fail(e).flatMap(g) = Fail.(e)
  * Fail(e).flatMap(x => f(x).flatMap(g))) = Fail(e) // Proved
  *
  * Success(v).flatMap(f).flatMap(g) =
  *  f(v).flatMap(g) OR Fail(e)
  *
  *  Success(v).flatMap(x => f(x).flatMap(g)) =
  *  f(v).flatMap(g) OR Fail(e)
  *
  * They're the same // Proved
   */

  val attempt = Attempt{
    throw new RuntimeException("My Monad")
  }

  println(attempt)

  /*
    EXERCISE - 1: implemenet a Lazy[T] monad = will only be executed when it's needed

    unit/apply
    flatMap

    EXERCISE - 2:
    Monads = unit + flatmap
    Monads = unit + map + flatten
    Transform one monad from 1st definition, to another
    Implement map and flatten using flatMap

    def flatMap[B](f: T => Monad[B]) : Monad[B] = .... (implemented)

    def map[B](f: T => B): Monad[B] = ???
    def flatten(m: Monad[Monad[T]]) : Monat[T] = ???

    (list in mind)
   */

  class Lazy[+A](value: => A){
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=>A) => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy{
    def apply[A](value: => A): Lazy[A] = {
      new Lazy(value)
    }
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

 // println(lazyInstance.use)

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy{
    10 * x
  })
  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy{
    10 * x
  })
  flatMappedInstance.use
  flatMappedInstance2.use


  /*
    Exercise 2
        def flatMap[B](f: T => Monad[B]) : Monad[B] = .... (implemented)

    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x)))
    def flatten(m: Monad[Monad[T]]) : Monad[T] = m.flatMap(x : Monad[T] => x)

    MAP =====>  List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
    FLATTEN =>  List(1,2,3).flatten = List(List(1,2,3), List(4,5)).flatMap(x => x) = List(1,2,3,4)
   */


}
