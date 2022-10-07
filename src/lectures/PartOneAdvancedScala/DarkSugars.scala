package lectures.PartOneAdvancedScala

import javax.print.MultiDoc
import scala.util.Try

object DarkSugars extends App {
    // syntax sugar #1: methods with single param
    def singleArgumentMethod(arg: Int) = s"$arg little ducks..."
    val descriptions = singleArgumentMethod{
        // write some complex code
        42
    }

    val aTryInstance = Try { // java's Try {...}
        throw new RuntimeException()
    }
    List(1,2,3).map { x =>
        x + 1
    }
    // syntax suer #2 single abstract method pattern
    trait Action {
        def act(a: Int) :Int
    }
    val anInstance = new Action {
        override def act(x: Int): Int = x + 1
    }

    val aFunkyInstance :Action = (x :Int) => x + 1 // magic

    // example: Runnable
    val aThread = new Thread(new Runnable {
        override def run(): Unit = ???
    })
    val aSweeterThread = new Thread(() => println("sweet, scala"))

    abstract class AnAbstractType {
        def implemented: Int = 24
        def f(a: Int) : Unit
    }
    val anAbstractTypeInstance :AnAbstractType = (a: Int) => println(a)


    // syntax suer #3 the :: and #::  methods are special
    val prependedList = 2 :: List(3,4)
    // 2.::(list(3,4))
    // List(3,4)::(2)
    // scala spec: last char decide associativity of method
    1 :: 2 :: 3 :: List(4,5)
    List(4,5).::(3).::(2).::(1)//equivalent

    class MyStream[T] {
        def -->:(value: T) = this
    }
    val myStream = 1 -->: 2 -->:3 -->: new MyStream[Int]

    // syntax suer #4 multi-word method naming
    class TeenGirl(name: String) {
        def `and then said`(gossip :String) = println(s"$name said $gossip")
    }
    val lilly = new TeenGirl("Lilly")
    lilly `and then said` "Scala is so sweet"

    // syntax suer #5 infix types
    class Composite[A, B]
//    val composite :Composite[Int, String] = ???
    val composite: Int Composite String = ???

    class -->[A,B]
    val towards: Int --> String = ???

    // syntax suer #6 update() is very special, much like apply()
    val anArray = Array(1,2,3)
    anArray(2) = 7 // overwrite to anArray.update(2, 7)
    //used in mutable collections
    // apply and update

    // syntax suer #7 setters for mutable collections
    class Mutable {
        private var internalNumber :Int = 0  //private for oo encapsulation
        def member:Int = internalNumber // "getter
        def member_=(value: Int) :Unit =
            internalNumber = value // "setter"
    }

    val aMutableContainer = new Mutable
    aMutableContainer.member = 42 //equals to aMutableContainer.member_=(42)













}
