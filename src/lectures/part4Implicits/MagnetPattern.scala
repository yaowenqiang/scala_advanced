package lectures.part4Implicits

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object MagnetPattern extends App {
    class P2PRequest

    class P2PResponse

    class Serializer[T]

    trait Actor {
        def receive(statusCode: Int): Int

        def receive(request: P2PRequest): Int

        def receive(response: P2PResponse): Int

        //        def receive[T: Serializer](message: T)(implicit serializer: Serializer[T]): Unit
        def receive[T: Serializer](message: T): Int

        def receive[T: Serializer](message: T, statusCode: Int): Int

        def receive(future: Future[P2PRequest]): Int
        //        def receive(future: Future[P2PResponse]): Int

        /*
        i. type erasure
        2. Lifting doesn't work for all overloads
        val receiveValue = receive _
        3. code duplication
        4. type inference and default arguments

         */


    }

    trait MessageMagnet[Result] {
        def apply(): Result
    }

    def receive[R](magnet: MessageMagnet[R]): R = magnet()

    implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
        def apply(): Int = {
            println("handling P2P request")
            42
        }
    }

    implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
        def apply(): Int = {
            println("handling P2P response")
            24
        }
    }

    receive(new P2PRequest)
    receive(new P2PResponse)

    // no more type erasure problems
    implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
        override def apply(): Int = 2
    }

    implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
        override def apply(): Int = 3
    }

    println(receive(Future(new P2PRequest)))
    println(receive(Future(new P2PResponse)))

    // lifting works
    trait mathLib {
        def add1(n: Int): Int = n + 1

        def add1(s: String): Int = s.toInt + 1
    }

    //    trait addMagnet[T] {
    trait addMagnet {
        def apply(): Int
    }

    def add1(magnet: addMagnet): Int = magnet()

    implicit class AddInt(x: Int) extends addMagnet {
        override def apply(): Int = x + 1
    }

    implicit class AddString(s: String) extends addMagnet {
        override def apply(): Int = s.toInt + 1
    }

    val addFV = add1 _
    println(addFV(1))
    println(addFV("1"))

    //    val receiveFV = receive _
    //    receiveFV(new P2PResponse)

    /*
    Drawbacks
    1 - verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - call by name doesn't work correctly
     */

    class Handler {
        def handle(s: => String): Unit = {
            println(s)
            println(s)
        }
    }

    trait HandleMagnet {
        def apply(): Unit
    }

    def handle(magnet: HandleMagnet): Unit = magnet()

    implicit class StringHandle(s: => String) extends HandleMagnet {
        def apply(): Unit = {
            println(s)
            println(s)
        }
    }

    def sideEffectMethod(): String = {
        println("Hello, Scala")  // only print once
        "magnet"
    }
    // a function call
//    handle(sideEffectMethod())

    // a  expression call
    handle {
        println("Hello, handle") // side effect
        "magnet" // new StringHandle("magnet")
    }
    // careful, hard to trace

}
