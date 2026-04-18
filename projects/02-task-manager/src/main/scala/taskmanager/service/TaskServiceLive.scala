package taskmanager.service

import zio._
import taskmanager.domain.Task
import taskmanager.repository.TaskRepository
import taskmanager.domain._

final class TaskServiceLive(
    repository: TaskRepository
) extends TaskService:

    override def addTask(title: String): IO[TaskError, Task] =
        for
            validatedTitle <- validateTitle(title)
            task <- repository.addTask(validatedTitle)
        yield task

    override def getAllTasks: UIO[List[Task]] =
        repository.getAllTasks

    override def getTaskById(id: Int): IO[TaskError, Task] =
        repository.getTaskById(id)

    override def updateTaskById(id: Int): IO[TaskError, Task] =
        repository.updateTaskById(id)

    override def deleteTaskById(id: Int): IO[TaskError, Unit] =
        repository.deleteTaskById(id)

    private def validateTitle(title: String): IO[TaskError, String] =
        val trimmed = title.trim
    
        if trimmed.isEmpty then ZIO.fail(EmptyTitle)
        else ZIO.succeed(trimmed)

object TaskServiceLive:
    val layer: URLayer[TaskRepository, TaskService] =
        ZLayer.fromFunction(new TaskServiceLive(_))