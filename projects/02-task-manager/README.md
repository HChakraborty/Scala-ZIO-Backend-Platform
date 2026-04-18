# Task Manager API (ZIO)

A simple REST API built using Scala and ZIO, evolving from an initial CLI-based application into an HTTP service to explore real-world backend design patterns.

## Overview

This project started similar to [CLI Calculator](../01-cli-calculator/README.md) and was later extended into a web API using ZIO HTTP.

The focus was to move from simple command handling to structured request/response handling, while keeping the same core domain and service logic.

## Features

* Create task → `POST /api/tasks`
* List tasks → `GET /api/tasks`
* Get task → `GET /api/tasks/{id}`
* Update task (mark as completed) → `PATCH /api/tasks/{id}`
* Delete task → `DELETE /api/tasks/{id}`
* Health check → `GET /api/health`

## Architecture

* **Main** → Server startup and dependency wiring
* **Routes** → HTTP endpoint definitions
* **Handlers** → Request parsing and response mapping
* **Service** → Business logic
* **Repository** → In-memory state using `Ref`
* **Domain** → Core models and typed errors

## What changed from CLI to API

* Replaced command parsing with HTTP routing
* Introduced request/response handling using ZIO HTTP
* Added JSON encoding/decoding using `zio-json`
* Mapped domain errors to proper HTTP responses (400, 404)
* Separated HTTP layer (routes/handlers) from service layer

## What I learned

* Designing REST APIs using ZIO HTTP
* Handling request parsing and validation in handlers
* Converting domain errors into HTTP responses
* Working with JSON codecs (`JsonEncoder` / `JsonDecoder`)
* Structuring applications with HTTP, service, and repository layers
* Managing effect types (`ZIO[R, E, A]`) across layers
* Keeping core business logic independent from transport (CLI → HTTP)

## Example Request

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test task"}'
```

## Running the Project

```bash
sbt run
```

Server runs at:

```
http://localhost:8080
```

## Notes

* Uses in-memory storage (no database)
* IDs are auto-incremented
* Update operation marks a task as completed
* Focused on learning transition from CLI to HTTP backend design

## Tech Stack

* Scala 3
* ZIO
* ZIO HTTP
* zio-json
* sbt


