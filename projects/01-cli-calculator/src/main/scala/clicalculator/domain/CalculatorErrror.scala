package clicalculator.domain

sealed trait CalculatorError

case object EmptyInput extends CalculatorError
case object InvalidExpression extends CalculatorError
case object InvalidNumber extends CalculatorError
case object InvalidOperator extends CalculatorError
case object DivisionByZero extends CalculatorError

object CalculatorErrorHandler:
    def toMessage(error: CalculatorError): String =
        error match
            case EmptyInput => "Please enter an expression!"
            case InvalidExpression => "Invalid Expression! Use format like: 10 + 5"
            case InvalidNumber => "One of the values is not a valid number!"
            case InvalidOperator => "Invalid Operators! Supported operators: +, -, *, /"
            case DivisionByZero =>  "Division by zero is not allowed!"
        