package lectures.part4

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age) yo <a href=$email> </div>"
  }

  User("John", 32, "john@gmail.com").toHtml
  /*
  * Disadvantages
  * 1 - for the types WE write
  * 2 - ONE implementation out of quite a number
  *
   */

  // option 2 - pattern matching
  object HtmlSerialiazerPM {
    def serializableToHtml(value: Any): Unit = value match {
      case User(n, a, e) =>
      //case java.util.Date =>
      case _ =>
    }
  }

  /*
  * Disadvantages
  * 1 - lost type safety
  * 2 - modify the code every time
  * 3 - still one implementation for every time
   */

  trait HTMLSerializer[T]{
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String = s"<div>${value.name} (${value.age}) yo <a href=${value.email}> </div>"
  }

  val john = User("John", 32, "john@gmail.com")
  // println(UserSerializer.serialize(john))

  // 1 - we can define serializer for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date]{
    override def serialize(value: Date): String = s"<div>${value.toString}</div>"
  }

  // 2 - we can define multiple serializer for same tipe
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String = s"<div>${value.name}</div>"
  }

  // TYPE CLASS -> HTMLSerializer
  // TYPE CLASS INSTANCES -> UserSerializer ecc ecc --- we use abstract class


  // TYPE CLASS
  trait MyTypeClassTemplate[T]{
    def action(value: T): String
  }

  object MyTypeClassTemplate{
    def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
  }

  /**
   * Equality
   */
  trait Equal[T]{
    def apply(value: T, value2: T): Boolean
  }

  implicit object UserEqualizerName extends Equal[User]{
    override def apply(value: User, value2: User): Boolean = value.name.equals(value2.name)
  }
  object UserEqualizerAge extends Equal[User]{
    override def apply(value: User, value2: User): Boolean = value.age == value2.age
  }

  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]) : String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int]{
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  //println(HTMLSerializer.serialize(42)(IntSerializer))

  println(HTMLSerializer.serialize(john)) //we made userSerializer implicit

  // we have access to the entire type class interface, not only the serializer
  println(HTMLSerializer[User].serialize(john))


  /**
   * Exercise : implement type class pattern for the equality
   */
  object Equal{
    def apply[T](a:T, b:T)(implicit equalizer: Equal[T]) : Boolean = equalizer.apply(a, b)
  }

  val anotherJohn = User("John", 45, "nono@gmail.com")

  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism

}

