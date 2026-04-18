package taskmanager.http.handler

import zio._
import zio.http._
import zio.json._

import taskmanager.domain._
import taskmanager.http.dto._
import taskmanager.http.mapper.HttpErrorMapper
import taskmanager.service.TaskService
import taskmanager.http.util.HttpUtils

object TaskHttpHandlers:
    def healthCheck: Response =
        Response.text("Task Manager API is running!")

    def getAllTasks: ZIO[TaskService, Nothing, Response] =
        TaskService.getAllTasks.map { tasks =>
            HttpUtils.jsonResponse(
                tasks.map(TaskResponse.fromDomain)
            )
        }

    def getTaskById(idText: String): ZIO[TaskService, Nothing, Response] =
        parseId(idText)
            .foldZIO(
                error => 
                    ZIO.succeed(HttpErrorMapper.toResponse(error)),
                id =>
                    TaskService.getTaskById(id)
                        .fold(
                            error => 
                                HttpErrorMapper.toResponse(error),
                            task => 
                                HttpUtils.jsonResponse(TaskResponse.fromDomain(task))
                        )
            )

    def addTask(request: Request): ZIO[TaskService, Nothing, Response] =
        for
            response <- decodeBody[CreateTaskRequest](request).foldZIO(
                errorResponse => 
                    ZIO.succeed(errorResponse),
                createTaskRequest => 
                    TaskService.addTask(createTaskRequest.title).fold(
                        error => HttpErrorMapper.toResponse(error),
                        task =>
                            HttpUtils.jsonResponse(
                                TaskResponse.fromDomain(task),
                                Status.Created
                            )
                    )
            )
        yield response

    def updateTaskById(idText: String): ZIO[TaskService, Nothing, Response] =
        parseId(idText)
            .foldZIO(
                error => ZIO.succeed(HttpErrorMapper.toResponse(error)),
                id => 
                    TaskService.updateTaskById(id)
                        .fold(
                            error => 
                                HttpErrorMapper.toResponse(error),
                            task => 
                                HttpUtils.jsonResponse(TaskResponse.fromDomain(task))
                        )
            )

    def deleteTaskById(idText: String): ZIO[TaskService, Nothing, Response] =
        parseId(idText)
            .foldZIO(
                error => ZIO.succeed(HttpErrorMapper.toResponse(error)),
                id => 
                    TaskService.deleteTaskById(id)
                        .fold(
                            error => 
                                HttpErrorMapper.toResponse(error),
                            _ =>
                                Response.status(Status.NoContent)
                        )
            )

    private def parseId(idText: String): IO[TaskError, Int] =
        ZIO
            .attempt(idText.trim.toInt)
            .mapError(_ => InvalidTaskId)

    private def decodeBody[A: JsonDecoder](request: Request): IO[Response, A] =
        for
            bodyText <- request.body.asString.mapError{ _ =>
                HttpUtils.jsonResponse(
                            ErrorResponse("Failed to read request body!"),
                            Status.BadRequest
                        )
            }
            value <- ZIO.fromEither(
                bodyText.fromJson[A].left.map {_ =>
                    HttpUtils.jsonResponse(
                        ErrorResponse("Invalid JSON request body!"),
                        Status.BadRequest
                    )
                }
            )

        yield value
        