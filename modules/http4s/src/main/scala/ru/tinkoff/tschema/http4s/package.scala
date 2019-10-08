package ru.tinkoff.tschema

import cats.data.{EitherT, Kleisli, OptionT}

package object http4s {
  type KleisliRouting[G[_], A] = Kleisli[EitherT[G, Rejection, *], Routing[G], A]
}
