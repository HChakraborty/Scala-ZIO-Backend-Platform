# Scala ZIO Backend

A collection of backend-focused Scala projects built using **ZIO**, demonstrating functional programming (FP), HTTP APIs, concurrency patterns and automated testing.

## Overview

This repository contains 2 independent Scala projects(as of now) built progressively to strengthen backend development skills using ZIO.

The projects are intentionally structured as a learning progression:

- **Project 01** focuses on fp basics, handling side effects and service/repository separation in a simple CLI program.
- **Project 02** builds on those same fconcepts and extends them into a simple backend system with Rest APIs and concurrency concepts.

### Note

I had some academic exposure to FP (2 years) during university, and these projects helped me build on that in a more practical way through Scala and ZIO.

As time goes, I will keep making new projects that uses concepts from previous projects to show progression. This will help in understanding more advanced concepts.

## Projects

```text
projects/
├── 01-cli-calculator/
├── 02-task-manager-api/
````

### 01 - [CLI Calculator](./projects/01-cli-calculator/README.md)

Focus:

* Stateless computation
* Input parsing
* Typed error handling
* Service + repository basics
* Simple automated tests with ZIO Test

This project establishes the core ZIO and Scala patterns used throughout the repository.

### 02 - [Task Manager API](./projects//02-task-manager/README.md)

Focus:

* REST API using ZIO HTTP
* CRUD operations for tasks
* JSON request / response handling
* In-memory state using `Ref`
* Service + repository + HTTP layering
* Domain errors mapped to HTTP responses
* Background event processing with fibers
* Retry logic using `Schedule`
* Parallel batch task updates
* Basic service and repository tests with ZIO Test

This project expands on the concepts from the CLI Calculator and applies them to a more simple backend like application.

## Concepts Covered

* Functional programming in Scala
* Typed error handling using sealed traits
* ZIO effect model (`ZIO[R, E, A]`)
* Dependency injection using `ZLayer`
* In-memory state management using `Ref`
* Request / response handling with ZIO HTTP
* Concurrency and retry handling
* Basic automated testing with ZIO Test


## Tech Stack

* Scala 3
* ZIO
* ZIO HTTP
* zio-json
* ZIO Test
* sbt

---

## Running Projects

Each project is standalone.

```bash
cd projects/01-cli-calculator
sbt run
```

or

```bash
cd projects/02-task-manager-api
sbt run
```

## Running Tests

From inside a project folder:

```bash
sbt test
```



