package exercises

import scala.annotation.tailrec

abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B) : MyStream[B] // prepend operator
    def ++[B >: A](anotherStream: => MyStream[B]) : MyStream[B] // concatenate two streams
    def foreach(f :A => Unit) : Unit
    def map[B](f: A => B ) : MyStream[B]
    def flatMap[B](f: A => MyStream[B]) : MyStream[B]
    def filter(predicate: A => Boolean) : MyStream[A]
    def take(n: Int) :MyStream[A] // take the first n elements out of this stream
    @tailrec
    final def toList[B >: A](acc: List[B]=Nil) :List[B] =
        if (isEmpty) acc.reverse
        else tail.toList(head :: acc)

    def takeAsList(n: Int) :List[A] = take(n).toList()
}

object EmptyStream extends MyStream[Nothing] {
    def isEmpty: Boolean = true
    def head :Nothing = throw new NoSuchElementException()
    def tail: MyStream[Nothing] = throw new NoSuchElementException()

    def #::[B >: Nothing](element: B) : MyStream[B]  = new Cons(element, this)
    def ++[B >: Nothing](anotherStream: => MyStream[B]) = anotherStream
    def foreach(f: Nothing => Unit) : Unit = ()
    def map[B](f: Nothing => B ) : MyStream[B]  = this
    def flatMap[B](f: Nothing => MyStream[B]) : MyStream[B]  = this
    def filter(predicate: Nothing => Boolean) : MyStream[Nothing] = this
    def take(n: Int) :MyStream[Nothing] = this

}
class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
    def isEmpty: Boolean = false
    override val head :A = hd
    override lazy val tail: MyStream[A] = tl

    def #::[B >: A](element: B) : MyStream[B]  = new Cons(element, this)
    def ++[B >: A](anotherStream: => MyStream[B]) = new Cons(head, tail ++ anotherStream)
    def foreach(f: A => Unit) : Unit = {
        f(head)
        tail.foreach(f)
    }
    def map[B](f: A => B ) : MyStream[B]  = new Cons(f(head), tail.map(f))
    def flatMap[B](f: A => MyStream[B]) : MyStream[B]  = f(head) ++ tail.flatMap(f)
    def filter(predicate: A => Boolean) : MyStream[A] = {
        if (predicate(head)) new Cons(head, tail.filter(predicate))
        else tail.filter(predicate)
    }
    def take(n: Int) :MyStream[A] = {
        if (n <= 0) EmptyStream
        else if (n == 1) new Cons(head, EmptyStream)
        else new Cons(head, tail.take(n-1))
    }

}
object MyStream {
    def from[A](start :A)(generator: A => A) : MyStream[A] =
        new Cons(start, MyStream.from(generator(start))(generator))
}
object StreamPlayground extends App {
    val natures = MyStream.from(1)(_ + 1)
    println(natures.head)
    println(natures.tail.head)
    println(natures.tail.tail.head)

    val startFrom0 = 0 #:: natures
    println(startFrom0.head)
    println(startFrom0.take(100).foreach(println))

    println(startFrom0.map(_ * 2).take(100).toList())
    println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
    println(startFrom0.filter(_ < 10).take(10).toList())
//    println(startFrom0.filter(_ < 10).take(11).toList())  will crash
    println(startFrom0.filter(_ < 10).take(10).take(20).toList())

    // TODO
   def fibonacci(first :BigInt, second: BigInt) : MyStream[BigInt]  =
       new Cons(first, fibonacci(second, first + second))

    println(fibonacci(1,1).take(100).toList())

    //eratoothenes sieve

    def eratosthenes(nnumbers: MyStream[Int]) : MyStream[Int]  =
        if (nnumbers.isEmpty) nnumbers
        else new Cons(nnumbers.head, eratosthenes(nnumbers.tail.filter(_ % nnumbers.head != 0)))

    println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}
