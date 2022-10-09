package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
    def apply(elem: A) : Boolean =
        contains(elem)
    def contains(elem: A) : Boolean
    def +(value: A) : MySet[A]
    def ++(anotherMySet : MySet[A]) :MySet[A] // union
    def map[B](f : A => B): MySet[B]
    def flatmap[B](f: A => MySet[B]) : MySet[B]
    def filter(predicate: A => Boolean) : MySet[A]
    def foreach(f: A => Unit) : Unit
    def -(elem :A) : MySet[A]
    def --(anotherSet :MySet[A]) : MySet[A] // difference
    def &(anotherSet :MySet[A]) : MySet[A] // intersection
    def unary_! : MySet[A]

}

class EmptySet[A] extends MySet[A] {
    def contains(elem: A) : Boolean = false
    def +(elem: A) : MySet[A]  = new NonEmptySet[A](elem,this)
    def ++(anotherMySet : MySet[A]) :MySet[A] = anotherMySet
    def map[B](f : A => B): MySet[B] = new EmptySet[B]
    def flatmap[B](f: A => MySet[B]) : MySet[B] = new EmptySet[B]
    def filter(predicate: A => Boolean) : MySet[A] = this
    def foreach(f: A => Unit) : Unit = ()
    def -(elem :A) : MySet[A] = this
    def --(anotherSet :MySet[A]) : MySet[A] = this
    def &(anotherSet :MySet[A]) : MySet[A] = this
    def unary_! :MySet[A] = new PropertyBasedSet[A](_ => true)
}

//class AllInclusiveSet[A] extends MySet[A] {
//    override def contains(values: A): Boolean = true
//
//    override def +(value: A): MySet[A] = this
//
//    override def ++(anotherMySet: MySet[A]): MySet[A] = this
//
//    override def map[B](f: A => B): MySet[B] =  ???
//
//    override def flatmap[B](f: A => MySet[B]): MySet[B] = ???
//
//    override def filter(predicate: A => Boolean): MySet[A] = ??? // property based set
//
//    override def foreach(f: A => Unit): Unit = ???
//
//    override def -(elem: A): MySet[A] = ???
//
//    override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
//
//    override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//
//    override def unary_! : MySet[A] = new EmptySet[A]
//
//}

// all elements of type A which satisfy a property
// TODO
class PropertyBasedSet[A](property : A => Boolean) extends MySet[A] {
    def contains(elem: A) : Boolean = property(elem)
    def +(elem: A) : MySet[A]  = {
        new PropertyBasedSet[A](x => property(x) || x == elem)
    }

    def ++(anotherMySet : MySet[A]) :MySet[A] =
        new PropertyBasedSet[A](x => property(x) || anotherMySet(x))
    def map[B](f : A => B): MySet[B] = politelyFail
    def flatmap[B](f: A => MySet[B]) : MySet[B] = politelyFail
    def filter(predicate: A => Boolean) : MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))
    def foreach(f: A => Unit) : Unit = politelyFail
    def -(elem :A) : MySet[A] = filter(x => x != elem)
    def --(anotherSet :MySet[A]) : MySet[A] = filter(!anotherSet)
    def &(anotherSet :MySet[A]) : MySet[A] = filter(anotherSet)
    def unary_! :MySet[A] = new PropertyBasedSet[A](x => !property(x))

    def politelyFail = throw new IllegalArgumentException("really deep rabbit hole")
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

    def -(elem :A) : MySet[A] =
        if (head == elem) tail
        else tail - elem + head
//    def --(anotherSet :MySet[A]) : MySet[A] = filter(!anotherSet)
    def --(anotherSet :MySet[A]) : MySet[A] = filter((x => !anotherSet(x)))
//    def &(anotherSet :MySet[A]) : MySet[A] = filter(x => anotherSet.contains(x))
//    def &(anotherSet :MySet[A]) : MySet[A] = filter(x => anotherSet(x))
    def &(anotherSet :MySet[A]) : MySet[A] = filter(anotherSet) // intersection = filtering
    def unary_! :MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))

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

    val aNegative= !s
    println(aNegative(2))
    println(aNegative(5))

    val negativeEven = aNegative.filter(x => x % 2 == 0)
    val negativeEvenFive = negativeEven + 5
    println(negativeEven(5))
    println(negativeEvenFive(5))
}
