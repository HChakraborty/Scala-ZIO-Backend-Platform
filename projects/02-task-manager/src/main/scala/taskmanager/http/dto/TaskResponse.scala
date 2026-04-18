package taskmanager.http.dto

import zio.json._
import taskmanager.domain.{Task, TaskStatus}

final case class TaskResponse(
    id: Int,
    title: String,
    status: String
)

object TaskResponse:
    given JsonEncoder[TaskResponse] =
        DeriveJsonEncoder.gen[TaskResponse]

    def fromDomain(task: Task): TaskResponse =
        TaskResponse(
            id = task.id,
            title = task.title,
            status = TaskStatus.toDisplayStatus(task.status)
        )