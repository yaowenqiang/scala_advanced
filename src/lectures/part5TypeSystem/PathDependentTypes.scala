package lectures.part5TypeSystem

import scala.math.Numeric.IntIsIntegral

object PathDependentTypes extends App {
    class Outer {
        class Inner
        object InnerObject
        type InnerType
        def print(i : Inner) = println(i)
        def printGeneral(i : Outer#Inner) = println(i)

    }
    def aMethod: Int = {
        class HelperClass
//        type HelperType
        type HelperType = String
        42
    }
    // per instance
    val o = new Outer
    val inner = new o.Inner //is a type
    val oo = new Outer
//    val otherInner :oo.Inner = new o.Inner // won't work

    o.print(inner)
    o.printGeneral(inner)
    oo.printGeneral(inner)
//    oo.print(inner)

    // path dependent type

    trait ItemLike {
        type key
    }
    trait Item[K] extends ItemLike {
        type key = K
    }
    trait IntItem extends Item[Int]
    trait StringItem extends Item[String]

//    def get[ItemType <: ItemLike](key: ItemType#key): ItemType
//    def get[ItemType <: ItemLike](key: ItemType#key): ItemType
//    get[IntItem](42)
//    get[StringItem]("Scala")
//    get[IntItem]("Scala")
    // TODO






}
