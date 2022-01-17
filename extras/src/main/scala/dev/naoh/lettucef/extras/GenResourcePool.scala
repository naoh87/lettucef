package dev.naoh.lettucef.extras

import cats.effect.Deferred
import cats.effect.Ref
import cats.effect.Resource
import cats.effect.kernel.Concurrent
import cats.effect.syntax.all._
import cats.syntax.applicativeError._
import cats.syntax.apply._
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

    def take(state: Ref[F, State[F, A]]): F[Peaked[F, A]] =
      for {
        pop <- state.modify(_.take1)
        p <- pop match {
          case Right(p) => F.pure(p)
          case Left(true) => peak().flatMap {
            case Right(p) => F.pure(p)
            case Left(value) =>
              state.update(_.deactivate1) >> F.raiseError[Peaked[F, A]](value)
          }
          case _ =>
            F.raiseError[Peaked[F, A]](PoolLifecycleViolation)
        }
      } yield p

    def pushBack(state: Ref[F, State[F, A]], p: Peaked[F, A]): F[Unit] =
      state.modify(_.push1(p, config.maxIdle)).flatMap(_.sequence.void)

    Resource
      .make(Ref.of(State.empty[F, A])) { state =>
        Deferred[F, Unit].flatMap(ks =>
          state.modify(_.setFinalizer(ks.complete(()).void)).flatMap(_.sequence) >>
            ks.get >>
            state.get.flatMap(_.release().sequence.void))
      }
      .map { state =>
        Resource.make(take(state))(pushBack(state, _)).map(_.a)
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

    def push1(b: Peaked[F, A], maxQueue: Int): (State[F, A], List[F[Unit]]) = {
      if (finalizer.isDefined) { //for guard leak usage
        (deactivate1, b.release :: (if (active > 1) Nil else finalizer.toList))
      } else {
        if (queue.size < maxQueue) {
          (State(active - 1, queue.appended(b), finalizer), Nil)
        } else {
          (deactivate1, b.release :: Nil)
        }
      }
    }

    def deactivate1: State[F, A] =
      copy(active = active - 1)

    def setFinalizer(fu: F[Unit]): (State[F, A], List[F[Unit]]) =
      (copy(finalizer = Some(fu)), if (active == 0) List(fu) else Nil)

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