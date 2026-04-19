# Scala ZIO Backend

A collection of backend-focused Scala projects built using **ZIO**, demonstrating functional programming, typed error handling, layered architecture, HTTP APIs, and practical concurrency patterns.

## Overview

This repository contains multiple independent projects, each focused on specific backend concepts and built progressively from simple CLI applications to web APIs with asynchronous processing.

The goal is to build a strong foundation in functional backend development using modern Scala patterns and practical system design.

## Projects

```text id="9wx9g2"
projects/
├── 01-cli-calculator/
├── 02-task-manager-api/
```

### 01 - CLI Calculator

* Stateless computation
* Input parsing
* Typed error handling
* Service + repository basics

### 02 - Task Manager API

* REST API using ZIO HTTP
* CRUD operations for tasks
* JSON request / response handling
* In-memory state using `Ref`
* Service + repository + HTTP layering
* Domain errors mapped to HTTP responses
* Background event processing with fibers
* Retry logic using `Schedule`
* Parallel batch task updates

## Concepts Covered

* Functional programming in Scala
* Typed error handling using sealed traits
* ZIO effect model (`ZIO[R, E, A]`)
* Dependency injection using `ZLayer`
* Separation of concerns across layers
* In-memory state management using `Ref`
* Request / response handling with ZIO HTTP
* JSON encoding / decoding with `zio-json`
* Background processing using fibers
* Parallel workflows with `foreachPar`
* Retry policies with `Schedule`
* Resource lifecycle management

---

## Tech Stack

* Scala 3
* ZIO
* ZIO HTTP
* zio-json
* sbt

---

## Running Projects

Each project is standalone.

```bash id="wq8grm"
cd projects/01-cli-calculator
sbt run
```

or

```bash id="pkh7x4"
cd projects/02-task-manager-api
sbt run
```

## Goal

To design and implement backend systems using functional programming and modern Scala patterns, progressing from CLI applications to structured web APIs with practical concurrency patterns.

## Author

Built as part of backend engineering practice using Scala and ZIO.
