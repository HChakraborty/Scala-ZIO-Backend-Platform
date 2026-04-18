package taskmanager.http.dto

import zio.json._

final case class ErrorResponse(
    message: String
)

object ErrorResponse:
    given JsonEncoder[ErrorResponse] =
        DeriveJsonEncoder.gen[ErrorResponse]