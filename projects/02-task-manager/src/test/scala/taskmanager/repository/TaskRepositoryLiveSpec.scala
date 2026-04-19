package taskmanager.repository

import zio._
import zio.test._
import taskmanager.domain._

object TaskRepositoryLiveSpec extends ZIOSpecDefault:

  override def spec =
    suite("TaskRepositoryLiveSpec")(
      test("addTask should create a task with Pending status") {
        for
          repo <- ZIO.service[TaskRepository]
          task <- repo.addTask("Learn Scala")
        yield assertTrue(
          task.id == 1,
          task.title == "Learn Scala",
          task.status == Pending
        )
      },

      test("getTaskById should return TaskNotFound for missing id") {
        for
          repo <- ZIO.service[TaskRepository]
          result <- repo.getTaskById(999).either
        yield assertTrue(
          result == Left(TaskNotFound)
        )
      },

      test("updateTaskById should mark task as Completed") {
        for
          repo <- ZIO.service[TaskRepository]
          created <- repo.addTask("Test repo")
          updated <- repo.updateTaskById(created.id)
        yield assertTrue(
          updated.id == created.id,
          updated.status == Completed
        )
      },

      test("deleteTaskById should remove the task") {
        for
          repo <- ZIO.service[TaskRepository]
          created <- repo.addTask("Delete repo task")
          _ <- repo.deleteTaskById(created.id)
          result <- repo.getTaskById(created.id).either
        yield assertTrue(
          result == Left(TaskNotFound)
        )
      }
    ).provideLayer(TaskRepositoryLive.layer)