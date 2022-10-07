package lectures.part2AdvancedFunctionProgramming

object PartialFunctions extends App {
    val aFunction = (x :Int) => x + 1  //Function1[Int, INt] == Int => Int
    val aFussyFunction = (x: Int) =>
        if (x == 1) 42
        else if (x == 2) 56
        else if (x == 5) 999
        else throw new FunctionNotApplicableException

    class FunctionNotApplicableException extends RuntimeException

    val aNicerFussyFunction = (x: Int) => x match {
        case 1 => 42
        case 2 =>  56
        case 5 =>  999
//        case _ =>  throw new FunctionNotApplicableException
    }

    val aPartialFunction: PartialFunction[Int, Int] = {
        case 1 => 42
        case 2 =>  56
        case 5 =>  999
    } // partial function value

    println(aPartialFunction(2))
//    println(aPartialFunction(100))
//     partial function utilities

    println(aPartialFunction.isDefinedAt(44))
    val lifted = aPartialFunction.lift // Int => Option[Int]
    println(lifted(2))
    println(lifted(20))

    val aChainPartialFunction = aPartialFunction.orElse[Int, Int] {
        case 45 => 67
    }
    println(aChainPartialFunction(2))
    println(aChainPartialFunction(45))

    // Partial Function extends normal functions

    val aTotalFunction: Int => Int = {
        case 1 => 99
    }

    // High Order Function accept Partial Functions as well

    val aMapList = List(1,2,3).map {
        case 1 => 42
        case 2 => 78
        case 3 => 1000
//        case 5 => 1000
    }
    println(aMapList)

    /*
    Note: Partial Function can only take one parameter type
     */

    val aManualFussyFunction = new PartialFunction[Int, Int] {
        override def apply(x: Int): Int = x match {
            case 1 => 42
            case 2 => 75
            case 5 => 999
        }
        override def isDefinedAt(x :Int): Boolean =
        x == 3 || x == 2 || x == 5
    }

    def chatBot :PartialFunction[String, String] = {
        case "hello" => "Hi, my name is T2000"
        case "goodbye" => "Goodbye, I'll be back"
        case "mission" => "save John connor"
    }

//    scala.io.Source.stdin.getLines().foreach(line=> println(s"You said : $line"))
//    scala.io.Source.stdin.getLines().foreach(line=> println("You said : " + chatBot(line)))
    scala.io.Source.stdin.getLines().map(chatBot).foreach(println)

}
