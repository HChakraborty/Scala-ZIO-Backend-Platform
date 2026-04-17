# CLI Calculator (ZIO)

A simple CLI-based calculator application built using Scala and ZIO, demonstrating functional programming concepts, typed error handling, and service-oriented design.



## Overview

This project parses and evaluates user input from the command line in the form of a single arithmetic expression. It validates input, applies calculation logic, and outputs results while handling errors in a functional and type-safe way.



## Features

- Reads arithmetic expressions from CLI (e.g., `10 + 5`, `-3 * -4`)
- Supports operators: `+`, `-`, `*`, `/`
- Handles negative numbers (e.g., `-3 * -4`, `3 * -5`)
- Ignores extra whitespace in input
- Validates expression structure and numbers
- Handles errors using typed domain models
- Clean separation between services and repository



## Architecture

- **Main** → Entry point and orchestration
- **CalculatorService** → Parsing and calculation logic
- **OperatorRepository** → Provides supported operators
- **PrintService** → Handles CLI output
- **CalculatorError** → Typed domain error modeling
- **Operation** → Domain model representing a single calculation



## What I learned

- Scala syntax
- Pure vs Non pure functions
- `ZIO[R, E, A]` effect model
- Typed error handling using `sealed trait`
- Functional composition using `for` comprehension
- Avoiding exception-heavy logic
- Service abstraction using traits
- Dependency injection using `ZLayer`
- Separation of concerns (service vs repository)
- Basic parsing without overengineering


## Supported Input

Valid examples:

```

10 + 5
10+5
-3 * 4
3 * -5
-3 * -4
2.5 + 1.2

```



## Invalid Input Examples

```

10 +
* 5
10 ++ 5
10 + 5 - 2
(10 + 5)

````

These will result in appropriate error messages.


## Example Flow

1. User enters an expression  
2. Input is validated:
   - Empty input → error  
   - Invalid expression → error  
   - Invalid number → error  
   - Invalid operator → error  
3. Expression is parsed into an `Operation`
4. Calculation is performed
5. Result is printed



## Running the Project

```bash
sbt run
````


## Sample Output

![CLI Output](docs/cli-output.png)



## Design Decisions

* Supports only **single binary expressions** (`a op b`)
* Does **not support chained operations or parentheses** (intentional to avoid overengineering)
* Unary minus (`-`) is supported for negative numbers
* Operator parsing is handled manually instead of using complex regex or parser libraries


