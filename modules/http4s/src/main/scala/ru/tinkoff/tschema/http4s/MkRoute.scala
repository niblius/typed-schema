package ru.tinkoff.tschema.http4s

import cats.data.{Kleisli, OptionT}
import cats.syntax.semigroupk._
import cats.{FlatMap, Monad, SemigroupK}
import org.http4s.{HttpRoutes, Request, Response}
import shapeless.HList
import ru.tinkoff.tschema.macros.MakerMacro
import ru.tinkoff.tschema.typeDSL.DSLDef

object MkService {
  // G is internal effect type
  def apply[G[_]] = new MkApply[G]

  class MkApply[G[_]] {
    type F[A] = Kleisli[OptionT[G, *], Routing[G], A]

    def apply[Def <: DSLDef, Impl](definition: => Def)(impl: Impl): F[Response[G]] =
      macro MakerMacro
        .makeRouteHNil[F, MacroInterface[G], Def, Impl, F[Response[G]]]

    def of[Def <: DSLDef, Impl, In <: HList](definition: => Def)(impl: Impl)(input: In): F[Response[G]] =
      macro MakerMacro
        .makeRoute[F, MacroInterface[G], Def, Impl, F[Response[G]], In]
  }

  class MacroInterface[G[_]] {
    def makeResult[F[_], Out]: ResultPA1[F, Out] = new ResultPA1[F, Out]

    def concatResults[F[_]: SemigroupK](x: F[Response[G]], y: F[Response[G]]): F[Response[G]] =
      x <+> y

    def serve[F[_], T] = new ServePA[F, T]

    def route[Res](res: => Res) = new RoutePA[Res](res)

    class ResultPA1[F[_], Out] {
      def apply[In <: HList, Impl](in: In)(impl: Impl)(key: String, groups: String*): F[Response[G]] =
        macro MakerMacro.makeResult[F, In, Out, Impl, F[Response[G]]]
    }

    class RoutePA[Res](res: => Res) {
      def apply[F[_]: Routed: Monad, In, Out](in: In) //(implicit complete: CompleteIn[F, In, Out, Res])
        : F[Response[G]] =
        ???
      //Routed.checkPathEnd(complete.completeIn(res, in))
    }

    class ServePA[F[_], T] {
      def apply[In, Out](in: In)(implicit serve: Serve[T, F, G, In, Out], F: FlatMap[F]) =
        new ServePA2[F, T, In, Out](in)(serve)
    }

    class ServePA2[F[_]: FlatMap, T, In, Out](val in: In)(srv: Serve[T, F, G, In, Out]) {
      def apply(f: Out => F[Response[G]]): F[Response[G]] = srv.process(in, f)
    }
  }
}
