package clitaskmanager.repository

import zio._
import clitaskmanager.domain._
import clitaskmanager.domain.Task

sealed trait TaskRepository:
    def create(title: String): UIO[Task]
    def getAll: UIO[List[Task]]
    def getById(id: Int): IO[TaskError, Task]
    def markDone(id: Int): IO[TaskError, Task]
    def delete(id: Int): IO[TaskError, Unit]

object TaskRepository:
    def create(title: String): ZIO[TaskRepository, Nothing, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.create(title))

    def getAll: ZIO[TaskRepository, Nothing, List[Task]] =
        ZIO.serviceWithZIO[TaskRepository](_.getAll)

    def getById(id: Int): ZIO[TaskRepository, TaskError, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.getById(id))

    def markDone(id: Int): ZIO[TaskRepository, TaskError, Task] =
        ZIO.serviceWithZIO[TaskRepository](_.markDone(id))

final class TaskRepositoryLive(
    taskRef: Ref[List[Task]],
    nextIdRef: Ref[Int]
) extends TaskRepository:
    
    override def create(title: String): UIO[Task] =
        for
            nextid <- nextIdRef.getAndUpdate(_ + 1)

            newTask = Task(
                id = nextid,
                title = title,
                status = Pending 
            )

            _ <- taskRef.update(task => task :+ newTask)
        yield newTask

    override def getAll: UIO[List[Task]] =
        taskRef.get

    override def getById(id: Int): IO[TaskError, Task] =
        taskRef.get.flatMap { tasks =>
            ZIO.fromEither(findTask(tasks, id))
        }

    override def markDone(id: Int): IO[TaskError, Task] =
        taskRef.modify { tasks =>
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

    override def delete(id: Int): IO[TaskError, Unit] =
        taskRef.modify { tasks => 
            val existTask = tasks.exists(_.id == id)
            val updatedTasks = tasks.filterNot(_.id == id)

            if existTask then
                (Right(()), updatedTasks)
            else 
                (Left(TaskNotFound), updatedTasks)
        }.flatMap{
            case Right(_) => ZIO.unit
            case Left(error) => ZIO.fail(error)
        }

    private def findTask(tasks: List[Task], id: Int): Either[TaskError, Task] =
        tasks.find(_.id == id).toRight(TaskNotFound)

object TaskRepositoryLayer:
    val layer: ULayer[TaskRepository] =
        ZLayer.
            fromZIO(
                for
                    taskRef <- Ref.make(List.empty[Task])
                    nextIdRef <- Ref.make(1)
                yield new TaskRepositoryLive(taskRef, nextIdRef)
            )
        