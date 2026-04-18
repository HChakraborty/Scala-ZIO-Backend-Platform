package taskmanager.domain

sealed trait TaskStatus

case object Pending extends TaskStatus
case object Completed extends TaskStatus

object TaskStatus:
    def toDisplayStatus(task: TaskStatus): String =
        task match
            case Pending => "Pending"
            case Completed => "Completed"
        