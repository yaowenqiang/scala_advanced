package exercises

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}

object Futures extends App {
    //    1) fulfill a future immediately with a value
    def fulfillImmediately[T](value: T): Future[T] = Future(value)

    //    2) inSequence(fa, fb)
    def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
        first.flatMap(_ => second)

    //    1) first(fa, fb) => new future with the first value of the two futures
    def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
        val promise = Promise[A]

        def tryComplete(promise: Promise[A], result: Try[A]) = result match {
            case Success(r) => try {
                promise.success(r)
            } catch {
                case _ =>
            }
            case Failure(t) => try {
                promise.failure(t)
            } catch {
                case _ =>
            }

        }

        //        fa.onComplete {
        //            case Success(r) =>  try {
        //                promise.success(r)
        //            } catch {
        //                case _ =>
        //            }
        //            case Failure(t) =>  try {
        //                promise.failure(t)
        //            } catch {
        //                case _ =>
        //            }
        //        }

        //        fa.onComplete(result => tryComplete(promise,result))
        //        fb.onComplete(result => tryComplete(promise,result))
        //        fa.onComplete(promise.tryComplete(_))
        //        fb.onComplete(promise.tryComplete(_))

        //        fa.onComplete(promise.tryComplete)
        //        fb.onComplete(promise.tryComplete)
        fa.onComplete(tryComplete(promise, _))
        fb.onComplete(tryComplete(promise, _))
        //        fb.onComplete {
        //            case Success(r) =>  try {
        //                promise.success(r)
        //            } catch {
        //                case _ =>
        //            }
        //            case Failure(t) =>  try {
        //                promise.failure(t)
        //            } catch {
        //                case _ =>
        //            }
        //        }

        promise.future
    }

    //    1) last(fa, fb) => last future with the last value
    def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
        // 1 promise witch both futures will try to complete
        // 2 promise witch the last future will complete
        val bothPromise = Promise[A]
        val lastPromise = Promise[A]
        val checkOnComplete = (result: Try[A]) =>
            if (!bothPromise.tryComplete(result))
                lastPromise.complete(result)

        fa.onComplete(checkOnComplete)
        fb.onComplete(checkOnComplete)
        //        fa.onComplete(result => {
        //            if (!bothPromise.tryComplete(result)) {
        //                lastPromise.complete(result)
        //            }
        //        })
        //        fb.onComplete(result => {
        //            if (!bothPromise.tryComplete(result)) {
        //                lastPromise.complete(result)
        //            }
        //        })


        lastPromise.future
    }

    val fast = Future {
        Thread.sleep(1000)
        42
    }

    val slow = Future {
        Thread.sleep(2000)
        45
    }

    //    first(fast, slow).foreach(println)
    //    last(fast, slow).foreach(println)

    first(fast, slow).foreach(f => println(s"first: ${f}"))
    last(fast, slow).foreach(l => println(s" last: ${l}"))

    Thread.sleep(3000)

    //    1) retryUntil[T](action: () => Future[T], condition: T => Boolean) : Future[T]


    def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
        action()
            .filter(condition)
            .recoverWith {
                case _ => retryUntil(action, condition)
            }
    }

    val random = new Random()
    val action = () => Future {
        Thread.sleep(1000)
        val nextValue = random.nextInt(100)
        println(s"Generated ${nextValue}")
        nextValue
    }

    retryUntil(action, (x: Int) => x < 10).foreach(result => println(s"settled at ${result}"))

    Thread.sleep(100000)

}
