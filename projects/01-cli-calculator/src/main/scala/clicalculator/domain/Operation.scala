package clicalculator.domain

final case class BinaryOperation(
    left: BigDecimal,
    operator: Char,
    right: BigDecimal
)