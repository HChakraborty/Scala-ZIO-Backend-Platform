package clicalculator.service

import zio._
import clicalculator.domain._
import clicalculator.repository.CalculatorRepository

trait CalculatorService:
    def parse(input: String): IO[CalculatorError, BinaryOperation]
    def calculate(operation: BinaryOperation): IO[CalculatorError, BigDecimal]
    def evaluate(input: String): IO[CalculatorError, BigDecimal]
    def getSupportedOperators: UIO[Set[Char]]

object CalculatorService:
    def parse(input: String): ZIO[CalculatorService, CalculatorError, BinaryOperation] =
        ZIO.serviceWithZIO[CalculatorService](_.parse(input))

    def calculate(operation: BinaryOperation): ZIO[CalculatorService, CalculatorError, BigDecimal] =
        ZIO.serviceWithZIO[CalculatorService](_.calculate(operation))

    def evaluate(input: String): ZIO[CalculatorService, CalculatorError, BigDecimal] =
        ZIO.serviceWithZIO[CalculatorService](_.evaluate(input))

    def getSupportedOperators: ZIO[CalculatorService, Nothing, Set[Char]] =
        ZIO.serviceWithZIO[CalculatorService](_.getSupportedOperators)

final class CalculatorServiceLive(
    repository: CalculatorRepository
) extends CalculatorService:
    override def parse(input: String): IO[CalculatorError, BinaryOperation] =
        for
            trimmed <- ZIO.succeed(input.trim)
            _ <- if trimmed.isEmpty then ZIO.fail(EmptyInput)
                else ZIO.unit

            tuple <- extractParts(trimmed)
            (leftRaw, operator, rightRaw) = tuple

            left <- parseNumber(leftRaw)
            right <- parseNumber(rightRaw)
            isSupported <- repository.isOperatorSupported(operator)
            _ <- if isSupported then ZIO.unit
                else ZIO.fail(InvalidOperator)

        yield BinaryOperation(left, operator, right)

    override def calculate(operation: BinaryOperation): IO[CalculatorError, BigDecimal] =
        val left = operation.left
        val right = operation.right

        operation.operator match
            case '+' => ZIO.succeed(left + right)
            case '-' => ZIO.succeed(left - right)
            case '*' => ZIO.succeed(left * right)
            case '/' =>
                if right == 0 then ZIO.fail(DivisionByZero)
                else ZIO.succeed(left / right)
            case _ => ZIO.fail(InvalidOperator)

    override def evaluate(input: String): IO[CalculatorError, BigDecimal] =
        parse(input).flatMap(calculate)

    override def getSupportedOperators: UIO[Set[Char]] =
        repository.supportedOperators

    private def isUnaryMinus(input: String, index: Int): Boolean =
        if input(index) != '-' then false
        else
            val previousNonSpace =
                input.substring(0, index).reverse.dropWhile(_.isWhitespace).headOption

            previousNonSpace match
                case None => 
                    true
                case Some(ch) => 
                    Operators.symbols.contains(ch)

    private def extractParts(input: String): IO[CalculatorError, (String, Char, String)] =
        val operatorIndexes =
            input.zipWithIndex.collect {
                case (ch, index)
                    if Operators.symbols.contains(ch)
                    && index != 0
                    && !isUnaryMinus(input, index) =>
                        index
            }

        operatorIndexes match
            case Seq(index) =>
                val leftRaw = input.substring(0, index).trim
                val operator = input.charAt(index)
                val rightRaw = input.substring(index + 1).trim

                if leftRaw.isEmpty || rightRaw.isEmpty then ZIO.fail(InvalidExpression)
                else ZIO.succeed((leftRaw, operator, rightRaw))

            case _ =>
                ZIO.fail(InvalidExpression)

    private def parseNumber(value: String): IO[CalculatorError, BigDecimal] =
        ZIO.
            attempt(BigDecimal(value))
            .mapError(_ => InvalidNumber)

object CalculatorServiceLayer:
    val layer: URLayer[CalculatorRepository, CalculatorService] =
        ZLayer.fromFunction(new CalculatorServiceLive(_))

        