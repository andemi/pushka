package pushka.annotation

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros
import scala.language.postfixOps

class valueMatcher(val key: String, val value: String) extends StaticAnnotation
