package lectures.part2AdvancedFunctionProgramming

//Partially Applied Function
object CurriesAndPAf extends App {
    def superAdder :Int => Int => Int =
        x => y => x + y

    val add3 = superAdder(3)
    println(add3(5))
    println(superAdder(3)(5)) // curried function

    def curriedAdder(x: Int)(y: Int) : Int = x + y
    val add4 :Int => Int = curriedAdder(4)
//        val add4  = curriedAdder(4)
    // lifting or ETA-expansion
    // functions not equals to method
    def inc(x:Int) = x + 1
//    list(1,2,3).map(inc)  = list(1,2,3).map(x => inc(x))
    // partial function applications

    val add5 = curriedAdder(5) _ // do ETA-expansion

    val simpleAddFunction=(x: Int, y:Int) => x + y
    def simpleAddMethod(x:Int, y:Int) = x + y
    def curriedAddMethod(x:Int)(y:Int) = x + y


    val add7 = (x:Int) => simpleAddFunction(x, 7)
    val add7_2 = simpleAddFunction.curried(7)
    val add7_6 = simpleAddFunction(7, _:Int)
    val add7_3 = curriedAddMethod(7) _
    val add7_4 = curriedAddMethod(7)(_) //alternative syntax
    val add7_5 = simpleAddMethod(7, _:Int) //alternative syntax for turning methods into function values
//    as y => simpleAddMethod(7, y)

    // underscore are powerful
    def concatenator(a: String, b:String, c:String) = a + b + c
    val insertName = concatenator("Hello, I'm ", _:String, " ,How are you")
    // x :String => concatenator("Hello, I'm ", x, " ,How are you")
    println(insertName("Jack"))

    val fillingTheBlanks = concatenator("Hello, ", _:String, _:String) //
    // (x, y) => concatenator("Hello, ", x, y)
    println(fillingTheBlanks(" How ", "Are you"))

    def byName(n: Int) = n + 1

    def byFunction(f:() => Int) = f() + 1

    def method :Int = 42
    def parenMethod() :Int = 42

    byName(12)
    byName(method)
    byName(parenMethod())
    byName(parenMethod) // ok, bug beware byName(parenMethod())
//    byName(() => 42) // not ok
    byName((() => 42)()) //ok
//    byName(parenMethod, _) // not ok
//    byFunction(42) // not ok
//    byFunction(method) // not ok compile does not do eta-expansion here
    byFunction(parenMethod) // compile does eta-expansion
    byFunction(() => 42) // ok
    byFunction(parenMethod _) // ok





    def curriedFormatter(s: String)(number: Double) :String = s.format(number)
    val numbers =List(Math.PI, Math.E, 1, 9.8, 1.3E-12)
    val simpleFormat = curriedFormatter("%4.2f") _
    val seriousFormat = curriedFormatter("%8.6f") _
    val preciseFormat = curriedFormatter("%14.12f") _
    println(numbers.map(simpleFormat))
    println(numbers.map(seriousFormat))
    println(numbers.map(preciseFormat))
    println(numbers.map(curriedFormatter("%14.12f"))) // compiler does sweet eta-expansion for use










}
