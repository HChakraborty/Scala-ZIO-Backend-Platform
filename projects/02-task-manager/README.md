# Task Manager API

A simple REST API built by evolving from CLI-based application (check GitHub history) into an HTTP service.

## Overview

This project started similar to [CLI Calculator](../01-cli-calculator/README.md), then evolved into a web API using ZIO HTTP and later we added some concurrency concepts and automated tests.


## Features

* Create task → `POST /api/tasks`
* List tasks → `GET /api/tasks`
* Get task → `GET /api/tasks/{id}`
* Update task (mark as completed) → `PATCH /api/tasks/{id}`
* Delete task → `DELETE /api/tasks/{id}`
* Complete multiple tasks concurrently → `PATCH /api/tasks/complete-batch`
* Health check → `GET /api/health`

## Architecture

* **Main** → Server startup, dependency wiring, background worker lifecycle
* **Routes** → HTTP endpoint definitions
* **Handlers** → Request parsing and response mapping
* **Service** → Business logic + task event publishing + notification triggering
* **Repository** → In-memory state using `Ref`
* **Event Processor** → Background fiber consuming task events
* **Domain** → Core models, typed errors, task events
* **Tests** → Basic service and repository specs using ZIO Test


## What I learned

* Designing REST APIs using ZIO HTTP
* Handling request parsing and validation in handlers
* Converting domain errors into HTTP responses
* Working with JSON codecs (`JsonEncoder` / `JsonDecoder`)
* Using a background fiber to process task events asynchronously
* Running concurrent task updates with `foreachPar`
* Applying retry strategies using `Schedule`
* Managing in-memory state safely with `Ref`
* Handling startup and shutdown of background workers with `forkScoped`
* Extending a CRUD API with event-driven processing patterns
* Writing simple automated tests with ZIO Test for service and repository layers

## Example Request

```bash
curl -X POST http://localhost:8080/api/tasks ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Test task\"}"
````

## Batch Completion Example

```bash
curl -X PATCH http://localhost:8080/api/tasks/complete-batch ^
  -H "Content-Type: application/json" ^
  -d "{\"ids\":[1,2]}"
```

## Running the Project

```bash
sbt run
```

Server runs at:

```text
http://localhost:8080
```

## Running Tests

```bash
sbt test
```

## Notes

* Uses in-memory storage (no database, as of yet, will expand in another project)
* IDs are auto-incremented
* Update operation marks a task as completed
* Task completion triggers background event processing
* Notification sending retries on simulated failure


## Tech Stack

* Scala 3
* ZIO
* ZIO HTTP
* zio-json
* ZIO Test
* sbt

