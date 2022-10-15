package lectures.part3Concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object FuturesAndPromises extends App {
    def calculateMeansOfLife(): Int = {
        Thread.sleep(2000)
        42
    }

    val aFuture = Future {
        calculateMeansOfLife() // on run another thread
    } // (global)  passed by the compiler

    println(aFuture.value) // Option[Try[Int]]
    println("waiting for the future.")
    //    aFuture.onComplete(t => t match {
    //        case Success(meansOfLife) => println(s"the means of life is ${meansOfLife}")
    //        case Failure(e) => println(s"I have failed with ${e}")
    //    })
    aFuture.onComplete({
        case Success(meansOfLife) => println(s"the means of life is ${meansOfLife}")
        case Failure(e) => println(s"I have failed with ${e}")
    }) // called by some thread

    Thread.sleep(3000)


    case class Profile(id: String, name: String) {
        def poke(anotherProfile: Profile): Unit =
            println(s"${this.name} poking ${anotherProfile.name}")
    }

    object SocialNetwork {
        // database
        val names = Map(
            "fb.id.1-zack" -> "Mark",
            "fb.id.2-bill" -> "bill",
            "fb.id.3-dummy" -> "Dummy",
        )

        val friends = Map(
            "fb.id.1-zack" -> "fb.id.2-bill",
        )

        val random = new Random()

        // api
        def fetchProfileById(id: String): Future[Profile] = Future {
            Thread.sleep(random.nextInt(3000))
            Profile(id, names(id))
        }

        def fetchBestFriend(profile: Profile): Future[Profile] = Future {
            Thread.sleep(random.nextInt(4000))
            val bfid = friends(profile.id)
            Profile(bfid, names(bfid))
        }
    }

    // client

    //    val mark = SocialNetwork.fetchProfileById("fb.id.1-zuck")
    //    mark.onComplete {
    //        case Success(markProfile) => {
    //            val bill = SocialNetwork.fetchBestFriend(markProfile)
    //            bill.onComplete {
    //                case Success(billProfile) => markProfile.poke(billProfile)
    //                case Failure(e) => e.printStackTrace()
    //            }
    //        }
    //        case Failure(ex) => ex.printStackTrace()
    //    }

    // functional composition of futures
    // map , flatMap ,filter
    //    val nameOfThemAll = mark.map(profile => profile.name)
    //    val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
    //    val zackBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith('z'))

    for {
        mark <- SocialNetwork.fetchProfileById("fb.id.1-zack")
        bill <- SocialNetwork.fetchBestFriend(mark)
    } mark.poke(bill)

    Thread.sleep(10000)

    // fallback

    val aProfile = SocialNetwork.fetchProfileById("unknown id").recover {
        case e: Throwable => Profile("fb.id.0-dummy", "forever along")
    }

    val aFetchedProfile = SocialNetwork.fetchProfileById("unknown id").recoverWith {
        case e: Throwable => SocialNetwork.fetchProfileById("fb.id.1-zack")
    }

    val aFallbackResult = SocialNetwork.fetchProfileById("unknown id").fallbackTo(SocialNetwork.fetchProfileById("fb.id.1-zack"))


}
