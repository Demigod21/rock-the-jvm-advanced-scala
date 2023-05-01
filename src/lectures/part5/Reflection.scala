package lectures.part5

object Reflection extends App {

  // reflection + macros + quasiquotes => METAPROGRAMMING
  case class Person(name: String) {
    def sayMyName(): Unit = println(s"Hi my name is $name")
  }
  // 0 - import

  import scala.reflect.runtime.{universe => ru}

  // 1 - MIRROR
  val m = ru.runtimeMirror(getClass.getClassLoader)

  // 2 - create a class object
  val clazz = m.staticClass("lectures.part5.Reflection.Person") // creating a class object by NAME
  // kinda description of class

  // 3 - create a reflected mirror
  val cm = m.reflectClass(clazz)
  // can access the class and its members and methods

  // 4 - get the constructor
  val constructor = clazz.primaryConstructor.asMethod // symbol

  // 5 - reflect the constructor
  val constructorMirror = cm.reflectConstructor(constructor)

  // 6 - invoke the constructor
  val instance = constructorMirror.apply("john")

  println(instance)
  // I have an instance
  val p = Person("Mary") // from the wire as a serialized object
  // method name computed from somewhere else
  val methodName = "sayMyName"
  // 1 - mirror
  // 2 - reflect the instance
  val reflectedMary = m.reflect(p)
  // 3 - method symbol
  val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
  // 4 - reflect the method
  val method = reflectedMary.reflectMethod(methodSymbol)
  // 5 - invoke the method
  method.apply()

  // type erasure
  // history : jvm erase type for backwards compatibilty, started with java 5 and generics being erased

  // pp # 1 : differentiate types at runtime
  val numbers = List(1, 2, 3)
  numbers match {
    case listofStrings: List[String] => println("List of string")
    case listofInt: List[Int] => println("List of int")
  } // generics are eliminated at runtime, so it match a list only

  // pp #2 limitations on overloads
  // def processList(list: List[Int]): Int = 43 sa,e
  // def processList(list: List[String]): Int = 45 same

  // TypeTags

  // 0 - import
  import ru._
  val ttag = typeTag[Person]
  println(ttag.tpe)

  class MyMap[K, V]

  def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]): List[Type] = typeTag.tpe match {
    case TypeRef(_, _, typeArgs) => typeArgs
    case _ => List()
  }

  val myMap = new MyMap[Int, String]
  val typeArgs = getTypeArguments(myMap) //implicit typeTag : TypeTag[MyMap[Int, String]]
  println(typeArgs)

  def isSubtype[A, B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean = {
    ttagA.tpe <:< ttagB.tpe
  }

  class Animal
  class Dog extends Animal
  println(isSubtype[Dog, Animal])

}
