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
        val consumer =  new Thread(() => {
            println("consumer waiting")
            while(container.isEmpty) {
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


    def smartProducerConsumer() : Unit = {
        val container = new SimpleContainer

        val consumer = new Thread(() => {
            println("[consumer] waiting")
            container.synchronized{
                container.wait()
            }

            // container must have some value
            print(s"[consumer] I have consumed ${container.get}")
        })

        val producer = new Thread(() => {
            print(s"[producer] hard at work...")
            Thread.sleep(2000)
            val value = 42
            container.synchronized{
                println(s"[producr] I'm producing ${value}")
                container.set(value)
                container.notify()
            }
        })

        consumer.start()
        producer.start()
    }

//    smartProducerConsumer()


    def prodConsLargeBuffer() : Unit = {
        val buffer : mutable.Queue[Int] = new mutable.Queue[Int]
        val capacity = 3

        val consumer = new Thread(() => {
            val random = new Random()
            while(true) {
               buffer.synchronized {
                   if(buffer.isEmpty) {
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
            while(true) {
                buffer.synchronized {
                    if(buffer.size == capacity) {
                        println("[consumer] buffer is full, waiting...")
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

    prodConsLargeBuffer()

}
