package clicalculator

import zio._
import java.io.IOException

import clicalculator.domain._
import clicalculator.domain.CalculatorError
import clicalculator.repository._
import clicalculator.service._

object main extends ZIOAppDefault:
    private val appLayers = 
        CalculatorRepositoryLayer.layer >+>
        CalculatorServiceLayer.layer >+>
        PrintServiceLayer.layer

    def loop: ZIO[CalculatorService & PrintService, IOException, Unit] = 
        for
            _ <- PrintService.printLine("")
            _ <- PrintService.printLine("Enter expression:")
            input <- Console.readLine
            
            trimmed = input.trim

            - <- 
                if trimmed.equalsIgnoreCase("exit") then PrintService.printLine("Goodbye!")
                else
                    for
                        _ <- 
                            CalculatorService
                                .evaluate(trimmed)
                                .foldZIO(
                                    error => 
                                        PrintService.printLine(s"Error: ${CalculatorErrorHandler.toMessage(error)}"),
                                    result =>
                                        PrintService.printLine(s"Result: ${result}")
                                )
                        _ <- loop
                    yield ()
        
        yield()
                                

    val program: ZIO[CalculatorService & PrintService, IOException, Unit] =
        for
            operators <- CalculatorService.getSupportedOperators
            _ <- PrintService.printLine("CLI Calculator")
            _ <- PrintService.printLine(s"Supported operators: ${operators.mkString(" ")}")
            _ <- PrintService.printLine("Type expression like: 10 + 5")
            _ <- PrintService.printLine("Type 'exit' to quit" )
            - <- loop
            
        yield ()

    override def run: ZIO[Any, Any, Any] =
        program.provide(appLayers)