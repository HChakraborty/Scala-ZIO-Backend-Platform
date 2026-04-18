package taskmanager.service

import zio._
import taskmanager.domain.{TaskError, Task}

trait TaskService:
    def addTask(title: String): IO[TaskError, Task]
    def getAllTasks: UIO[List[Task]]
    def getTaskById(id: Int): IO[TaskError, Task]
    def updateTaskById(id: Int): IO[TaskError, Task]
    def deleteTaskById(id: Int): IO[TaskError, Unit]

object TaskService:
    def addTask(title: String): ZIO[TaskService, TaskError, Task] =
        ZIO.serviceWithZIO[TaskService](_.addTask(title))

    def getAllTasks: ZIO[TaskService, Nothing, List[Task]] =
        ZIO.serviceWithZIO[TaskService](_.getAllTasks)

    def getTaskById(id: Int): ZIO[TaskService, TaskError, Task] = 
        ZIO.serviceWithZIO[TaskService](_.getTaskById(id))

    def updateTaskById(id: Int): ZIO[TaskService, TaskError, Task] =
        ZIO.serviceWithZIO[TaskService](_.updateTaskById(id)) 
    
    def deleteTaskById(id: Int): ZIO[TaskService, TaskError, Unit] =
        ZIO.serviceWithZIO[TaskService](_.deleteTaskById(id)) 

