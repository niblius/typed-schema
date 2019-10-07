package ru.tinkoff.tschema.http4s

import org.http4s.Response

trait Serve[T, F[_], G[_], In, Out] {
  def process(in: In, k: Out => F[Response[G]]): F[Response[G]]

  def as[U]: Serve[U, F, G, In, Out] = this.asInstanceOf[Serve[U, F, G, In, Out]]
}
