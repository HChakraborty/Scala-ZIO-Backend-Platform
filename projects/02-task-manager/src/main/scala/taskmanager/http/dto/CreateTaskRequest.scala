package taskmanager.http.dto

import zio.json._

final case class CreateTaskRequest(
    title: String
)

object CreateTaskRequest:
    given JsonDecoder[CreateTaskRequest] =
        DeriveJsonDecoder.gen[CreateTaskRequest]