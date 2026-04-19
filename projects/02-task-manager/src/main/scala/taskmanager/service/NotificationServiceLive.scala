package taskmanager.service

import zio._
import taskmanager.domain.Task

final class NotificationServiceLive extends NotificationService:

    override def sendTaskCompleted(task: Task): IO[String, Unit] =
        //The below code simulates a fake notification sending.
        //For simplicity, we are simulating using RandomNumberGenerator
        //to simulate succeed or fail.
        //Will move to proper code later.
        for
            random <- Random.nextIntBounded(3)
            _ <-
                if random == 0 then
                    ZIO.fail(s"Failed to send notification for task ${task.id}")
                else
                    Console.printLine(
                        s"Notification sent for completed task: ${task.title}"
                    ).orDie
        yield ()

object NotificationServiceLive:
    val layer: ULayer[NotificationService] =
        ZLayer.succeed(new NotificationServiceLive)