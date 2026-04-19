package taskmanager.event

import zio._
import taskmanager.domain.TaskEvent

final class TaskEventPublisherLive(
    queue: Queue[TaskEvent]
) extends TaskEventPublisher:
    override def publish(event: TaskEvent): UIO[Unit] =
        queue.offer(event).unit

object TaskEventPublisherLive:
    val layer: URLayer[Queue[TaskEvent], TaskEventPublisher] =
        ZLayer.fromFunction(new TaskEventPublisherLive(_))