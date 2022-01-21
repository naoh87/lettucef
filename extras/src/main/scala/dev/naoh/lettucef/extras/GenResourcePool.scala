package dev.naoh.lettucef.extras

import cats.effect.Deferred
import cats.effect.Ref
import cats.effect.Resource
import cats.effect.kernel.Concurrent
import cats.effect.syntax.all._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.traverse._
import dev.naoh.lettucef.api.extras.ResourcePool
import dev.naoh.lettucef.api.extras.ResourcePool.PoolLifecycleViolation
import dev.naoh.lettucef.extras.GenResourcePool.State._
import scala.collection.immutable.Queue


object GenResourcePool {
  def create[F[_], A](config: ResourcePool, source: Resource[F, A])(implicit F: Concurrent[F]): Resource[F, Resource[F, A]] = {
    def peak(): F[Either[Throwable, Peaked[F, A]]] =
      for {
        da <- Deferred[F, Either[Throwable, A]]
        c <- source.use(a => da.complete(Right(a)) >> F.never.void).handleErrorWith(e => da.complete(Left(e)).void).start
        ea <- da.get
      } yield ea.map(Peaked(_, c.cancel))

    Resource
      .make(Ref.of(State.empty[F, A])) { state =>
        Deferred[F, Unit].flatMap(ks =>
          state.modify(_.setFinalizer(ks.complete(()).void)).flatMap(_.sequence) >>
            ks.get >>
            state.get.flatMap(_.release().sequence.void))
      }
      .map { state =>
        Resource.make {
          state.modify(_.take1).flatMap {
            case Right(p) => F.pure(p)
            case Left(true) => peak().flatMap {
              case Right(p) => F.pure(p)
              case Left(err) =>
                state.modify(_.deactivate1).flatMap(_.sequence) >> F.raiseError[Peaked[F, A]](err)
            }
            case _ =>
              F.raiseError[Peaked[F, A]](PoolLifecycleViolation)
          }
        } { p =>
          state.modify(_.push1(p, config.maxIdle)).flatMap {
            case None => F.unit
            case Some(release) => release >> state.modify(_.deactivate1).flatMap(_.sequence).void
          }
        }.map(_.a)
      }
      .evalTap(res => List.fill(config.minIdle.min(config.maxIdle))(res.use(_ => F.unit)).sequence)
  }

  final case class State[F[_], A](
    active: Int,
    queue: SizedQueue[Peaked[F, A]],
    finalizer: Option[F[Unit]]
  ) {
    def take1: (State[F, A], Either[Boolean, Peaked[F, A]]) =
      if (finalizer.isEmpty) {
        queue.dequeueOption match {
          case Some((ba, tail)) => (State(active + 1, tail, finalizer), Right(ba))
          case None =>
            (copy(active = active + 1), Left(true))
        }
      } else {
        (this, Left(false))
      }

    def push1(b: Peaked[F, A], maxQueue: Int): (State[F, A], Option[F[Unit]]) = {
      if (finalizer.isDefined) { //for guard leak usage
        (this, Some(b.release))
      } else if (queue.size < maxQueue) {
        (State(active - 1, queue.appended(b), finalizer), None)
      } else {
        (this, Some(b.release))
      }
    }

    def deactivate1: (State[F, A], Option[F[Unit]]) =
      (
        copy(active = active - 1),
        if (active > 1) None else finalizer
      )

    def setFinalizer(fu: F[Unit]): (State[F, A], Option[F[Unit]]) =
      (copy(finalizer = Some(fu)), if (active == 0) Some(fu) else None)

    def release(): List[F[Unit]] = queue.q.toList.map(_.release)
  }

  object State {
    def empty[F[_], A]: State[F, A] = State(0, SizedQueue.empty, None)

    final class SizedQueue[A](val q: Queue[A], val size: Int) {
      def dequeueOption: Option[(A, SizedQueue[A])] = q.dequeueOption match {
        case Some((head, tail)) => Some((head, new SizedQueue(tail, size - 1)))
        case None => None
      }

      def appended(a: A): SizedQueue[A] =
        new SizedQueue(q.appended(a), size + 1)
    }

    object SizedQueue {
      def empty[A]: SizedQueue[A] = new SizedQueue(Queue.empty, 0)
    }

  }

  private[extras] case class Peaked[F[_], A](a: A, release: F[Unit])

}
