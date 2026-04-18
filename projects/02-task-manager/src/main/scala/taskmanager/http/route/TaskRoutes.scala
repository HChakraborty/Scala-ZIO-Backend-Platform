package taskmanager.http.route

import zio.http._
import taskmanager.http.handler.TaskHttpHandlers

object TaskRoutes:

    val routes =
        Routes(
            Method.GET / Root / "api" / "health" -> handler {
                TaskHttpHandlers.healthCheck
            },

            Method.GET / Root / "api" / "tasks" -> handler {
                TaskHttpHandlers.getAllTasks
            },

            Method.GET / Root / "api" / "tasks" / string("id") -> handler { (idText: String, _: Request) =>
                TaskHttpHandlers.getTaskById(idText)
            },
            
            Method.POST / Root / "api" / "tasks" -> handler { (request: Request) =>
                TaskHttpHandlers.addTask(request)
            },

            Method.PATCH / Root / "api" / "tasks" / string("id") -> handler { (idText: String, _: Request) =>
                TaskHttpHandlers.updateTaskById(idText)
            },

            Method.DELETE / Root / "api" / "tasks" / string("id") -> handler { (idText: String, _: Request) =>
                TaskHttpHandlers.deleteTaskById(idText)
            },
        )