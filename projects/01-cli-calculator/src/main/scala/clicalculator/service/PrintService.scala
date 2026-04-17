package clicalculator.service

import zio._
import java.io.IOException

trait PrintService:
    def printLine(value: String): IO[IOException, Unit]

object PrintService:
    def printLine(value: String): ZIO[PrintService, IOException, Unit] =
        ZIO.serviceWithZIO[PrintService](_.printLine(value))

final class PrintServiceLive extends PrintService:
    override def printLine(value: String): IO[IOException, Unit] =
        Console.printLine(value)

object PrintServiceLive:
    val layer: ULayer[PrintService] =
        ZLayer.succeed(new PrintServiceLive)