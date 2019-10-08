package ru.tinkoff.tschema.http4s

import org.http4s.Response

trait CompleteIn[F[_], G[_], -In, Out, A] {
  def completeIn(a: A, in: In): F[Response[G]]
}