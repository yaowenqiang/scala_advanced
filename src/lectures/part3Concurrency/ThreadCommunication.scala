package lectures.part3Concurrency

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

    smartProducerConsumer()


}
