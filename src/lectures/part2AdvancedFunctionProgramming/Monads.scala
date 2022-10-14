package lectures.part2AdvancedFunctionProgramming

object Monads extends App {
    // TODO learn haskell's monad first
    // our own Try Monad
    trait Attempt[+A] {
        def flatMap[B](f: A => Attempt[B]) : Attempt[B]
    }
    object Attempt {
        def apply[A](a: => A) : Attempt[A] =
            try {
                Success(a)
            } catch {
                case e: Throwable => Fail(e)
            }
    }

    case class Success[+A](value: A) extends Attempt[A] {
        def flatMap[B](f: A => Attempt[B]): Attempt[B] =
            try {
                f(value)
            } catch {
                case e: Throwable => Fail(e)
            }
    }
    case class Fail(a: Throwable) extends Attempt[Nothing] {
        def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
    }


    /*
        left-identity
        unit.flatMap(f) = f(x)
        Attempt(x).flatMap(x) = f(x) // Success Case
        Attempt(x).flatMap(f) = f(x) // proof.

        right identity
        attempt.flatMap(unit) = attempt
        Success(x).flatMap(x => Attempt(x)) = attempt(x) => success(x)
        Fail(_).flatMap(...) = Fail(x)
        associativity
        attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))

        Fail(x).flatMap(f).flatMap(g) = Fail(x)
        Fail(x).flatMap(x => f(x).flatMap(g)) = fail(x)

        Success(x).flatMap(f).flatMap(g) =
            f(x).flatMap(g) or Fail(x)
            Success(v).flatMap(v => f(v).flatMap(g)) =
            f(v).flatMap(g) OR Fail(v)
     */

    val attempt = Attempt {
        throw new RuntimeException("My own Monad, yes!")
    }
    println(attempt)
    // a lazy monad

    class Lazy[+A](value: => A) {
//        def flatMap[B](f: A => Lazy[B]) : Lazy[B] = f(value)
        // call by need
        private lazy val internalValue = value
        def use = internalValue
        def flatMap[B](f: (=> A) => Lazy[B]) : Lazy[B] = f(internalValue)
    }
    object Lazy {
        def apply[A](value: => A) :Lazy[A] = new Lazy(value)
    }

    val lazyInstance = Lazy {
        println("today i don't feel like doing anything")
        42
    }

//    println(lazyInstance.use)
    val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
        10 * x
    })

    val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
        10 * x
    })
    flatMappedInstance.use
    flatMappedInstance2.use

    /*
    left-identity
    unit.flatMap(f) = f(v)
    Lazy(v).flatMap(f) = f(v)
     */

    /*

    right-identity
    l.flatMap(unit) = l
    Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)
     */

    /*
        associativity l.flatMap(f).flatMap(g) = l.flatMap(x =>f(x).flatMap(g))
        Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
        Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
     */

    /*
    map and flatten in terms of flatmap

    Monad[T] {
        def flatMap[B](f: T => Monad[b]): Monad[B] = ...(implemented)
        def map[B](f: T => B) : Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
        def flatten(m: Monad[Monad[T]]: Monad[T] = m.flatMap((x: Monad[T])) => x)
        List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
        List(List(1,3), List(3,4)).flatten = List(List(1,3), List(3,4)).flatten.flatMap(x => x) = List(1,2,3,4)
    }
     */
}
