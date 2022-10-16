package lectures.part5TypeSystem

import lectures.PartOneAdvancedScala.AdvancedPatternMatching.MyList

object Variance extends App {
    trait Animal
    class Dog extends Animal
    class Cat extends Animal
    class Kitty extends Cat
    class Crocodile extends Animal
    // what is variance?
    // "Inheritance" type substitution of generics

    class Cage[T]
    // yes - variance
    class ICage[T]
    class CCage[+T] // covariant
    class XCage[-T] // contravariance

    val cCage : CCage[Animal] = new CCage[Cat]
//    val iCage : ICage[Animal] = new ICage[Dog]
    // hell no - opposite - contravariance
    val xCage : XCage[Cat] = new XCage[Animal]

    class InvariantCage[T](val animal: T) // invariant

    // covariant positions
    class CovariantCage[+T](val animal: T) // covariant position
//    class ContravariantCage[-T](val animal: T) // covariant position
    /*
    val catCage : XCage[Cat = new XCage[Animal](new Dog())
     */


//    class CovariantVariableCage[+T](var animaL: T) // types of vars are in contrvavariant position
    /*
    val cCage :XCage[Animal] = new CCage[Cat](new Cat)
    cCage.animal = new Crocodile()
     */

//    class ContraCovariantVariableCage[-T](var animaL: T)// types of vars are in contrvavariant position

        class InvariantVariableCage[T](var animaL: T) // this is ok


//    trait anotherCovariantCage[+T]{
//        def addAnimal(animal: T) //Contravariant position
//
        /*
        var ccage : CCage[Animal] = new CCage[Dog]
        ccage.add(new Cat

         */
//    }

        class AnotherCovariantCage[-T] {
            def addAnimal(animal: T) = true
        }

    val acc :AnotherCovariantCage[Cat] = new AnotherCovariantCage[Animal]
    acc.addAnimal(new Cat)
    acc.addAnimal(new Kitty)

    class MyList[+A] {
        def add[B >: A](element: B) : MyList[B] = new MyList[B] // widening the type
    }

//    val emptyList = MyList[Kitty]
//    val animals = emptyList.add(new Kitty)
//    val moreAnimals = animals.add(new Cat)
//    val evenMoreAnimals = moreAnimals.add(new Dog)

    // method arguments are in contravariant position
    // method return types

    class PetShop[-T] {
//        def get(isItaPuppy: Boolean) : T // method return types are in covariant positions
        /*
        val catShop = new PetShop[Animal] {
        def get(isItaPuppy: Boolean) : Animal = new Cat
        val dogShop : PetShop[Dog] = catShop
        dogShop.get(true)
         */
        def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S) : S = defaultAnimal
    }

    val shop : PetShop[Dog] = new PetShop[Animal]
//    val animal = shop.get(true, new Cat)

    class TerraNova extends Dog
    val bigFurry = shop.get(true, new TerraNova)

    /*
    // Big Rule
    - method arguments are in contravariant position
    - return type are in covariant position
     */

}
