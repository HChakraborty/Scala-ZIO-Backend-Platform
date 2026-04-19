package taskmanager.service

import zio._
import taskmanager.domain.Task
import taskmanager.repository.TaskRepository
import taskmanager.domain._
import taskmanager.event.TaskEventPublisher

final class TaskServiceLive(
    repository: TaskRepository,
    eventPublisher: TaskEventPublisher
) extends TaskService:

    override def addTask(title: String): IO[TaskError, Task] =
        for
            validatedTitle <- validateTitle(title)
            task <- repository.addTask(validatedTitle)
            _ <- eventPublisher.publish(TaskCreated(task))
        yield task

    override def getAllTasks: UIO[List[Task]] =
        repository.getAllTasks

    override def getTaskById(id: Int): IO[TaskError, Task] =
        repository.getTaskById(id)

    override def updateTaskById(id: Int): IO[TaskError, Task] =
        for
            task <- repository.updateTaskById(id)
            _ <- eventPublisher.publish(TaskCompleted(task))
        yield task

    override def deleteTaskById(id: Int): IO[TaskError, Unit] =
        for
            _ <- repository.deleteTaskById(id)
            _ <- eventPublisher.publish(TaskDeleted(id))
        yield ()

    override def completeTasksBatch(ids: List[Int]): IO[TaskError, List[Task]] =
        ZIO.foreach(ids)(id => updateTaskById(id))

    private def validateTitle(title: String): IO[TaskError, String] =
        val trimmed = title.trim
    
        if trimmed.isEmpty then ZIO.fail(EmptyTitle)
        else ZIO.succeed(trimmed)

object TaskServiceLive:
    val layer: URLayer[TaskRepository & TaskEventPublisher, TaskService] =
        ZLayer.fromFunction(new TaskServiceLive(_, _))