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





}
