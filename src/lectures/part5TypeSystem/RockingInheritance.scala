package lectures.part5TypeSystem

object RockingInheritance extends App {
    // convenience
    trait Writer[T] {
        def write(value: T): Unit
    }

    trait Closable {
        def close(status: Int): Unit
    }

    trait GenericStream[T] {
        def foreach(f: T => Unit): Unit
    }

    def processStream[T](stream: GenericStream[T] with Writer[T] with Closable): Unit = {
        stream.foreach(println)
        stream.close(1)
    }

    // diamond problem

    trait Animal {
        def name: String
    }

    trait Lion extends Animal {
        override def name: String = "Lion"
    }

    trait Tiger extends Animal {
        override def name: String = "tiger"
    }

    class Mutant extends Lion with Tiger {
        //        override def name: String = "ALIEN"
    }

    val aMutant = new Mutant
    println(aMutant.name)
    /*
    Mutant extends Animal with { override def name: String = "Lion" }
    with extends Animal with { override def name: String = "Tiger" }
    LAST OVERRIDE PICKEd
     */
    // the super problem = type linearization

    trait Cold {
        def print(): Unit = println("Cold")
    }

    trait Green extends Cold {
        override def print(): Unit = {
            println("Green")
            super.print()
        }
    }

    trait Blue extends Cold {
        override def print(): Unit = {
            println("Blue")
            super.print()
        }
    }

    class Red {
        def print() :Unit = println("Red")
    }

    class White extends Red with Green with Blue {
//    class White extends Red {
        override def print(): Unit = {
            println("White")
            super.print()
        }
    }

    val aColor = new White
    aColor.print()

}
/*
Code = AnyRef with <Code>
Green
     =  Cold with <Green>
     = AnyRef with <Code> with <Green>
Blue
     = Cold with <Blue>
     = AnyRef with <Code> with <Blue>
Red  = AnyRef with <Red>

White = Red with Green with Blue with <White>
      = AnyRef with <Red>
        with (AnyRef with <Code> with <Green>)
        with (AnyRef with <Code> with <Blue>)
        With <White>

      = AnyRef with <Red> with <Cold> with <Green> With <Blue> with <White>
      // Type linearization
      // super will search from <White>'s left
 */
