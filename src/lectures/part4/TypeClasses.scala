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

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String = s"<div>${value.name} (${value.age}) yo <a href=${value.email}> </div>"
  }

  val john = User("John", 32, "john@gmail.com")
  // println(UserSerializer.serialize(john))

  // 1 - we can define serializer for other types

  import java.util.Date

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(value: Date): String = s"<div>${value.toString}</div>"
  }

  // 2 - we can define multiple serializer for same tipe
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(value: User): String = s"<div>${value.name}</div>"
  }

  // TYPE CLASS -> HTMLSerializer
  // TYPE CLASS INSTANCES -> UserSerializer ecc ecc --- we use abstract class


  // part 2
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  println(HTMLSerializer.serialize(42))
  //println(HTMLSerializer.serialize(42)(IntSerializer))

  println(HTMLSerializer.serialize(john)) //we made userSerializer implicit

  // we have access to the entire type class interface, not only the serializer
  println(HTMLSerializer[User].serialize(john))


  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(john.toHTML) // user serializer is implicit
  // COOOL
  /**
   * - extends to new types
   * - different implementation for the same type
   */
  println(2.toHTML) // we have an implicit serializer for int
  println(john.toHTML(PartialUserSerializer)) //different implementation for same type, we just pass the implicity

  /**
   * MAIN ELEMENTS OF ENHANCING TYPE WITH TYPE CLASSES
   *  - type class itself --- HTMLSerializer[T]{---}
   *  - type class instances (some of which are implicit) --- UseriSerializer, IntSerializer...
   *  - conversion class implicit --- HTMLEnrichment[T]
   */

  // context bounds
  def htmlBoilerPlate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String =
    s"<html><body>${content.toHTML}</body></html>" // context bound that inject the implicit
  //we cannot use serializer by name, because compiler inject

  // implicitly
  case class Permissions(mask: String)

  implicit val defaultPermissions = Permissions("0744")
  // in some other part of the code
  // we want to surface out
  val standardPerms = implicitly[Permissions]
}

