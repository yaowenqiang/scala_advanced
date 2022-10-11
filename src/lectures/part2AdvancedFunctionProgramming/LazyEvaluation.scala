package lectures.part2AdvancedFunctionProgramming

object LazyEvaluation extends App {
    // lazy delays the evaluation of value
//    lazy val x :Int = throw new RuntimeException()
    lazy val x :Int = {
    println("hello") //only  print once
    42
}
    println(x)
    println(x)

    def sideEffectCondition: Boolean = {
        println("Foo")
        true
    }
    def simpleCondition : Boolean = false

    lazy val lazyCondition = sideEffectCondition
    println(if(simpleCondition && lazyCondition) "yes" else "no")

    // in conjunction with call by name

//    def byNameMethod(n: Int) : Int = n + n + n + 1
//    def byNameMethod(n: => Int) : Int = n + n + n + 1
    def byNameMethod(n: => Int) : Int = {
        lazy val t = n // call by need
        t + t + t + 1
    }
    def retrieveMagicValue :Int = {
        println("Waiting")
        Thread.sleep(2000)
        42
    }
    println(byNameMethod(retrieveMagicValue))
    // lazy vals

    // filtering with lazy vals

    def lessThan30(i: Int) :Boolean = {
        println(s"$i is less then 30?")
        i < 30
    }
    def greaterThan20(i: Int) :Boolean = {
        println(s"$i is greater then 20?")
        i > 20
    }
    val numbers = List(1, 20, 40, 50, 23)
    val lt30 = numbers.filter(lessThan30)
    val gt20 = lt30.filter(greaterThan20)
    println(gt20)

    val lt30Lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
    println("----------")
    lt30Lazy.foreach(println)
    val gt20Lazy = lt30.withFilter(greaterThan20)
    println("----------")
//    println(gt20Lazy)
    gt20Lazy.foreach(println)
    println("##########")
    numbers.withFilter(lessThan30).withFilter(greaterThan20).foreach(println)

    //for comprehensions use withFilter with guards

    for {
        a <- List(1,2,3) if a % 2 == 0
    } yield a + 1
    List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int



}
