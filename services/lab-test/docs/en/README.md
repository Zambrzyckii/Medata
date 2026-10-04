# lab-test service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

Owner of laboratory tests (`LabTest`) together with a category **replica** (`TestCategory`: `id`+`name` only), kept in sync by events from the `category` service. Port **8082**, package `com.medata.labtest`, Spring Boot MVC (Tomcat).

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8082 (in-memory H2)
./mvnw spotless:apply verify    # format + full build
```

In containers the service runs from the whole-system `compose.yaml` in the repo root (`docker compose up --build`); the multi-stage `Dockerfile` here builds the jar with the service's own wrapper and runs it on an Eclipse Temurin 25 JRE.

## Configuration

Defaults in `src/main/resources/application.properties`; every key can be overridden by an environment variable (Spring relaxed binding), which is exactly what `compose.yaml` does:

| Property | Default | Env override (compose) |
|---|---|---|
| `server.port` | `8082` | — (fixed; `EXPOSE 8082`, not published to the host) |
| `spring.datasource.url` | `jdbc:h2:mem:labtest` | `SPRING_DATASOURCE_URL=jdbc:postgresql://lab-test-db:5432/labtest` |
| `spring.datasource.username` / `password` | `sa` / empty | `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` |

No `driver-class-name` anywhere: Boot infers the driver from the JDBC URL. `ddl-auto=create-drop` applies to both engines (schema + seed per start; migrations come in lab 7).

## Data model

`test_categories` (replica: `id`, `name` — **no** random default id, values come from events/seed) `1 : N` `lab_tests` (`id`, `name`, `unit`, `reference_min`, `reference_max`, `price`, `category_id` FK). `REMOVE` + `orphanRemoval` cascade: removing a replica deletes its tests. Seed: replicas under the fixed UUIDs (`1111…`, `2222…`, `3333…`) + 6 tests.

## API

| Endpoint | Description | Codes |
|---|---|---|
| `GET /api/tests` | test list (id + name) | 200 |
| `GET /api/tests/{id}` | full data (category flattened to its name) | 200, 404 |
| `PUT /api/tests/{id}` | updates a test | 204, 400, 404 |
| `DELETE /api/tests/{id}` | deletes a test | 204, 404 |
| `GET /api/categories/{categoryId}/tests` | tests of a category (empty → 200 `[]`, missing → 404) | 200, 404 |
| `POST /api/categories/{categoryId}/tests` | the only way to create tests | 201, 400, 404 |
| `PUT /internal/categories/{id}` | **event**: idempotent replica upsert | 204 |
| `DELETE /internal/categories/{id}` | **event**: replica removal (+ test cascade) | 204 |

`/internal/**` has no gateway route — reachable service-to-service only. Swagger UI: `http://localhost:8082/swagger-ui.html`.

## Decisions

- The replica is slimmed to the minimum the relation and DTOs need (`id`+`name`, no `requiresFasting`).
- Event handlers are idempotent: `PUT` = upsert (`orElseGet`), `DELETE` of a missing replica → 204 — repeated (at-least-once) events are safe.
