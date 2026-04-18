package taskmanager.domain

final case class Task(
    id: Int,
    title: String,
    status: TaskStatus
)