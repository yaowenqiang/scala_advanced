package lectures.part3Concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.{ForkJoinTaskSupport, Task, TaskSupport}
import scala.collection.parallel.immutable.ParVector
import scala.collection.parallel.immutable.ParSet;

object ParallelUtils extends App {
    // parallel collections
    //    val parallelList = (List(1,2,3))
    //    val parallelList = List(1,2,3).par
    val parVector = ParVector[Int](1, 2, 3)
    /*
    Seq
    Vector
    Array
    Map - Hash, Trie
    Set - Hash, Trie
     */

    def measure[T](operation: => T): Long = {
        val time = System.currentTimeMillis()
        operation
        System.currentTimeMillis() - time
    }

    val list = (1 to 10000).toList
    val serialTime = measure {
        list.map(_ + 1)
    }
    println(s"serial time : ${serialTime}")
    val parallelTime = measure {
        list.par.map(_ + 1)
    }
    println(s"parallel time : ${parallelTime}")
    /*
     Map-Reduce Model
     - split the elements into chunks - splitter
     - operation
     - recombine - Combiner
     */
    // map, flatmap, filter, foreach,
    // reduce, fold with associative operators (not safe),

    println(List(1, 2, 3).reduce(_ - _))
    println(List(1, 2, 3).par.reduce(_ - _))

    // synchronization
    var sum = 0
    List(1, 2, 3).par.foreach(sum += _) // not guaranteed
    println(sum)

    // configuring

    parVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))

    /*
    alternatives
    ThreadPoolTaskSupport - deprecated
    ExecutionContextTaskSupport(Et)
     */

    //    parVector.tasksupport = new TaskSupport {
    //        override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
    //
    //        override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
    //
    //        override def parallelismLevel: Int = ???
    //
    //        override val environment: AnyRef = _
    //    }

    // atomic ops and reference

    val atomic = new AtomicReference[Int](2)
    val currentValue = atomic.get() // thread-safe read
    atomic.set(4) // thread-safe write
    atomic.getAndSet(5) // thread-safe combo
    // if the value is 10, then set to 20, otherwise do nothing
    // reference equality
    atomic.compareAndSet(10, 20)

    atomic.updateAndGet(_ + 1) // thread-safe function run
    atomic.getAndUpdate(_ + 1)
    atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
    atomic.getAndAccumulate(12, _ + _)


}
