package lectures.part5TypeSystem

object SelfTypes extends App {
    // requiring a type to be mixed in

    trait InstrumentList {
        def play(): Unit
    }

    trait Singer {self: InstrumentList => // self type, whoever implement Singer to implement InstrumentList
        // rest of the implementation or API
        def sing() :Unit
    }
//    trait Singer {this: InstrumentList => // self type, whoever implement Singer to implement InstrumentList
//        // rest of the implementation or API
//        def sing() :Unit
//    }
//    trait Singer {this: InstrumentList => // self type, whoever implement Singer to implement InstrumentList
//        def sing() :Unit
//    }

    class LeadSinger extends Singer with InstrumentList {
        override def sing(): Unit = ???

        override def play(): Unit = ???
    }

//    class VocalList extends Singer {
//        override def sing(): Unit = ???
//    }

    val jamesHetfield = new Singer with InstrumentList {
        override def sing(): Unit = ???

        override def play(): Unit = ???
    }

    class Guitarist extends InstrumentList {
        override def play(): Unit = println("Guitar solo")
    }
    val ericCloption = new Guitarist with Singer {
        override def sing(): Unit = ???
    }

    // vs inheritance

    class A
    class B extends A  // b is a

    trait T
    trait S{self: T => } // S requires a T

    // cake pattern  =>  dependency injection


    // DI
    class Component {

    }
    class ComponentA extends Component
    class ComponentB extends Component
    class dependentComponent(val component: Component)

    trait ScalaComponent {
        def action(a: Int) : String

    }
    trait ScalaDependentComponent {self :ScalaComponent=>
        def dependentAction(x: Int) :String = action(x) + "this rocks"
    }

    trait ScalaApplication {self :ScalaDependentComponent=>
    }

    // layer 1  small components
    trait Picture extends ScalaComponent
    trait Stats extends ScalaComponent

    // layer 2  compose

    trait Profile extends ScalaDependentComponent with Picture
    trait Analytics extends ScalaDependentComponent with Stats

    // layer 3 app

    trait AnalyticsApp extends ScalaApplication with Analytics


    // cyclical dependency

    class x
//    class Y extends X
    trait X {self: Y => }
    trait Y {self: X => }
}
