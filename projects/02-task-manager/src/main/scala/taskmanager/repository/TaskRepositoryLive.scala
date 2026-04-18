package taskmanager.repository

import zio._
import taskmanager.domain.Task
import taskmanager.domain._

final class TaskRepositoryLive(
    tasksRef: Ref[List[Task]],
    nextIdRef: Ref[Int]
) extends TaskRepository:
    
    override def addTask(title: String): UIO[Task] =
        for
            nextid <- nextIdRef.getAndUpdate(_ + 1)

            newTask = Task(
                id = nextid,
                title = title,
                status = Pending 
            )

            _ <- tasksRef.update(task => task :+ newTask)
        yield newTask

    override def getAllTasks: UIO[List[Task]] =
        tasksRef.get

    override def getTaskById(id: Int): IO[TaskError, Task] =
        tasksRef.get.flatMap { tasks =>
            ZIO.fromEither(findTask(tasks, id))
        }

    override def updateTaskById(id: Int): IO[TaskError, Task] =
        markTaskDone(id)

    override def deleteTaskById(id: Int): IO[TaskError, Unit] =
        tasksRef.modify { tasks => 
            val existTask = tasks.exists(_.id == id)

            if existTask then
                val updatedTasks = tasks.filterNot(_.id == id)
                (Right(()), updatedTasks)
            else 
                (Left(TaskNotFound), tasks)
        }.flatMap{
            case Right(_) => ZIO.unit
            case Left(error) => ZIO.fail(error)
        }

    private def findTask(tasks: List[Task], id: Int): Either[TaskError, Task] =
        tasks.find(_.id == id).toRight(TaskNotFound)

    private def markTaskDone(id: Int): IO[TaskError, Task] =
        tasksRef.modify { tasks =>
            findTask(tasks, id) match
                case Right(existingTask) =>
                    val updatedTask = existingTask.copy(status = Completed )
                    val updatedTasks = tasks.map { task =>
                        if task.id == id then updatedTask
                        else task
                    }
                    (Right(updatedTask), updatedTasks)
                    
                case _ => 
                    (Left(TaskNotFound), tasks)
        }.flatMap {
            case Right(task) => ZIO.succeed(task)
            case Left(error) => ZIO.fail(error)
        }

object TaskRepositoryLive:
    val layer: ULayer[TaskRepository] =
        ZLayer.
            fromZIO(
                for
                    taskRef <- Ref.make(List.empty[Task])
                    nextIdRef <- Ref.make(1)
                yield new TaskRepositoryLive(taskRef, nextIdRef)
            )
        