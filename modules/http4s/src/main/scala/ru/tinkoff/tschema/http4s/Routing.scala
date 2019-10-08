package ru.tinkoff.tschema.http4s

import cats.data.Kleisli
import org.http4s.Request

final case class Routing[G[_]](
    request: Request[G],
    matched: Int
)