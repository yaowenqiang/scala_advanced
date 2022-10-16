package lectures.part4Implicits

object ImplicitsIntro extends App {
    val pair = "Daniel" -> "999"
    val intPair = 1 -> 2

    case class Person(name: String) {
        def great = s"Hi, my name is $name"
    }
//    case class A(name: String) {
//        def great = s"Hi, my name is $name"
//    }
    implicit def fromStringToPerson(str: String) :Person  = Person(str)
//    implicit def fromStringToA(str: String) :A  = A(str)
    println("peter".great) // println(Person('peter").great

    // implicit parameters

    def increment(x: Int)(implicit amount: Int) = x + amount
    implicit val defaultAmount = 10

    increment(2)
//    increment(2)(defaultAmount)

}
