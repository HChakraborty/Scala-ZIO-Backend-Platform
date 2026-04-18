package taskmanager

import zio._
import zio.http._
import taskmanager.http.route.TaskRoutes
import taskmanager.repository.TaskRepositoryLive
import taskmanager.service.TaskServiceLive

object Main extends ZIOAppDefault:

    override def run: ZIO[Any, Any, Any] =
        Server.serve(TaskRoutes.routes)
            .onInterrupt(
                Console.printLine("Server stopped.").orDie
            )
            .provide(
                Server.defaultWithPort(8080),
                TaskRepositoryLive.layer,
                TaskServiceLive.layer
            )