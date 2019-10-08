package ru.tinkoff.tschema.http4s

import cats.data.{Kleisli, OptionT}
import cats.syntax.semigroupk._
import cats.{FlatMap, Monad, SemigroupK}
import org.http4s.{EntityBody, EntityEncoder, HttpRoutes, Request, Response}
import shapeless.HList
import ru.tinkoff.tschema.macros.MakerMacro
import ru.tinkoff.tschema.typeDSL.DSLDef

object MkRoute {
  // G is the internal effect type
  def apply[G[_]] = new MkApply[G]

  class MkApply[G[_]] {
    type K[A] = KleisliRouting[G, A]
    def apply[Def <: DSLDef, Impl](definition: => Def)(impl: Impl): K[Response[G]] =
      macro MakerMacro
        .makeRouteHNil[G, MacroInterface.type, Def, Impl, K[Response[G]]]

    def of[Def <: DSLDef, Impl, In <: HList](definition: => Def)(impl: Impl)(input: In): K[Response[G]] =
      macro MakerMacro
        .makeRoute[G, MacroInterface.type, Def, Impl, K[Response[G]], In]
  }

  object MacroInterface {
    def makeResult[G[_], Out]: ResultPA1[G, Out] = new ResultPA1[G, Out]

    def concatResults[G[_]](x: KleisliRouting[G, Response[G]], y: KleisliRouting[G, Response[G]])(
        implicit K: SemigroupK[KleisliRouting[G, *]]): KleisliRouting[G, Response[G]] =
      x <+> y

    def serve[G[_], T] = new ServePA[G, T]

    def route[Res](res: => Res) = new RoutePA[Res](res)

    class ResultPA1[G[_], Out] {
      type K[A] = KleisliRouting[G, A]
      def apply[In <: HList, Impl](in: In)(impl: Impl)(key: String, groups: String*): K[Response[G]] =
        macro MakerMacro.makeResult[G, In, Out, Impl, K[Response[G]]]
    }

    // TODO: custom complete
    class RoutePA[Res](res: => Res) {
      def apply[G[_], In, Out](in: In)(implicit enc: EntityEncoder[G, Res], K: Monad[KleisliRouting[G, *]])
      //(implicit complete: CompleteIn[F, G, In, Out, Res], routed: Routed[F, G])
        : KleisliRouting[G, Response[G]] =
        K.pure(Response.apply(body = enc.toEntity(res).body))
      //Routed.checkPathEnd(complete.completeIn(res, in))
    }

    class ServePA[G[_], T] {
      def apply[In, Out](in: In)(implicit serve: Serve[T, G, In, Out], G: FlatMap[G]) =
        new ServePA2[G, T, In, Out](in)(serve)
    }

    class ServePA2[G[_], T, In, Out](val in: In)(srv: Serve[T, G, In, Out]) {
      def apply(f: Out => KleisliRouting[G, Response[G]]): KleisliRouting[G, Response[G]] = srv.process(in, f)
    }
  }
}
