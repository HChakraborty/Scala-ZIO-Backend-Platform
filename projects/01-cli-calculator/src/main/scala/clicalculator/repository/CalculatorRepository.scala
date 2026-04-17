package clicalculator.repository

import zio._
import clicalculator.domain.Operators

trait CalculatorRepository:
    def getSupportedOperators: UIO[Set[Char]]
    def isOperatorSupported(operator: Char): UIO[Boolean]

object CalculatorRepository:
    def getSupportedOperators: ZIO[CalculatorRepository, Nothing, Set[Char]] =
        ZIO.serviceWithZIO[CalculatorRepository](_.getSupportedOperators)

    def isOperatorSupported(operator: Char): ZIO[CalculatorRepository, Nothing, Boolean] = 
        ZIO.serviceWithZIO[CalculatorRepository](_.isOperatorSupported(operator))

final class CalculatorRepositoryLive extends CalculatorRepository:

    override def getSupportedOperators: UIO[Set[Char]] =
        ZIO.succeed(Operators.symbols)
        
    override def isOperatorSupported(operator: Char): UIO[Boolean] = 
        ZIO.succeed(Operators.symbols.contains(operator))

object CalculatorRepositoryLive:
    val layer: ULayer[CalculatorRepository] = 
        ZLayer.succeed(new CalculatorRepositoryLive)