package lectures.part5TypeSystem

object TypeMembers extends App{
    class Animal
    class Dog extends Animal
    class Cat extends Animal
    class AnimalCollection {
        type AnimalType
        type BoundedAnimal <: Animal
        type SuperBoundedAnimal >: Dog <: Animal
        type AnimalC = Cat
    }

    val ac = new AnimalCollection
//    val dog: ac.AnimalType = ???
//    val cat: ac.BoundedAnimal = ???

    val pop : ac.SuperBoundedAnimal = new Dog;
    val cat : ac.AnimalC = new Cat
    type CatAlias = Cat
    val anotherCat :CatAlias = new Cat

    trait MyList {
        // alternative to generics
        type T
        def add(element: T): MyList
    }

    class NonEmptyList(value: Int) extends  MyList {
        override type T = Int

        override def add(element: Int): MyList = ???
    }


    // .type

    type CatType = cat.type
    val newCat : CatType = cat
//    new CatType


    trait MList {
        type A
        def head : A
        def tail: MList

    }

    trait ApplicableToNumbers {
        type A <: Number
    }

    // not ok
//    class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
//        type A = String
//        def head :String = hd
//        def tail = tl
//    }

    // ok
    class IntList(hd: Int, tl: IntList) extends MList {
        type A = Int
        def head :A = hd
        def tail = tl
    }



}
