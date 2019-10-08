package ru.tinkoff.tschema.http4s

import cats.{Applicative, Apply, Foldable, Monad, MonoidK}
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.foldable._
import cats.syntax.functor._
import cats.data.{EitherT, Kleisli}
import org.http4s.Request
import ru.tinkoff.tschema.param.{Param, ParamSource}

import scala.util.matching.Regex

trait Routed[G[_]] {
  def request(implicit G: Applicative[G]): KleisliRouting[G, Request[G]] = Kleisli { r =>
    EitherT.pure[G, Rejection](r.request)
  }

  def path(implicit G: Applicative[G]): KleisliRouting[G, CharSequence] = Kleisli { r =>
    EitherT.pure[G, Rejection](r.request.pathInfo)
  }

  def matched(implicit G: Applicative[G]): KleisliRouting[G, Int] = Kleisli { r =>
    EitherT.pure[G, Rejection](r.matched)
  }

  def withMatched[A](m: Int, fa: KleisliRouting[G, A]): KleisliRouting[G, A] =
    Kleisli.local((r: Routing[G]) => r.copy(matched = m))(fa)

  def reject[A](rejection: Rejection)(implicit G: Applicative[G]): KleisliRouting[G, A] =
    Kleisli.liftF(EitherT.leftT[G, A](rejection))
}

object Routed extends RoutedFunctions {
  def apply[G[_]](implicit instance: Routed[G]): Routed[G] = instance
}

class RoutedFunctions {
  def matched[G[_]: Applicative: Routed]: KleisliRouting[G, Int]       = Routed[G].matched
  def path[G[_]: Applicative: Routed]: KleisliRouting[G, CharSequence] = Routed[G].path
  def withMatched[G[_]: Routed, A](m: Int, fa: KleisliRouting[G, A]): KleisliRouting[G, A] =
    Routed[G].withMatched(m, fa)
  def request[G[_]: Applicative: Routed]: KleisliRouting[G, Request[G]] = Routed[G].request
  def matchedPath[G[_]: Applicative: Routed]: KleisliRouting[G, CharSequence] =
    (path, matched).mapN(_.subSequence(0, _))
  def unmatchedPath[G[_]: Applicative: Routed]: KleisliRouting[G, CharSequence] =
    (path, matched).mapN((p, m) => p.subSequence(m, p.length()))

  def reject[G[_]: Applicative: Routed, A](rejection: Rejection): KleisliRouting[G, A] =
    Routed[G].reject(rejection)
  def rejectMany[G[_]: Applicative: Routed, A](rejections: Rejection*): KleisliRouting[G, A] =
    reject(Foldable[List].fold(rejections.toList))

  def addMatched[G[_]: Applicative, A](i: Int, fa: KleisliRouting[G, A])(
      implicit routed: Routed[G]): KleisliRouting[G, A] = matched.flatMap(m => withMatched(m + i, fa))
  def segment[G[_]: Applicative, A](f: Option[CharSequence] => KleisliRouting[G, A])(
      implicit routed: Routed[G]): KleisliRouting[G, A] =
    customSegment(SegmentPattern, 1, f)

  private[this] val SegmentPattern = "/?([^/]*)".r

  def customSegment[G[_]: Applicative: Routed, A](
      pattern: Regex,
      groupId: Int,
      f: Option[CharSequence] => KleisliRouting[G, A]): KleisliRouting[G, A] =
    for {
      path     <- unmatchedPath[G]
      matchOpt = pattern.findPrefixMatchOf(path)
      res      <- matchOpt.fold(f(None))(m => addMatched(m.end - m.start, f(Some(m.group(groupId)))))
    } yield res

  private[this] def commonPathPrefix(path: CharSequence, pref: CharSequence): Int =
    if (pref.length() <= path.length() && path.subSequence(0, pref.length()) == pref) pref.length()
    else if (pref.length() < path.length() && path.charAt(0) == '/' && path.subSequence(1, pref.length() + 1) == pref)
      pref.length() + 1
    else -1

  def checkPrefix[G[_]: Applicative: Routed, A](pref: CharSequence, fa: KleisliRouting[G, A]): KleisliRouting[G, A] =
    for {
      path <- unmatchedPath[G]
      i    = commonPathPrefix(path, pref)
      res  <- if (i < 0) reject[G, A](Rejection.notFound) else if (i > 0) addMatched(i, fa) else fa
    } yield res

  def checkPath[G[_]: Applicative: Routed, A](full: CharSequence, fa: KleisliRouting[G, A]): KleisliRouting[G, A] =
    for {
      path <- unmatchedPath[G]
      i    = commonPathPrefix(path, full)
      res  <- if (i == path.length()) addMatched(i, fa) else reject[G, A](Rejection.notFound)
    } yield res

  def param[F[_]: Routed: Monad, S >: ParamSource.All <: ParamSource, T](name: String)(implicit param: Param[S, T],
                                                                                       p: ParamDirectives[S]): F[T] =
    p.getByName[F, T](name, os => p.provideOrReject[F, T](name, param.get(name, _ => os)))
}
