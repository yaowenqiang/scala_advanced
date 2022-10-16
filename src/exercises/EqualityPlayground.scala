package exercises

object EqualityPlayground extends App {

    trait HTMLWritable {
        def toHtml: String
    }

    case class User(name: String, age: Int, email: String) extends HTMLWritable {
        override def toHtml: String = s"<div>${name} (${age} yo) <a href='${email}'>email me</a></div>"
    }

    trait Equal[T] {
        def apply(a: T, b: T): Boolean
    }

    object Equal {
        def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
            equalizer.apply(a, b)
    }

//    implicit object NameEquality extends Equal[User] {
//        override def apply(a: User, b: User): Boolean = a.name == b.name
//    }

    implicit object FullEquality extends Equal[User] {
        override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
    }

    val john = User("John", 32, "john@rockjvm.com")
    val anotherJohn = User("John", 32, "john@rockjvm.com")
    println(Equal.apply(john, anotherJohn))
    println(Equal(john, anotherJohn))
    // AD-HOC polymorphism


    implicit class TypeSafeEqual[T](value: T) {
        def ===(other: T)(implicit equalizer: Equal[T]):Boolean = equalizer.apply(value, other)
        def !==(other: T)(implicit equalizer: Equal[T]):Boolean = !equalizer.apply(value, other)
    }

    println(john === anotherJohn)
    /*
    john.===(anotherJohn)
    new typeSafeEqual(john).===(anotherJohn)
    new typeSafeEqual(john).===(anotherJohn).nameEquality(john)

     */
    /*
    type safe
     */

    //    println(john == 42)
//    println(john === 42) // type safe, compile wile fail

}
