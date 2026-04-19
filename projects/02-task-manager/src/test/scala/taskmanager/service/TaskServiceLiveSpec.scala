package taskmanager.service

import zio._
import zio.test._
import taskmanager.domain._
import taskmanager.event.TaskEventPublisher
import taskmanager.repository.TaskRepositoryLive

object TaskServiceLiveSpec extends ZIOSpecDefault:

  private val testEventPublisher: ULayer[TaskEventPublisher] =
    ZLayer.succeed(
      new TaskEventPublisher:
        override def publish(event: TaskEvent): UIO[Unit] =
          ZIO.unit
    )

  private val testLayer: ULayer[TaskService] =
    TaskRepositoryLive.layer ++ testEventPublisher >>> TaskServiceLive.layer

  override def spec =
    suite("TaskServiceLiveSpec")(
      test("addTask should create a task with trimmed title and Pending status") {
        for
          task <- TaskService.addTask("  Learn ZIO  ")
        yield assertTrue(
          task.id == 1,
          task.title == "Learn ZIO",
          task.status == Pending
        )
      },

      test("addTask should fail when title is empty") {
        for
          result <- TaskService.addTask("   ").either
        yield assertTrue(
          result == Left(EmptyTitle)
        )
      },

      test("updateTaskById should mark task as Completed") {
        for
          created <- TaskService.addTask("Write tests")
          updated <- TaskService.updateTaskById(created.id)
        yield assertTrue(
          updated.id == created.id,
          updated.title == "Write tests",
          updated.status == Completed
        )
      },

      test("deleteTaskById should remove the task") {
        for
          created <- TaskService.addTask("Delete me")
          _ <- TaskService.deleteTaskById(created.id)
          result <- TaskService.getTaskById(created.id).either
        yield assertTrue(
          result == Left(TaskNotFound)
        )
      }
    ).provideLayer(testLayer)