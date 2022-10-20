package lectures.part5TypeSystem

object DuckTyping extends App {
    // structural type
    type JavaClosable = java.io.Closeable

    class HipsterClosable {
        def close(): Unit = println("Yeah yeah I'm closing.")

        def closeSilently(): Unit = println("Not making a sound")
    }

    //    def closeQuietly(closable: JavaClosable or HipsterClosable): Unit = {
    //
    //    }

    type UnifiedClosable = {
        // structural type
        def close(): Unit
    }

    def closeQuietly(unifiedClosable: UnifiedClosable): Unit = {
        unifiedClosable.close()
    }

    closeQuietly(new JavaClosable {
        override def close(): Unit = println("closed")
    })
    closeQuietly(new HipsterClosable)

    // type refinements

    type AdvancedClosable = JavaClosable {
        def closeSilently(): Unit
    }

    class AdvancedJavaClosable extends JavaClosable {
        def close(): Unit = println("Java close")

        def closeSilently(): Unit = println("Java close silently")
    }

    def closeShh(advClosable: AdvancedClosable): Unit = advClosable.closeSilently()

    // TODO
//    closeShh(new AdvancedClosable)
    //    closeShh(new HipsterClosable)

    // using structural types as standalone types
    def altClose(closable: {def close(): Unit}): Unit = closable.close()

    // type-checking - duck typing

    type SoundMaker = {
        def makeSound(): Unit
    }

    class Dog {
        def makeSound(): Unit = println("Bark!")
    }

    class Car {
        def makeSound(): Unit = println("vroom")
    }

    // static duck typing
    // duck test

    val dog: SoundMaker = new Dog
    val car: SoundMaker = new Car


    // CAVEAT: based on reflection

    trait CBL[+T] {
        def head: T

        def tail: CBL[T]
    }

    class Human {
        def head: Brain = new Brain
    }

    class Brain {
        override def toString: String = "BRAIN!"
    }

    def f[T](somethingWithAHead: {def head: T}): Unit = println(somethingWithAHead.head)

    case object CBLNil extends CBL[Nothing] {
        def head: Nothing = ???

        def tail: CBL[Nothing] = ???
    }

    case class CBCons[T](override val head: T, override val tail: CBL[T]) extends CBL[T]
    f(CBCons(2,CBLNil))
    f(new Human) // T = Brain

    object HeadEqualizer {
        type Headable[T] = {def head: T}
        def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
    }

    val brainList = CBCons(new Brain, CBLNil)
    HeadEqualizer.===(brainList, new Human)

    val stringList = CBCons("brains", CBLNil)
    HeadEqualizer.===(new Human, stringList) // not type safe

}


