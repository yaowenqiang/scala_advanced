package lectures.part3Concurrency

import java.util.concurrent.Executors

object Intro extends App {
    /*
    interface Runnable {
        public class run()
    }
     */
    // JVM threads
    val aThread = new Thread(new Runnable {
        override def run(): Unit = println("Running in parallel")
    })

//    val secondThread = new Runnable {
//        override def run(): Unit = println("Running secondThread")
//    }
//    secondThread.start()
    aThread.start() //give the signal to the JVM to start a JVM thread
    // create a JVM Thread

    aThread.join() // blocks until aThread finish running

    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
    val threadGoodBye = new Thread(() => (1 to 5).foreach(_ => println("Goodbye")))
    threadHello.start()
    threadGoodBye.start()
    // different run produces different result

    // executors

    val pool = Executors.newFixedThreadPool(10)
    pool.execute(() => {
        Thread.sleep(1000)
        println("done after 1 second")
    })

    pool.execute(() => {
        Thread.sleep(1000)
        println("almost done")
        Thread.sleep(1000)
        println("done after a second")
    })

    pool.shutdown()
//    pool.execute(() => println("Should not appear")) // throw an exception in the calling thread

//    pool.shutdownNow()
    println(pool.isShutdown)// shutdown means the pool does not accept more actions

}
