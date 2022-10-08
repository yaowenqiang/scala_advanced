package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
    def apply(elem: A) : Boolean =
        contains(elem)
    def contains(values: A) : Boolean
    def +(value: A) : MySet[A]
    def ++(anotherMySet : MySet[A]) :MySet[A]
    def map[B](f : A => B): MySet[B]
    def flatmap[B](f: A => MySet[B]) : MySet[B]
    def filter(predicate: A => Boolean) : MySet[A]
    def foreach(f: A => Unit) : Unit
}

class EmptySet[A] extends MySet[A] {
    def contains(values: A) : Boolean = false
    def +(elem: A) : MySet[A]  = new NonEmptySet[A](elem,this)
    def ++(anotherMySet : MySet[A]) :MySet[A] = anotherMySet
    def map[B](f : A => B): MySet[B] = new EmptySet[B]
    def flatmap[B](f: A => MySet[B]) : MySet[B] = new EmptySet[B]
    def filter(predicate: A => Boolean) : MySet[A] = this
    def foreach(f: A => Unit) : Unit = ()
}
class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
    def contains(elem: A) : Boolean =
        elem == head || tail.contains(elem)
    def +(elem: A) : MySet[A] =
        if (this contains elem) this
        else new NonEmptySet[A](elem, this)
    def ++(anotherMySet : MySet[A]) :MySet[A] = {
        // TODO
        tail ++ anotherMySet + head
    }

    def map[B](f : A => B): MySet[B] = (tail map f) + f(head)
    def flatmap[B](f: A => MySet[B]) : MySet[B] = (tail flatmap f) ++ f(head)
    def filter(predicate: A => Boolean) : MySet[A] = {
        val filteredTail = tail filter predicate
        if (predicate(head)) filteredTail + head
        else filteredTail
    }
    def foreach(f: A => Unit) : Unit = {
        f(head)
        tail foreach f
    }
}
object MySet {
    def apply[A](values: A*) :MySet[A] = {
        @tailrec
        def buildSet(valueSeq: Seq[A], acc: MySet[A]): MySet[A] =
            if(valueSeq.isEmpty) acc
            else buildSet(valueSeq.tail, acc + valueSeq.head)

        buildSet(values.toSeq, new EmptySet[A])
    }
}

object MySetPlayground extends App {
    val s = MySet(1,2,3)
//    s + 5 foreach println
    s + 5 ++ MySet(-2, -5, 3) foreach println
    s.map(x => x + 10) foreach println
//    s.flatmap(x =>MySet(x, 10 * x)) foreach println
    s.flatmap(x =>MySet(x, 10 * x)) filter(_ % 2 == 0) foreach println
}
