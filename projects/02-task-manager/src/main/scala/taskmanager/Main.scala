package taskmanager

import zio._
import zio.http._
import taskmanager.http.route.TaskRoutes
import taskmanager.repository.TaskRepositoryLive
import taskmanager.domain.TaskEvent
import taskmanager.event._
import taskmanager.service._

object Main extends ZIOAppDefault:

    override def run: ZIO[Any, Any, Any] =
        ZIO.scoped {
            for 
                queue <- Queue.unbounded[TaskEvent]

                _ <- TaskEventProcessor
                        .run(queue)
                        .provide(NotificationServiceLive.layer)
                        .forkScoped

                _ <- Console.printLine("Task Manager API running on http://localhost:8080").orDie

                _ <- Server
                        .serve(TaskRoutes.routes)
                        .provide(
                            Server.defaultWithPort(8080),
                            TaskRepositoryLive.layer,
                            ZLayer.succeed(queue),
                            TaskEventPublisherLive.layer,
                            TaskServiceLive.layer,
                        )
            yield ()
        }.onInterrupt(
            Console.printLine("Server stopped.").orDie
        )