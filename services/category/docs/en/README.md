# category service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The source of truth for test categories (`TestCategory`). Exposes REST CRUD and, on category create and delete, publishes a REST event to the `lab-test` service, which maintains a replica. Port **8081**, package `com.medata.category`, Spring Boot MVC (Tomcat).

## Running

```bash
./mvnw spring-boot:run          # starts on :8081
./mvnw spotless:apply verify    # format + full build
```

## Configuration (`src/main/resources/application.properties`)

| Key | Value | Why |
|---|---|---|
| `server.port` | `8081` | fixed service port |
| `spring.datasource.url` | `jdbc:h2:mem:category` | private in-memory database |
| `labtest.base-url` | `http://localhost:8082` | event receiver address (labs 6/7: overridden by the environment) |

## Data model

One table `test_categories` (`id` UUID, `name`, `requires_fasting`). The entity has **no** tests collection — tests live in `lab-test`. Seed: 3 categories with fixed UUIDs (`1111…`, `2222…`, `3333…`).

## API

| Endpoint | Description | Codes |
|---|---|---|
| `GET /api/categories` | list (id + name) | 200 |
| `POST /api/categories` | creates + **upsert event** to lab-test | 201 |
| `GET /api/categories/{id}` | full category data | 200, 404 |
| `PUT /api/categories/{id}` | updates (no event — deliberate gap) | 204, 404 |
| `DELETE /api/categories/{id}` | deletes + **delete event** to lab-test | 204, 404 |

Swagger UI: `http://localhost:8081/swagger-ui.html`.

## Decisions

- Events are published by the **controller** (POST/DELETE), not the service — the seed fires no redundant events (lab-test seeds itself thanks to the fixed UUIDs).
- Best-effort publishing: `CategoryEventPublisher` catches `RestClientException` and logs a WARN — the service is not held hostage by the replica.
