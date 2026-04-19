package taskmanager.domain

sealed trait TaskEvent

final case class TaskCreated(task: Task) extends TaskEvent
final case class TaskCompleted(task: Task) extends TaskEvent
final case class TaskDeleted(id: Int) extends TaskEvent