package clitaskmanager.domain

sealed trait TaskError

case object EmptyTitle extends TaskError
case object InvalidTaskId extends TaskError
case object TaskNotFound extends TaskError
case object InvalidCommand extends TaskError
case object TasksNotFound extends TaskError

object TaskError:
    def toMessage(error: TaskError): String =
        error match
            case EmptyTitle => "Task title cannot be empty!"
            case InvalidCommand => "Invalid command!"
            case InvalidTaskId => "Task id must be a valid number!"
            case TaskNotFound => "Task not found!"
            case TasksNotFound => "No tasks found!"