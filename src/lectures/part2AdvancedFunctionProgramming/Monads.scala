package lectures.part2AdvancedFunctionProgramming

object Monads extends App {
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
        unit.flatMap(f) = f(x
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
}
