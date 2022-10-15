package lectures.part3Concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FuturesAndPromises extends App {
    def calculateMeansOfLife(): Int = {
        Thread.sleep(2000)
        42
    }

    val aFuture = Future {
        calculateMeansOfLife() // on run another thread
    } // (global)  passed by the compiler

    println(aFuture.value) // Option[Try[Int]]
    println("waiting for the future.")
//    aFuture.onComplete(t => t match {
//        case Success(meansOfLife) => println(s"the means of life is ${meansOfLife}")
//        case Failure(e) => println(s"I have failed with ${e}")
//    })
    aFuture.onComplete({
        case Success(meansOfLife) => println(s"the means of life is ${meansOfLife}")
        case Failure(e) => println(s"I have failed with ${e}")
    }) // called by some thread

    Thread.sleep(3000)

}
