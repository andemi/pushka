import org.scalatest._
import pushka._
import pushka.annotation.pushka

object SealedTraitSpec {

  @pushka sealed trait WithBody {
    def x: Int
  }
  
  object WithBody {
    case object A extends WithBody { val x = 0 }
    case object B extends WithBody { val x = 1 }
  }
  
  @pushka sealed trait User

  object User {
    case object Empty extends User
    case class Name(first: String, last: String) extends User
  }

  @pushka case class Container(user: User, anotherField: Int)
}

class SealedTraitSpec extends FlatSpec with Matchers with TestKit {

  import SealedTraitSpec._

  "ADT based on case class" should "writes by case class rules in property" in {
    val instance = Container(User.Name("John", "Doe"), 10)
    write(instance) should be(
      Ast.Obj(Map(
        "user" → Ast.Obj(Map(
          "name" → Ast.Obj(Map(
            "first" → Ast.Str("John"),
            "last" → Ast.Str("Doe"))
          ))),
        "anotherField" → Ast.Num(10.0)
      ))
    )
  }

  "ADT based on case object" should "writes as simple string" in {
    val instance = Container(User.Empty, 10)
    write(instance) should be(
      Ast.Obj(Map(
        "user" → Ast.Str("empty"),
        "anotherField" → Ast.Num(10.0))
      )
    )
  }
  
  "Sealed trait with body" should "be processed" in {
    write[WithBody](WithBody.A) shouldEqual Ast.Str("a")
  }
}
