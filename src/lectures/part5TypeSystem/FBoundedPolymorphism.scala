package lectures.part5TypeSystem

object FBoundedPolymorphism extends App {
    //   trait Animal {
    //       def bread: List[Animal]
    //   }
    //    class Cat extends Animal {
    //        override def bread: List[Animal] = ??? // List[Cat]
    //    }
    //
    //    class Dog extends Animal {
    //        override def bread: List[Animal] = ??? // List[Dog]
    //    }

    // solution 1 naive
    //       trait Animal {
    //           def bread: List[Animal]
    //       }
    //        class Cat extends Animal {
    //            override def bread: List[Cat] = ??? // List[Cat]
    //        }
    //
    //        class Dog extends Animal {
    //            override def bread: List[Cat] = ??? // List[Dog]
    //        }

    // solution 2 f-bounded-polymorphism

//    trait Animal[A <: Animal[A]] { // recursive type - f bounded Polymorphism
//        def bread: List[Animal[A]]
//    }
//
//    class Cat extends Animal[Cat] {
//        override def bread: List[Animal[Cat]] = ??? // List[Cat]
//    }
//
//    class Dog extends Animal[Dog] {
//        override def bread: List[Animal[Dog]] = ??? // List[Dog]
//    }
//
////    trait Entity[a <: Entity[E]] // ORM
//
//    class Person extends Comparable[Person] {
//        override def compareTo(o: Person): Int = ???
//    }
//
//    class Crocodile extends Animal[Dog] {
//        override def bread: List[Animal[Dog]] = ???
//    }

    // solution 3 f-bounded-polymorphism + self-types

//    trait Animal[A <: Animal[A]] {self: A =>  // recursive type - f bounded Polymorphism
//        def bread: List[Animal[A]]
//    }
//
//    class Cat extends Animal[Cat] {
//        override def bread: List[Animal[Cat]] = ??? // List[Cat]
//    }
//
//    class Dog extends Animal[Dog] {
//        override def bread: List[Animal[Dog]] = ??? // List[Dog]
//    }
//
////    class Crocodile extends Animal[Dog] {
////        override def bread: List[Animal[Dog]] = ???
////    }
//
//        class Fish extends Animal[Fish] {
//            override def bread: List[Animal[Fish]] = ???
//        }
//
//    class Shark extends Fish {
//        override def bread: List[Animal[Fish]] = List(new Cod) // wrong
//    }
//
//    class Cod extends Fish {
//        override def bread: List[Animal[Fish]] = ???
//    }

    // solution 4  type classes

//    trait CanBread[A] {
//        def bread(a: A) : List[A]
//    }
//
//    trait Animal
//
//    class Dog extends Animal
//    object Dog {
//        implicit object DogCanBread extends CanBread[Dog] {
//            def bread(a: Dog): List[Dog] = List()
//        }
//    }
//
//    implicit class CanBreadOps[A](animal: A) {
//        def bread(implicit canBread: CanBread[A]): List[A] =
//            canBread.bread(animal)
//    }
//
//    val dog = new Dog
//    dog.bread // List[Dog]
//    /*
//    new CanBreadOps[Dog](dog).bread
//    implicit value to pass to bread: Dog.DogCanBread
//     */
//
//    class Cat extends Animal
//    object Cat {
//        implicit object CatCanBread extends CanBread[Dog] {
//            override def bread(a: Dog): List[Dog] = ???
//        }
//
//        val cat = new Cat
//        cat.bread
//    }

    // solution 5


    trait Animal[A] { // pure type classes
        def bread[a: A] : List[A]
    }
    class Dog
    object Dog {
        implicit object DogAnimals extends Animal[Dog] {
            override def bread[a: Dog]: List[Dog] = List()
        }
    }

    class Cat
    object Cat {
        implicit object CatAnimal extends Animal[Dog] {
            override def bread[a: Dog]: List[Dog] = List()
        }
    }

    implicit class AnimalOps[A](animal: A){
       def bread(implicit animalTypeClassInstance: Animal[A]) : List[A] =
           animalTypeClassInstance.bread(animal)
    }

    val dog = new Dog
    dog.bread

    val cat = new Cat
    cat.bread

}
