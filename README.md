# Scala ZIO Backend

A collection of backend-focused Scala projects built using **ZIO**, demonstrating functional programming, state management, typed error handling, and layered backend architecture.

## Overview

This repository contains multiple independent projects, each focused on a specific backend concept and built progressively from simple CLI applications to HTTP APIs.

The goal is to build a strong foundation in functional backend development using modern Scala patterns and practical system design.

## Projects

```text
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

## Concepts Covered

* Functional programming in Scala
* Typed error handling using sealed traits
* ZIO effect model (`ZIO[R, E, A]`)
* Dependency injection using `ZLayer`
* Separation of concerns across layers
* In-memory state management using `Ref`
* Request / response handling with ZIO HTTP
* JSON encoding / decoding with `zio-json`

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

```bash
cd projects/01-cli-calculator
sbt run
```

or

```bash
cd projects/02-task-manager-api
sbt run
```

## Goal

To design and implement backend systems using functional programming and modern Scala patterns, progressing from CLI applications to structured web APIs.

## Author

Built as part of backend engineering practice using Scala and ZIO.
