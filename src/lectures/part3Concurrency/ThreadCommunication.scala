package lectures.part3Concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
    /*
    the producer-consumer problem
     */

    class SimpleContainer {
        private var value: Int = 0

        def isEmpty: Boolean = value == 0

        def get: Int = {
            val result = value
            value = 0
            result
        }

        def set(newValue: Int): Unit = value = newValue

    }

    def naiveProdCons(): Unit = {
        val container = new SimpleContainer
        val consumer = new Thread(() => {
            println("consumer waiting")
            while (container.isEmpty) {
                println("[consumer] actively waiting...")
            }
            println(s"[consumer] I have consumed ${container.get}")
        })
        val producer = new Thread(() => {
            println("[producer] computing...")
            Thread.sleep(500)
            val value = 42
            println(s"[producer] I have produced, after long work, the value ${value}")
            container.set(value)
        })

        consumer.start()
        producer.start()


    }

    //    naiveProdCons()

    // wait and notify


    def smartProducerConsumer(): Unit = {
        val container = new SimpleContainer

        val consumer = new Thread(() => {
            println("[consumer] waiting")
            container.synchronized {
                container.wait()
            }

            // container must have some value
            print(s"[consumer] I have consumed ${container.get}")
        })

        val producer = new Thread(() => {
            print(s"[producer] hard at work...")
            Thread.sleep(2000)
            val value = 42
            container.synchronized {
                println(s"[producr] I'm producing ${value}")
                container.set(value)
                container.notify()
            }
        })

        consumer.start()
        producer.start()
    }

    //    smartProducerConsumer()


    def prodConsLargeBuffer(): Unit = {
        val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
        val capacity = 3

        val consumer = new Thread(() => {
            val random = new Random()
            while (true) {
                buffer.synchronized {
                    if (buffer.isEmpty) {
                        println("[consumer] buffer empty ,waiting...")
                        buffer.wait()
                    }

                    val x = buffer.dequeue()
                    println(s"[consumer] consumed ${x} from buffer")

                    buffer.notify()
                }
                Thread.sleep(random.nextInt(500))
            }
        })

        val producer = new Thread(() => {
            val random = new Random()
            var i = 0
            while (true) {
                buffer.synchronized {
                    if (buffer.size == capacity) {
                        println("[producer] buffer is full, waiting...")
                        buffer.wait()
                    }

                    println(s"[producer] producing ${i} into buffer")
                    buffer.enqueue(i)

                    buffer.notify()
                    i += 1

                }
                Thread.sleep(random.nextInt(500))
            }
        })
        consumer.start()
        producer.start()
    }

    //    prodConsLargeBuffer()


    class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
        override def run(): Unit = {
            val random = new Random()
            while (true) {
                buffer.synchronized {
                    while (buffer.isEmpty) {
                        println(s"[consumer ${id}] buffer empty ,waiting...")
                        buffer.wait()
                    }

                    val x = buffer.dequeue()
                    println(s"[consumer ${id}] consumed ${x} from buffer")

                    buffer.notify()
                }
                Thread.sleep(random.nextInt(500))
            }
        }
    }


    class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
        override def run(): Unit = {
            val random = new Random()
            var i = 0
            while (true) {
                buffer.synchronized {
                    while (buffer.size == capacity) {
                        println(s"[producer ${id}] buffer is full, waiting...")
                        buffer.wait()
                    }

                    println(s"[producer ${id}] producing ${i} into buffer")
                    buffer.enqueue(i)

                    buffer.notify()
                    i += 1

                }
                Thread.sleep(random.nextInt(500))
            }
        }
    }

    def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
        val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
        val capacity = 3
        (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
        (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
    }

    //    multiProdCons(3, 3)


    // notify all

    def testNotifyAll(): Unit = {
        val bell = new Object
        (1 to 10).foreach(i => new Thread(() => {
            bell.synchronized {
                println(s"[thread ${i} waiting ...")
                bell.wait()
                println(s"[thread ${i} hooray!")
            }
        }).start())

        new Thread(() => {
            Thread.sleep(2000)
            println("Announcer Rock's roll!")
            bell.synchronized {
                bell.notifyAll()
                //                bell.notify()
            }
        }).start()
    }

    //    testNotifyAll()
    // dead lock
    case class Friend(name: String) {
        def bow(other: Friend): Unit = {
            this.synchronized {
                println(s"${this} : I am bowing to my friend ${other}")
                other.bow(this)
                println(s"${this} : my friend ${other} has risen")
            }
        }

        def rise(other: Friend): Unit = {
            this.synchronized {
                println(s"${this} : I am rising to my friend ${other}")
            }
        }

        var side = "right"

        def switchSide(): Unit = {
            if (side == "right") side = "left"
            else side = "right"
        }

        def pass(other: Friend): Unit = {
            while (this.side == other.side) {
                println(s"${this}: Oh, but please, ${other}, feel free to pass...")
                switchSide()
                Thread.sleep(1000)
            }
        }


    }

    val sam = Friend("Sam")
    val pierre = Friend("Pierre")
//    new Thread(() => sam.bow(pierre)).start() // sam's lock, then pierre.lock
//    new Thread(() => pierre.bow(sam)).start() // pierre.lock, then sam's lock
    // live lock

        new Thread(() => sam.pass(pierre)).start() // sam's lock, then pierre.lock
        new Thread(() => pierre.pass(sam)).start() // pierre.lock, then sam's lock


}
