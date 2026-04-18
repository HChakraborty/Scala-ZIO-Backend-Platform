package taskmanager.http.util

import zio.http._
import zio.json._

object HttpUtils:

    def jsonResponse[A: JsonEncoder](value: A, status: Status = Status.Ok): Response =
        Response
            .json(value.toJson)
            .status(status)