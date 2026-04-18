package taskmanager.http.mapper

import zio.http._
import zio.json._
import taskmanager.domain._
import taskmanager.http.dto.ErrorResponse
import taskmanager.http.util.HttpUtils

object HttpErrorMapper:

    def toResponse(error: TaskError): Response =
        error match
            case EmptyTitle | InvalidTaskId |InvalidCommand =>
                HttpUtils.jsonResponse(
                    ErrorResponse(TaskError.toMessage(error)), 
                    Status.BadRequest
                )

            case TaskNotFound =>
                HttpUtils.jsonResponse(
                    ErrorResponse(TaskError.toMessage(error)), 
                    Status.NotFound
                )
        