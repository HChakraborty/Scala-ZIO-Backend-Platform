package taskmanager.repository

import zio._
import taskmanager.domain.{TaskError, Task}

trait TaskRepository:
    def addTask(title: String): UIO[Task]
    def getAllTasks: UIO[List[Task]]
    def getTaskById(id: Int): IO[TaskError, Task]
    def updateTaskById(id: Int): IO[TaskError, Task]
    def deleteTaskById(id: Int): IO[TaskError, Unit]

object TaskRepository:
    def addTask(title: String): ZIO[TaskRepository, Nothing, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.addTask(title))

    def getAllTasks: ZIO[TaskRepository, Nothing, List[Task]] =
        ZIO.serviceWithZIO[TaskRepository](_.getAllTasks)

    def getTaskById(id: Int): ZIO[TaskRepository, TaskError, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.getTaskById(id))

    def updateTaskById(id: Int): ZIO[TaskRepository, TaskError, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.updateTaskById(id))

    def deleteTaskById(id: Int): ZIO[TaskRepository, TaskError, Unit] =
        ZIO.serviceWithZIO[TaskRepository](_.deleteTaskById(id))

