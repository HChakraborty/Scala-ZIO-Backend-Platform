package clicalculator.service

import zio._
import zio.test._
import clicalculator.domain._
import clicalculator.repository.CalculatorRepositoryLive

object CalculatorServiceLiveSpec extends ZIOSpecDefault:

  private val testLayer: ULayer[CalculatorService] =
    CalculatorRepositoryLive.layer >>> CalculatorServiceLayer.layer

  override def spec =
    suite("CalculatorServiceLiveSpec")(
      test("parse should create BinaryOperation for valid input") {
        for
          operation <- CalculatorService.parse("10 + 5")
        yield assertTrue(
          operation == BinaryOperation(BigDecimal(10), '+', BigDecimal(5))
        )
      },

      test("parse should fail for empty input") {
        for
          result <- CalculatorService.parse("   ").either
        yield assertTrue(
          result == Left(EmptyInput)
        )
      },

      test("calculate should fail for division by zero") {
        for
          result <- CalculatorService.calculate(BinaryOperation(10, '/', 0)).either
        yield assertTrue(
          result == Left(DivisionByZero)
        )
      },

      test("evaluate should parse and calculate valid expression") {
        for
          result <- CalculatorService.evaluate("12 * 4")
        yield assertTrue(
          result == BigDecimal(48)
        )
      }
    ).provideLayer(testLayer)