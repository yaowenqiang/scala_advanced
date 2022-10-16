package lectures.part4Implicits

import lectures.part4Implicits.JSONSerialization.JSONNumber

import java.util.Date

object JSONSerialization extends App {
    case class User(name: String, age: Int, email: String)

    case class Post(content: String, createAt: Date)

    case class Feed(user: User, posts: List[Post])

    sealed trait JSONValue {
        def stringify: String
    }

    final case class JSONString(value: String) extends JSONValue {
        override def stringify: String = "\"" + value + "\""
    }

    final case class JSONNumber(value: Int) extends JSONValue {
        override def stringify: String = value.toString
    }

    final case class JSONArray(values: List[JSONValue]) extends JSONValue {
        override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
    }

    final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
        override def stringify: String = values.map {
            case (key, value) => "\"" + key + ":" + value.stringify
        }
            .mkString("{", ",", "}")
    }

    trait JSONConverter[T] {
        def convert(value: T) : JSONValue
    }
    implicit  class JSONOps[T](value: T) {
        def toJSON(implicit converter: JSONConverter[T]) : JSONValue =
            converter.convert(value)
    }

    implicit object  StringConverter extends JSONConverter[String] {
        override def convert(value: String): JSONValue = JSONString(value)
    }

    implicit object  NumberConverter extends JSONConverter[Int] {
        override def convert(value: Int): JSONValue = JSONNumber(value)
    }

    implicit object  UserConverter extends JSONConverter[User] {
        override def convert(user: User): JSONObject = JSONObject(Map(
            "name" -> JSONString(user.name),
            "age" -> JSONNumber(user.age),
            "email" -> JSONString(user.email),
        ))
    }

    implicit object  PostConverter extends JSONConverter[Post] {
        override def convert(post: Post): JSONValue = JSONObject(Map(
            "content" -> JSONString(post.content),
            "created" -> JSONString(post.createAt.toString),
        ))
    }

    implicit object  FeedConverter extends JSONConverter[Feed] {
        override def convert(feed: Feed): JSONValue = JSONObject(Map(
//            "user" -> UserConverter.convert(feed.user), // TODO
//            "posts" -> JSONArray(feed.posts.map(PostConverter.convert)) // TODO
                "user" -> feed.user.toJSON,
                "posts" -> JSONArray(feed.posts.map(_.toJSON))
        ))
    }


    val data = JSONObject(Map(
        "user" -> JSONString("Daniel"),
        "posts" -> JSONArray(List(
            JSONString("scala rocks"),
            JSONNumber(10008)
        ))
    ))

//    println(data.stringify)
    val now = new Date(System.currentTimeMillis())
    val john = User("John", 34, "john@rocktjvm.com")
    val feed = Feed(john, List(
        Post("Hello",now),
        Post("World",now),
    ))

    println(feed.toJSON.stringify)

}
