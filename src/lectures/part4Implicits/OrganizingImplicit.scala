package lectures.part4Implicits

object OrganizingImplicit extends App {
    implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
    //    implicit val reverseOrdering(): Ordering[Int] = Ordering.fromLessThan(_ > _)
    //    implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)
    println(List(3, 6, 7, 4, 4).sorted)
    // scala.Predef

    /*
    Implicits (used as implicit parameters)
     - val/var
     - object
     - accessor methods = defs with no parentheses
     */

    case class Person(name: String, age: Int)

    val persons = List(
        Person("Steve", 50),
        Person("Amy", 22),
        Person("John", 30),
        Person("Zoy", 19),
    )

//    object implicitObject {
//        implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a,b)  => a.name.compareTo(b.name) < 0)
//    }

//    object Person {
//        implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a,b)  => a.name.compareTo(b.name) < 0)
//    }
        object  AlphabeticOrdering {
            implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a,b)  => a.name.compareTo(b.name) < 0)
        }
        object AgeOrdering {
            implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a,b)  => a.name.compareTo(b.name) < 0)
        }

//    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a,b)  => a.age < b.age)
//    println(persons.sorted)
//    import AlphabeticOrdering._
    import AgeOrdering._
    println(persons.sorted)

    /*
     implicit scope
     - normal scope = LOCAL SCOPE
     - imported scope
     - companions of all types involved in the method signature
     - List
     - Ordering
     - All the types involved or any supertype
     */
    // def sorted(B >: A](implicit ord: Ordering[B]): List[B]


//    object TotalPriceOrdering {
//        implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b)  => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
//    }
    object UnitCountOrdering {
        implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
    }
    object UnitPriceOrdering {
        implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
    }
    case class Purchase(nUnits: Int, unitPrice: Double)
    object Purchase {
        implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b)  => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
    }
}
