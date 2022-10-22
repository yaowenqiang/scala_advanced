package lectures.part5TypeSystem

object Reflection extends App {
    // reflection  macros => quasiquotes  => metaprogramming
    case class Person(name: String) {
        def sayMyName(): Unit = println(s"Hi, my name is $name")
    }
    // 0 - import

    import scala.reflect.runtime.{universe => ru}

    // 1 - mirror
    val m = ru.runtimeMirror(getClass.getClassLoader)
    // 2 - create a class object = "description"
    val clazz = m.staticClass("lectures.part5TypeSystem.Reflection.Person") // create a class object by name
    // 3 create a reflected mirror "can do things"
    val cm = m.reflectClass(clazz)

    // 4 get the constructor
    val constructor = clazz.primaryConstructor.asMethod
    // 5 - reflect the constructor
    val constructorMirror = cm.reflectConstructor(constructor)
    // 6 - invoke the constructor
    val instance = constructorMirror.apply("John")
    println(instance)

    val p = Person("Marry") // from the wire as a serialized object
    // method name computed from somewhere else
    val methodName = "sayMyName"

    // 1 - mirror
    // 2 - reflect the instance
    val reflected = m.reflect(p)
    // 3 - method symbol
    val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
    // 4 - reflect the method = can do things
    val method = reflected.reflectMethod(methodSymbol)
    // 5 - invoke the method
    method.apply()

    // type erasure

    // pp #1: differentiate types at runtime

    val numbers = List(1, 2, 3)
    numbers match {
        case listOfStrings: List[String] => println("list of strings")
        case listOfNumbers: List[Int] => println("list of numbers")
        //  case listOfStrings: List => println("list of strings")
        //  case listOfNumbers: List => println("list of numbers")
    }

    // pp #1: limitation on overloads
    // def processList(list: List[Int]) :Int = 42
    // def processList(list: List[String]) :String = "42"

    // TypeTags

    // 0 - import

    import ru._

    // 1 - create a type tag manually
    val tTag = typeTag[Person]
    println(tTag.tpe)

    class MyMap[K, V]

    // 2 - pass type tags as implicit parameters
    def getTypeArguments[T](value: T)(implicit typeTag: TypeTag[T]) = typeTag.tpe match {
        case TypeRef(_, _, typeArguments) => typeArguments
        case _ => List()
    }

    val myMap = new MyMap[Int, String]
    val typeArgs = getTypeArguments(myMap) // typeTag: TypeTag[MyMap[Int, String]]
    println(typeArgs)

    def isSubtype[A, B](implicit tTagA:TypeTag[A], tTagB: TypeTag[B]) :Boolean = {
        tTagA.tpe <:< tTagB.tpe
    }

    class Animal
    class Dog extends Animal
    println(isSubtype[Dog, Animal])
    // 3 - method symbol
    val anotherMethodSymbol = typeTag[Person].tpe.decl(ru.TermName(methodName)).asMethod
    // 4 - reflect the method = can do things
    val anotherMethod = reflected.reflectMethod(anotherMethodSymbol)
    // 5 - invoke the method
    anotherMethod.apply()
}
