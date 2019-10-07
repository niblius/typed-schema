package ru.tinkoff.tschema.http4s

import org.http4s.Request

final case class Routing[F[_]](
    request: Request[F],
    matched: Int
)

trait Routed[F[_]]
