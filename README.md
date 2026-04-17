
# Scala ZIO Backend

A collection of backend-focused Scala projects built using **ZIO**, demonstrating functional programming, state management, and layered backend architecture.

## Overview

This repository contains multiple independent projects, each focusing on a specific backend concept.

The goal is to build a strong foundation in functional backend development through progressively structured systems.


## Projects

```text
projects/
├── 01-cli-calculator/
├── 02-cli-task-manager/
````

### 01 - CLI Calculator

* Stateless computation
* Input parsing
* Typed error handling
* Service + repository basics

### 02 - CLI Task Manager

* Stateful application using `Ref`
* In-memory data storage
* CRUD-style workflow
* Service + repository layering with state

## Concepts Covered

* Functional programming in Scala
* Typed error handling using sealed traits
* ZIO effect model (`ZIO[R, E, A]`)
* Separation of concerns (service vs repository)
* Dependency injection using `ZLayer`
* In-memory state management using `Ref`

---

## Tech Stack

* Scala 3
* ZIO
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
cd projects/02-cli-task-manager
sbt run
```


## Goal

To design and implement backend systems using functional programming and modern Scala patterns, with a focus on clean architecture and composability.

## Author

Built as part of backend engineering practice using Scala and ZIO.