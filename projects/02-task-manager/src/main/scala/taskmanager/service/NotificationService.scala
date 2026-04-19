package taskmanager.service

import zio._
import taskmanager.domain.Task

trait NotificationService:
    def sendTaskCompleted(task: Task): IO[String, Unit]

object NotificationService:
    def sendTaskCompleted(task: Task): ZIO[NotificationService, String, Unit] =
        ZIO.serviceWithZIO[NotificationService](_.sendTaskCompleted(task))