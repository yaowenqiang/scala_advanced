package lectures.part4Implicits

object TypeClasses extends App {
    trait HTMLWritable {
        def toHtml: String
    }

    case class User(name: String, age: Int, email: String) extends HTMLWritable {
        override def toHtml: String = s"<div>${name} (${age} yo) <a href='${email}'>email me</a></div>"
    }
    val john = User("John", 32, "john@rockjvm.com")

//    object HTMLSerializer {
//        def serializaToHtml(value: Any) = value match {
//            case User(n, a, e) =>
//            case java.util.Date =>
//            case _ =>
//        }
//    }

    // type class
    trait HTMLSerializer[T] {
        def serialize(value: T) : String
    }

    // type class instances
    object UserSerialize extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href='${user.email}'>email me</a></div>"
    }
    println(UserSerialize.serialize(john))
    import java.util.Date
    object DateSerializer extends HTMLSerializer[Date] {
        override def serialize(date: Date): String = s"<div>${date.toString}</div>"
    }

    object PartialUserSerialize extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name}</div>"
    }

    trait MyTypeClassTemplate[T] {
        def action(value: T) :String
    }

    trait Equal[T] {
        def apply(a: T, b: T): Boolean
    }

    object NameEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name
    }

    object FullEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
    }
}

