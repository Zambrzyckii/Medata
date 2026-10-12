# lab-test service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

Owner of laboratory tests (`LabTest`) together with a category **replica** (`TestCategory`: `id`+`name` only), kept in sync by events from the `category` service. Port **8082**, package `com.medata.labtest`, Spring Boot MVC (Tomcat).

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8082 (in-memory H2)
./mvnw spotless:apply verify    # format + full build
```

In containers the root `compose.yaml` runs **two peer instances** of this image (`lab-test-1`, `lab-test-2`) on one shared database, and the gateway balances requests between them. Development mode runs a single instance and expects the discovery service on :8761. The config service is optional in dev thanks to the `optional:` import.

## Configuration

The local `application.properties` holds development defaults. Under compose the database and discovery settings come from the **config service**. Both instances ask it for the same `lab-test` configuration, which is exactly what keeps them on one database:

| Setting | Dev default (local file) | Under compose |
|---|---|---|
| `server.port` | `8082` | unchanged (`EXPOSE 8082`, not published to the host) |
| `spring.config.import` | `optional:configserver:` with fallback `http://localhost:8888` | `CONFIG_URL=http://config:8888` |
| `spring.datasource.*` | in-memory H2 | PostgreSQL, served by the config service |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka` (fallback of `DISCOVERY_URL`) | served by the config service |
| `eureka.instance.instance-id` | `lab-test-local` | `INSTANCE_ID=lab-test-1` and `lab-test-2` |

Precedence runs env over config server over the local file. The schema is owned by Flyway (`db/migration/V1__init.sql` + `V2__seed.sql`) with `ddl-auto=validate`. On a shared database only one instance applies the migrations, the other waits on the Flyway lock and skips.

## Data model

`test_categories` (replica: `id`, `name` — **no** random default id, values come from events/seed) `1 : N` `lab_tests` (`id`, `name`, `unit`, `reference_min`, `reference_max`, `price`, `category_id` FK). `REMOVE` + `orphanRemoval` cascade: removing a replica deletes its tests. Schema and seed live in the Flyway migrations. Replicas keep the fixed UUIDs (`1111…`, `2222…`, `3333…`) and the 6 seed tests carry fixed `aaaaaaaa-…-000N` ids, so re-running a seed can never duplicate them.

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
