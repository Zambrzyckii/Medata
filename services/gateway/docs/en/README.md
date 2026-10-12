# gateway service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The system's only public entry (**:8080**): Spring Cloud Gateway in the reactive flavor (WebFlux, Netty), forwarding requests to the `category` (:8081) and `lab-test` (:8082) services. Zero Java logic — everything is route configuration in properties. Package `com.medata.gateway`.

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8080 ("Netty started")
./mvnw spotless:apply verify    # format + full build
```

Requires discovery to be up. A route whose target has no known instance answers 503, a matched route with a dead instance answers 500, no matching route answers 404. In containers the gateway runs from the root `compose.yaml` (`docker compose up --build`), same multi-stage Dockerfile pattern as the other services.

## Configuration

Routes in `src/main/resources/application.properties`. Since lab 7 the target URIs use the `lb://` scheme, which means "take the instances of this logical name from the registry and balance round-robin". The same configuration works in dev and in containers, because the registry absorbs the environment differences:

| Order | `Path=` predicate | Target URI |
|---|---|---|
| 0 | `/api/categories/*/tests` | `lb://lab-test` |
| 1 | `/api/categories/**` | `lb://category` |
| 2 | `/api/tests/**` | `lb://lab-test` |

Most specific first (`*` = one segment, `**` = any number). `/internal/**` deliberately has no route. The registry address comes from `DISCOVERY_URL` (dev fallback `http://localhost:8761/eureka`) and the instance id from `INSTANCE_ID`. With two lab-test instances in compose the balancing is visible in their logs.

## Data model

None — the gateway stores no data.

## API

Defines no API of its own — it forwards requests 1:1 (path unchanged). Executable examples for the whole system: `request.http` in this directory (everything via :8080).

## Decisions

- The **WebFlux/Netty** flavor: a gateway mostly pumps bytes — non-blocking I/O serves many connections with a handful of threads; the project's only reactive application.
- Spring Cloud versions are picked by the `spring-cloud-dependencies 2025.1.3` BOM imported in `dependencyManagement` (pairing with Boot 4.0.8 — ADR-002, confirmed by the build).
- Not a config-service client on purpose. Task 5 of lab 7 says "services", so the gateway keeps `DISCOVERY_URL` and `INSTANCE_ID` in the environment.
