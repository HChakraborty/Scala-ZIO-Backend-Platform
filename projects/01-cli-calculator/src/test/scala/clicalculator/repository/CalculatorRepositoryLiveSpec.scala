package clicalculator.repository

import zio._
import zio.test._
import clicalculator.domain.Operators

object CalculatorRepositoryLiveSpec extends ZIOSpecDefault:

  override def spec =
    suite("CalculatorRepositoryLiveSpec")(
      test("getSupportedOperators should return all supported operators") {
        for
          repo <- ZIO.service[CalculatorRepository]
          operators <- repo.getSupportedOperators
        yield assertTrue(
          operators == Operators.symbols
        )
      },

      test("isOperatorSupported should return true for supported operator") {
        for
          repo <- ZIO.service[CalculatorRepository]
          result <- repo.isOperatorSupported('+')
        yield assertTrue(
          result
        )
      },

      test("isOperatorSupported should return false for unsupported operator") {
        for
          repo <- ZIO.service[CalculatorRepository]
          result <- repo.isOperatorSupported('%')
        yield assertTrue(
          !result
        )
      }
    ).provideLayer(CalculatorRepositoryLive.layer)