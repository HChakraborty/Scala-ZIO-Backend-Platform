package clitaskmanager.service

import zio._
import clitaskmanager.domain._
import clitaskmanager.domain.Task
import java.io.IOException


sealed trait PrintService:
    def printLine(value: String): IO[IOException, Unit]
    def printTask(tasks: List[Task]): IO[IOException, Unit]

object PrintService:
    def printLine(value: String): ZIO[PrintService, IOException, Unit] =
        ZIO.serviceWithZIO[PrintService](_.printLine(value))

    def printTask(tasks: List[Task]): ZIO[PrintService, IOException, Unit] =
        ZIO.serviceWithZIO[PrintService](_.printTask(tasks))

final class PrintServiceLive extends PrintService:
    override def printLine(value: String): IO[IOException, Unit] =
        Console.printLine(value)

    override def printTask(tasks: List[Task]): IO[IOException, Unit] =
        if tasks.isEmpty then Console.printLine(TaskError.toMessage(TasksNotFound))
        else
            ZIO.foreachDiscard(tasks) { task =>
                val statusText = TaskStatus.toDisplayStatus(task.status)
                Console.printLine(s"[${task.id}] ${task.title}: ${task.status}")
            }

object PrintServiceLayer:
    val layer: ULayer[PrintService] =
        ZLayer.succeed(new PrintServiceLive)
