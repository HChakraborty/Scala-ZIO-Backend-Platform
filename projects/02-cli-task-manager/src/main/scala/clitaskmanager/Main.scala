package clitaskmanager

import zio._
import clitaskmanager.domain.Task
import clitaskmanager.domain._
import clitaskmanager.repository._
import clitaskmanager.service._
import java.io.IOException

object Main extends ZIOAppDefault:
    
    private val appLayers = TaskRepositoryLayer.layer >+>
                            TaskServiceLayer.layer >+>
                            PrintServiceLayer.layer

    val program: ZIO[TaskService & PrintService, IOException, Unit] =
        for
            _ <- ZIO.foreachDiscard(List(
                "CLI Task Manager",
                "Commands:",
                "\tadd <title>",
                "\tlist",
                "\tget <id>",
                "\tdone <id>",
                "\tdelete <id>",
                "\texit"
            ))(PrintService.printLine)
            _ <- loop

        yield ()

    def loop: ZIO[TaskService & PrintService, IOException, Unit] =
        for
            _ <- PrintService.printLine("\nEnterCommand:")
            input <- Console.readLine
            
            trimmed = input.trim
            
            _ <-
                if trimmed.equalsIgnoreCase("exit") then PrintService.printLine("GoodBye.")
                else
                    for
                        _ <- handleCommand(trimmed)
                        _ <- loop
                    yield ()
        yield ()

    def handleCommand(command: String): ZIO[TaskService & PrintService, IOException, Unit] =
        command match
            case "list" =>
                for
                    tasks <- TaskService.getAll
                    _ <- PrintService.printTask(tasks)
                yield ()

            case _ if command.startsWith("get ") =>
                val idText = command.stripPrefix("get ")
                TaskService
                    .getTask(idText)
                    .foldZIO(
                        error => printError(error),
                        task => PrintService.printLine(s"[${task.id}] ${task.title}: ${task.status}")
                    )

            case _ if command.startsWith("add ") =>
                val title = command.stripPrefix("add ")
                TaskService
                    .addTask(title)
                    .foldZIO(
                        error =>
                            printError(error),
                        task =>
                            PrintService.printLine(s"Added Task [${task.id}]: ${task.title}")
                    )
            
            case _ if command.startsWith("done ") =>
                val idText = command.stripPrefix("done ")
                TaskService
                    .completeTask(idText)
                    .foldZIO(
                        error => printError(error),
                        task => PrintService.printLine(s"Task [${task.id}] marked as completed!")
                    )

            case _ if command.startsWith("delete ") =>
                val idText = command.stripPrefix("delete ")
                TaskService
                    .deleteTask(idText)
                    .foldZIO(
                        error => printError(error),
                       _ => PrintService.printLine("Task deleted!")
                    )

            case _ =>
                printError(InvalidCommand) 

    private def printError(error: TaskError): ZIO[PrintService, IOException, Unit] =
        PrintService.printLine(s"Error: ${TaskError.toMessage(error)}")

    override def run: ZIO[Any, Any , Any] =
        program.provide(appLayers)



