# Task Manager API (ZIO)

A simple REST API built using Scala and ZIO, evolving from an initial CLI-based application into an HTTP service, and later extended with background processing, concurrency, and resilience patterns.

## Overview

This project started similar to [CLI Calculator](../01-cli-calculator/README.md), then evolved into a web API using ZIO HTTP.

The goal was to move beyond basic request/response flows into more realistic asynchronous backend behavior.

## Features

* Create task → `POST /api/tasks`
* List tasks → `GET /api/tasks`
* Get task → `GET /api/tasks/{id}`
* Update task (mark as completed) → `PATCH /api/tasks/{id}`
* Delete task → `DELETE /api/tasks/{id}`
* Complete multiple tasks in parallel → `POST /api/tasks/complete-batch`
* Health check → `GET /api/health`

## Architecture

* **Main** → Server startup, dependency wiring, background worker lifecycle
* **Routes** → HTTP endpoint definitions
* **Handlers** → Request parsing and response mapping
* **Service** → Business logic + task event publishing + Notification Service
* **Repository** → In-memory state using `Ref`
* **Event Processor** → Background fiber consuming task events
* **Domain** → Core models, typed errors, task events

## What I learned

* Designing REST APIs using ZIO HTTP
* Handling request parsing and validation in handlers
* Converting domain errors into HTTP responses
* Working with JSON codecs (`JsonEncoder` / `JsonDecoder`)
* Structuring applications with HTTP, service, and repository layers
* Managing effect types (`ZIO[R, E, A]`) across layers
* Keeping core business logic independent from transport (CLI → HTTP)
* Using a background fiber to process task events asynchronously
* Running parallel task updates with `foreachPar`
* Applying retry strategies using `Schedule`
* Managing shared in-memory state safely with `Ref`
* Handling startup and shutdown of background workers with `forkScoped`
* Extending a CRUD API with event-driven processing patterns


## Example Request

```bash id="yvxikp"
curl -X POST http://localhost:8080/api/tasks ^
  -H "Content-Type: application/json" ^
  -d "{\"title\":\"Test task\"}"
```

## Batch Completion Example

```bash id="yvxikp"
curl -X PATCH http://localhost:8080/api/tasks/complete-batch ^
-H "Content-Type: application/json" ^ 
-d "{\"ids\":[1,2]}"
 ``` 

## Running the Project

```bash id="0wz50w"
sbt run
```

Server runs at:

```id="xy5fcm"
http://localhost:8080
```

## Notes

* Uses in-memory storage (no database)
* IDs are auto-incremented
* Update operation marks a task as completed
* Task completion triggers background event processing
* Notification sending retries on simulated failure
* Designed to learn progression from CLI → API → concurrency patterns

## Tech Stack

* Scala 3
* ZIO
* ZIO HTTP
* zio-json
* sbt
