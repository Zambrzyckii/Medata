# category service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The source of truth for test categories (`TestCategory`). Exposes REST CRUD and, on category create and delete, publishes a REST event to the `lab-test` service, which maintains a replica. Port **8081**, package `com.medata.category`, Spring Boot MVC (Tomcat).

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8081 (in-memory H2)
./mvnw spotless:apply verify    # format + full build
```

In containers the service runs from the whole-system `compose.yaml` in the repo root (`docker compose up --build`); the multi-stage `Dockerfile` here builds the jar with the service's own wrapper and runs it on an Eclipse Temurin 25 JRE.

## Configuration

Defaults in `src/main/resources/application.properties`; every key can be overridden by an environment variable (Spring relaxed binding), which is exactly what `compose.yaml` does:

| Property | Default | Env override (compose) |
|---|---|---|
| `server.port` | `8081` | — (fixed; `EXPOSE 8081`, not published to the host) |
| `spring.datasource.url` | `jdbc:h2:mem:category` | `SPRING_DATASOURCE_URL=jdbc:postgresql://category-db:5432/category` |
| `spring.datasource.username` / `password` | `sa` / empty | `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` |
| `labtest.base-url` | `http://localhost:8082` | `LABTEST_BASE_URL=http://lab-test:8082` |

No `driver-class-name` anywhere: Boot infers the driver from the JDBC URL, so the H2↔PostgreSQL switch is nothing but the URL. `ddl-auto=create-drop` applies to both engines (schema + seed per start; migrations come in lab 7).

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
