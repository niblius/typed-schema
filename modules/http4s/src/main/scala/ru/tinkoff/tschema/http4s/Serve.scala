package ru.tinkoff.tschema.http4s

import org.http4s.Response

trait Serve[T, G[_], In, Out] {
  def process(in: In, k: Out => KleisliRouting[G, Response[G]]): KleisliRouting[G, Response[G]]

  def as[U]: Serve[U, G, In, Out] = this.asInstanceOf[Serve[U, G, In, Out]]
}
