# gateway service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The system's only public entry (**:8080**): Spring Cloud Gateway in the reactive flavor (WebFlux, Netty), forwarding requests to the `category` (:8081) and `lab-test` (:8082) services. Zero Java logic — everything is route configuration in properties. Package `com.medata.gateway`.

## Running

```bash
./mvnw spring-boot:run          # dev mode on :8080 ("Netty started")
./mvnw spotless:apply verify    # format + full build
```

Requires the target services to be up — without them, matched routes return 500 (connection refused to the target), not 404. In containers the gateway runs from the root `compose.yaml` (`docker compose up --build`), same multi-stage Dockerfile pattern as the other services; it is one of only two containers with a published host port.

## Configuration

Routes in `src/main/resources/application.properties`; since lab 6 the target URIs are `${VAR:default}` placeholders — the default keeps dev mode working, the env var (set in `compose.yaml`) redirects to container DNS names:

| Order | `Path=` predicate | Target URI |
|---|---|---|
| 0 | `/api/categories/*/tests` | `${LABTEST_URL:http://localhost:8082}` |
| 1 | `/api/categories/**` | `${CATEGORY_URL:http://localhost:8081}` |
| 2 | `/api/tests/**` | `${LABTEST_URL:http://localhost:8082}` |

Most specific first (`*` = one segment, `**` = any number). `/internal/**` deliberately has no route. Compose sets `CATEGORY_URL=http://category:8081` and `LABTEST_URL=http://lab-test:8082`; discovery (`lb://`) replaces this in lab 7.

## Data model

None — the gateway stores no data.

## API

Defines no API of its own — it forwards requests 1:1 (path unchanged). Executable examples for the whole system: `request.http` in this directory (everything via :8080).

## Decisions

- The **WebFlux/Netty** flavor: a gateway mostly pumps bytes — non-blocking I/O serves many connections with a handful of threads; the project's only reactive application.
- Spring Cloud versions are picked by the `spring-cloud-dependencies 2025.1.3` BOM imported in `dependencyManagement` (pairing with Boot 4.0.8 — ADR-002, confirmed by the build).
