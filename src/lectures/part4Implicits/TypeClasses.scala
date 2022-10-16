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
    implicit object UserSerializer extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href='${user.email}'>email me</a></div>"
    }
    println(UserSerializer.serialize(john))
    import java.util.Date
    object DateSerializer extends HTMLSerializer[Date] {
        override def serialize(date: Date): String = s"<div>${date.toString}</div>"
    }

    object PartialUserSerialize extends HTMLSerializer[User] {
        override def serialize(user: User): String = s"<div>${user.name}</div>"
    }

    trait Equal[T] {
        def apply(a: T, b: T): Boolean
    }

//    implicit object NameEquality extends Equal[User] {
//        override def apply(a: User, b: User): Boolean = a.name == b.name
//    }

    implicit object FullEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
    }

    object HTMLSerializer {
        def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
            serializer.serialize(value)

        def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
    }

    implicit object IntSerializer extends HTMLSerializer[Int] {
        override def serialize(value: Int): String = s"<div style='color: blue;'>${value}</div>"
    }

    println(HTMLSerializer.serialize(41)(IntSerializer))
    println(HTMLSerializer.serialize(41))
    println(HTMLSerializer.serialize(john))

    // access to the entire type class interface
    println(HTMLSerializer[User].serialize(john))


    object Equal {
        def apply[T] (a: T, b: T) (implicit equalizer: Equal[T]) :Boolean =
            equalizer.apply(a, b)
    }

    // part 3

    implicit class HTMLEnrichment[T](value: T) {
        def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
    }

    println(john.toHTML(UserSerializer)) // println((new HTMLEnrichment[User](john).toHTML(UserSerializer))

    println(john.toHTML)

    println(2.toHTML)

    println(john.toHTML(PartialUserSerialize))

    /*
    - type class itself --- HTMLSerializer[T] { ... }
    - type class instances(some of witch are implicit) --- UserSerializer, IntSerializer
    - conversion with implicit class --- HTMLEnrichment
     */

    /*
    context bounds
     */

    def HtmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
        s"<html><body>${content.toHTML(serializer)}</body></html>"

    def HTMLSugar[T: HTMLSerializer](content: T): String = {
        val serializer = implicitly[HTMLSerializer[T]]
//        s"<html><body>${content.toHTML}</body></html>"
        s"<html><body>${content.toHTML(serializer)}</body></html>"
    }

    // implicitly
    case class Permissions(mask: String)
    implicit val defaultPermissions  = Permissions("0744")

    // TODO
    val standardPerms = implicitly[Permissions]


}


