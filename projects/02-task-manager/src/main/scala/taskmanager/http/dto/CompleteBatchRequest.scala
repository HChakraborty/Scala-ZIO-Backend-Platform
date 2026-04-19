package taskmanager.http.dto

import zio.json._

final case class CompleteBatchRequest(
    ids: List[Int]
)

object CompleteBatchRequest:
    given JsonDecoder[CompleteBatchRequest] =
        DeriveJsonDecoder.gen[CompleteBatchRequest]