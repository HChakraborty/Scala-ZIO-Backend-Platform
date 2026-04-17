package clitaskmanager.service

import zio._
import clitaskmanager.domain._
import clitaskmanager.domain.Task
import clitaskmanager.repository.TaskRepository

sealed trait TaskService:
    def addTask(title: String): IO[TaskError, Task]
    def getAll: UIO[List[Task]]
    def getTask(idText: String): IO[TaskError, Task]
    def completeTask(idText: String): IO[TaskError, Task]
    def deleteTask(idText: String): IO[TaskError, Unit]

object TaskService:
    def addTask(title: String): ZIO[TaskService, TaskError, Task] =
        ZIO.serviceWithZIO[TaskService](_.addTask(title))

    def getAll: ZIO[TaskService, Nothing, List[Task]] =
        ZIO.serviceWithZIO[TaskService](_.getAll)

    def getTask(idText: String): ZIO[TaskService, TaskError, Task] = 
        ZIO.serviceWithZIO[TaskService](_.getTask(idText))

    def completeTask(idText: String): ZIO[TaskService, TaskError, Task] =
        ZIO.serviceWithZIO[TaskService](_.completeTask(idText)) 
    
    def deleteTask(idText: String): ZIO[TaskService, TaskError, Unit] =
        ZIO.serviceWithZIO[TaskService](_.deleteTask(idText)) 

final class TaskServiceLive(
    repository: TaskRepository
) extends TaskService:

    override def addTask(title: String): IO[TaskError, Task] =
        for
            validatedTitle <- validateTitle(title)
            task <- repository.create(validatedTitle)
        yield task

    override def getAll: UIO[List[Task]] =
        repository.getAll

    override def getTask(idText: String): IO[TaskError, Task] =
        for
            id <- parseTaskId(idText)
            task <- repository.getById(id)
        yield task

    override def completeTask(idText: String): IO[TaskError, Task] =
        for
            id <- parseTaskId(idText)
            task <- repository.markDone(id)
        yield task

    override def deleteTask(idText: String): IO[TaskError, Unit] =
        for
            id <- parseTaskId(idText)
            _ <- repository.delete(id)
        yield ()

    private def validateTitle(title: String): IO[TaskError, String] =
        val trimmed = title.trim
    
        if trimmed.isEmpty then ZIO.fail(EmptyTitle)
        else ZIO.succeed(trimmed)

    private def parseTaskId(idText: String): IO[TaskError, Int] =
        ZIO
            .attempt(idText.trim.toInt)
            .mapError(_ => InvalidTaskId)

object TaskServiceLayer:
    val layer: URLayer[TaskRepository, TaskService] =
        ZLayer.fromFunction(new TaskServiceLive(_))