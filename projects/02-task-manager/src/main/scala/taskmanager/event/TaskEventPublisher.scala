package taskmanager.event

import zio._
import taskmanager.domain.TaskEvent

trait TaskEventPublisher:
    def publish(event: TaskEvent): UIO[Unit]

object TaskEventPublisher:
    def publish(event: TaskEvent): ZIO[TaskEventPublisher, Nothing, Unit] =
        ZIO.serviceWithZIO[TaskEventPublisher](_.publish(event))