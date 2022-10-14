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
//    threadHello.start()
//    threadGoodBye.start()
    // different run produces different result

    // executors

//    val pool = Executors.newFixedThreadPool(10)
//    pool.execute(() => {
//        Thread.sleep(1000)
//        println("done after 1 second")
//    })

//    pool.execute(() => {
//        Thread.sleep(1000)
//        println("almost done")
//        Thread.sleep(1000)
//        println("done after a second")
//    })

//    pool.shutdown()
    //    pool.execute(() => println("Should not appear")) // throw an exception in the calling thread

    //    pool.shutdownNow()
//    println(pool.isShutdown) // shutdown means the pool does not accept more actions

//    def runInParallel: Unit = {
//        var x = 0
//        var thread1 = new Thread(() => {
//            x = 1
//        })
//        var thread2 = new Thread(() => {
//            x = 2
//        })
//        thread1.start()
//        thread2.start()
//        println(x)
//    }

//    for (_ <- 1 to 10000) {
//        runInParallel
//    }

    // race condition

    class BankAccount(@volatile var amount: Int) {
        override def toString: String = "" + amount
    }

    def buy(account: BankAccount, thing: String, price: Int) = {
        account.amount -= price
//        println(s"I've bought ${thing}")
//        println(s"my account is now  ${account}")
    }

    def buySafe(account: BankAccount, thing: String, price: Int) = {
        account.synchronized( {
                // no two threads can evaluate this at the same time
                account.amount -= price
                println(s"I've bought ${thing}")
                println(s"my account is now  ${account}")
            }
        )
    }

//    for (_ <- 1 to 100) {
//        val account = new BankAccount(50000)
//        val thread1 = new Thread(() => buy(account, "Shoes", 3000))
//        val thread2 = new Thread(() => buy(account, "Iphone 12", 4000))
//        thread1.start()
//        thread2.start()
//        Thread.sleep(100)
//        if(account.amount != 43000) println(s"AHA: ${account.amount}")
//    }
/*
    for (_ <- 1 to 100) {
        val account = new BankAccount(50000)
        val thread1 = new Thread(() => buySafe(account, "Shoes", 3000))
        val thread2 = new Thread(() => buySafe(account, "Iphone 12", 4000))
        thread1.start()
        thread2.start()
        Thread.sleep(100)
        if(account.amount != 43000) println(s"AHA: ${account.amount}")
    }
*/

    // inception threads create new threads

    def inceptionThreads(maxThreads: Int, i: Int = 1) :Thread =
        new Thread(() => {
            if(i < maxThreads) {
                val newThread = inceptionThreads(maxThreads, i + 1)
                newThread.start()
                newThread.join()
            }
            println(s"Hello from thread: ${i}")
        })

    inceptionThreads(15).start()


    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    threads.foreach(_.join())
    println(x)
    // max of x is 100
    // min of x is 1


    // sleep fallacy

    var message = ""
    val awesomeThread = new Thread(() => {
        Thread.sleep(1000)
        message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()

    Thread.sleep(2000)
    awesomeThread.join() // wait awesomeThread finish
    println(message) //may be Scala sucks, or Scala is awesome, not guaranteed

}
