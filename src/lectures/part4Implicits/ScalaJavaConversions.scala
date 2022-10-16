package lectures.part4Implicits
import java.{util => ju}
object ScalaJavaConversions extends App {
    import collection.JavaConverters._
    val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
    (1 to 10).foreach(javaSet.add)
    println(javaSet)

    val scalaSet = javaSet.asScala

    /*
    Iterator
    Iterable
    ju.List - scala.collection.mutable.Buffer
    ju.Set - scala.collection.mutable.Set
    ju.Map - scala.collection.mutable.Map
     */

    import scala.collection.mutable._

    val numbersBuffer = ArrayBuffer[Int](1,2,3)
    val juNumbersBuffer = numbersBuffer.asJava

    println(juNumbersBuffer.asScala eq numbersBuffer)

    val numbers = List(1,2,3)
    val juNumbers = numbers.asJava
    val backToScala = juNumbers.asScala
    println(numbers eq backToScala) // false

    println(numbers == backToScala) // true

//    juNumbers.add(7)  will crash


    class ToScala[T](value: => T)  {
        def asScala: T = value
    }

    implicit def asScalaOptional[T](o: ju.Optional[T]) : ToScala[Option[T]] =
        new ToScala[Option[T]](
            if(o.isPresent) Some(o.get) else None
        )

    val juOptional :ju.Optional[Int] = ju.Optional.of(8)
    val scalaOption = juOptional.asScala
    println(scalaOption)

}
