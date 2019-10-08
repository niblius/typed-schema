package ru.tinkoff.tschema.http4s

import cats.Monoid

case class Rejection()

object Rejection {
  implicit val rejectionMonoid: Monoid[Rejection] = ???

  val notFound: Rejection = ???
}
