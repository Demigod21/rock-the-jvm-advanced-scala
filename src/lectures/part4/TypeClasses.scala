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

  object UserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String = s"<div>${value.name} (${value.age}) yo <a href=${value.email}> </div>"
  }

  val john = User("John", 32, "john@gmail.com")
  println(UserSerializer.serialize(john))

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

  /**
   * Equality
   */
  trait Equal[T]{
    def equal(value: T, value2: T): Boolean
  }
  object UserEqualizerName extends Equal[User]{
    override def equal(value: User, value2: User): Boolean = value.name.equals(value2.name)
  }
  object UserEqualizerAge extends Equal[User]{
    override def equal(value: User, value2: User): Boolean = value.age == value2.name
  }

}

