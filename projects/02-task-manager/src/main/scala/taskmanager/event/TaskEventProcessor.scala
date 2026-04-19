package taskmanager.event

import zio._
import taskmanager.domain._
import taskmanager.service.NotificationService

object TaskEventProcessor:
    private def log(message: String): UIO[Unit] =
        Console.printLine(message).orDie

    private def handleEvent(event: TaskEvent): ZIO[NotificationService, Nothing, Unit] =
        event match
            case TaskCreated(task) =>
                log(s"[event] Task created: ${task.id} - ${task.title}")
            
            case TaskDeleted(id) =>
                log(s"[event] Task deleted: ${id}")

            case TaskCompleted(task) => 
                for
                    _ <- log(s"[event] Task completed: ${task.id} - ${task.title}")
                    _ <- NotificationService
                            .sendTaskCompleted(task)
                            .retry(
                                Schedule.exponential(200.millis) && Schedule.recurs(3)
                            )
                            .catchAll( error =>
                                log(s"[event] Notification permanently failed: ${error}")
                            )
                yield ()
    
    def run(queue: Queue[TaskEvent]): ZIO[NotificationService, Nothing, Nothing] =
        queue.take.flatMap(event => handleEvent(event)).forever