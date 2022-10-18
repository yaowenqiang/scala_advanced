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

//    exercises

    class Vehicle
    class Bike extends Vehicle
    class Car extends Vehicle

    class IParking[T](vehicles: List[T]) {
        def park[T](vehicle: Vehicle): IParking[T] = ???
        def impound(vehicles: List[Vehicle]) : IParking[T] = ???
        def checkVehicles(conditions: String) : List[T] = ???
        def flatMap[T](f: T => IParking[T]) : IParking[T] = ???
    }
    class CParking[+T](vehicles: List[T]) {
        def park[S >: T](vehicle: S): CParking[S] = ???
        def impound[S >: T](vehicles: List[S]) : CParking[S] = ???
        def checkVehicles(conditions: String) : List[T] = ???
        def flatMap[S](f: T => CParking[S]) : IParking[S] = ???
    }
    class XParking[-T](vehicles: List[T]) {
        def park(vehicle: T): XParking[T] = ???
        def impound(vehicles: List[T]) : XParking[T] = ???
        def checkVehicles[S <: T](conditions: String) : List[S] = ???
//        def flatMap[S](f: T => XParking[S]) : XParking[S] = ???
        // TODO
//        def flatMap[R <:T, S](f: T => XParking[R]) : XParking[S] = ???
        def flatMap[R <:T, S](f: Function1[R, XParking[S]]) : XParking[S] = ???
    }
    class IList[T]

    /*
    rule of thumb:
    - use covariance = collection of things
    - use contravariance = group or actions
     */


    class CParking2[+T](vehicles: IList[T]) {
        def park[S >: T](vehicle: S): CParking[S] = ???
        def impound[S >: T](vehicles: IList[S]) : CParking2[S] = ???
        def checkVehicles[S >: T](conditions: String) : IList[S] = ???
    }
    class XParking2[-T](vehicles: IList[T]) {
        def park(vehicle: T): XParking2[T] = ???
        def impound[S <: T](vehicles: IList[S]) : XParking2[S] = ???
        def checkVehicles[S <: T](conditions: String) : IList[S] = ???
    }



}
