# gateway service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The system's only public entry (**:8080**): Spring Cloud Gateway in the reactive flavor (WebFlux, Netty), forwarding requests to the `category` (:8081) and `lab-test` (:8082) services. Zero Java logic — everything is route configuration in properties. Package `com.medata.gateway`.

## Running

```bash
./mvnw spring-boot:run          # starts on :8080 ("Netty started")
./mvnw spotless:apply verify    # format + full build
```

Requires the target services to be up — without them, matched routes return 500 (connection refused to the target), not 404.

## Configuration (`src/main/resources/application.properties`)

| Order | `Path=` predicate | Target |
|---|---|---|
| 0 | `/api/categories/*/tests` | `http://localhost:8082` |
| 1 | `/api/categories/**` | `http://localhost:8081` |
| 2 | `/api/tests/**` | `http://localhost:8082` |

Most specific first (`*` = one segment, `**` = any number). `/internal/**` deliberately has no route. Target addresses are hardcoded until lab 6 (then the environment), replaced by discovery (`lb://`) in lab 7.

## Data model

None — the gateway stores no data.

## API

Defines no API of its own — it forwards requests 1:1 (path unchanged). Executable examples for the whole system: `request.http` in this directory (everything via :8080).

## Decisions

- The **WebFlux/Netty** flavor: a gateway mostly pumps bytes — non-blocking I/O serves many connections with a handful of threads; the project's only reactive application.
- Spring Cloud versions are picked by the `spring-cloud-dependencies 2025.1.3` BOM imported in `dependencyManagement` (pairing with Boot 4.0.8 — ADR-002, confirmed by the build).
