package lectures.PartOneAdvancedScala

object AdvancedPatternMatching extends App {
    val numbers = List(1)
    val descriptions = numbers match {
        case head :: nil => println(s"the only element is $head")
        case _ =>
    }

    /*
     - constants
     - wildcards
     - case classes
     - tuples
     - some special magic like above
     */

    class Person(val name: String, val age: Int)

    object Person {
        def unapply(person: Person) :Option[(String, Int)] = Some((person.name, person.age))
//        def unapply(person: Person) :Option[(String, Int)] =
//            if(person.age < 22) None
//            else Some((person.name, person.age))
        def unapply(age: Int) :Option[String] = Some(if (age > 20) "minor" else "major" )
    }

    val bob = new Person("bob", 20)
    val greeting = bob match {
        case Person(n, a) => s"Hi, my name is $n and i am $a yo."
    }

    val legalStatus = bob.age match {
        case Person(status) => s"my legal status is $status"
    }
    println(greeting)
    println(legalStatus)

    val n: Int = 8
    val matchProperty = n match {
        case x if x < 10 => "single digit"
        case x if x %2 == 0 => "an even number"
        case _ => "no property"
    }

//    object even {
//        def unapply(arg: Int) :Option[Boolean] =
//            if (arg % 2 == 0)  Some(true)
//            else None
//    }

    object even {
        def unapply(arg: Int) :Boolean = arg % 2 == 0
    }

    object singleDigit {
        def unapply(arg: Int) :Boolean =
            if (arg > -10 && arg < 10) true
            else false
    }
//    object singleDigit {
//        def unapply(arg: Int) :Option[Boolean] =
//            if (arg > -10 && arg < 10) Some(true)
//            else None
//    }

    val matchProperty2 = n match {
        case singleDigit => "single digit"
        case even => "an even number"
//        case even() => "an even number"
        case _ => "no property"
    }

    println(matchProperty2)

    // infix pattern

    case class Or[A, B](a: A, b:B) //Either
    val either = Or(2, "two")
    val humanDescription = either match {
        case number Or string => s"$number is written as $string"
    }
    println(humanDescription)

    // decomposing sequences
    val vararg = numbers match {
        case List(1, _*) => "starting with 1"
    }

    abstract class MyList[+A] {
        def head: A = ???
        def tail: MyList[A] = ???
    }
    case object Empty extends MyList[Nothing]
    case class Cons[+A](override val head : A ,override val tail: MyList[A]) extends MyList[A]

    object MyList {
        def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
            if(list == Empty) Some(Seq.empty)
            else unapplySeq(list.tail).map(list.head +: _)
    }

    val myList :MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
    val decomposed = myList match {
        case MyList(1, 2, _*) => "Starting with 1 and 2"
        case _ => "something else"
    }
    println(decomposed)

    // custom return types for unapply must have the two methods below:
    // def isEmpty: Boolean,
    // def get something

    abstract class Wrapper[T] {
        def isEmpty: Boolean
        def get: T
    }

    object PersonWrapper {
        def unapply(person: Person) : Wrapper[String] =
            new Wrapper[String] {
                def isEmpty  = false
                def get :String = person.name
            }
    }

    println( bob match {
        case PersonWrapper(n) => s"this person 's name is $n"
        case _ => "an alien"
    }

    )
}
