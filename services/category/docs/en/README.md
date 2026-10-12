# category service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The source of truth for test categories (`TestCategory`). Exposes REST CRUD and, on category create and delete, publishes a REST event to the `lab-test` service, which maintains a replica. Port **8081**, package `com.medata.category`, Spring Boot MVC (Tomcat).

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8081 (in-memory H2)
./mvnw spotless:apply verify    # format + full build
```

In containers the service runs from the whole-system `compose.yaml` in the repo root (`docker compose up --build`); the multi-stage `Dockerfile` here builds the jar with the service's own wrapper and runs it on an Eclipse Temurin 25 JRE. Development mode expects the discovery service to be running (:8761). Without it the app still works but keeps logging registration WARNs. The config service is optional in dev thanks to the `optional:` import.

## Configuration

The local `application.properties` holds development defaults. Under compose the database and discovery settings come from the **config service**, and the environment carries only what task 5 of lab 7 requires:

| Setting | Dev default (local file) | Under compose |
|---|---|---|
| `server.port` | `8081` | unchanged (`EXPOSE 8081`, not published to the host) |
| `spring.config.import` | `optional:configserver:` with fallback `http://localhost:8888` | `CONFIG_URL=http://config:8888` |
| `spring.datasource.*` | in-memory H2 | PostgreSQL, served by the config service |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka` (fallback of `DISCOVERY_URL`) | served by the config service |
| `eureka.instance.instance-id` | `category-local` | `INSTANCE_ID=category-1` |
| `labtest.base-url` | `http://lab-test` | same value, a logical name resolved through the registry |

Precedence runs env over config server over the local file. Boot infers the JDBC driver from the URL. The schema is owned by Flyway (`db/migration/V1__init.sql` + `V2__seed.sql`) with `ddl-auto=validate`, so Hibernate only checks it.

## Data model

One table `test_categories` (`id` UUID, `name`, `requires_fasting`). The entity has **no** tests collection — tests live in `lab-test`. Schema and seed live in the Flyway migrations, with 3 seed categories under the fixed UUIDs (`1111…`, `2222…`, `3333…`).

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

- Events are published by the **controller** (POST/DELETE), not the service. The seed is plain SQL in a migration, so it fires no events (lab-test seeds itself thanks to the fixed UUIDs).
- Publishing stays best-effort. `CategoryEventPublisher` uses a `@LoadBalanced` `RestClient` (the plain builder is `@Primary`, so the Eureka transport is not intercepted) and catches `RestClientException` and `IllegalStateException` (no instance known), logging a WARN. The service is never held hostage by the replica.
