# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) is a Medical Laboratory Information System (LIS) — a portfolio project built on top of the 7-lab course *Internet Services Architectures* (Gdańsk University of Technology): Java SE → Spring Boot & JPA → REST → microservices → Angular → Docker → service discovery & centralized config.

> Wersja polska: [README.pl.md](README.pl.md)

## Status

Course part complete (labs 1 to 7). `docker compose up` starts 9 containers, the Angular frontend on NGINX (:4200), Spring Cloud Gateway with `lb://` routes (:8080), the `category` service and two load-balanced `lab-test` instances, Eureka discovery (:8761), a central config server (:8888) and two PostgreSQL databases on named volumes with Flyway migrations. Live status: [docs/en/STATE.md](docs/en/STATE.md).

## Quick start

```bash
docker compose up --build
```

Then open `http://localhost:4200` (the app) or `http://localhost:8080/api/categories` (the API). Nothing but Docker is required — the images build the services themselves. Credentials can be overridden by copying `.env.example` to `.env`.

<details>
<summary>Development mode (hot reload, H2 in-memory, no Docker)</summary>

```bash
cd services/discovery && ./mvnw spring-boot:run   # :8761 — the registry, start it first
cd services/category && ./mvnw spring-boot:run    # :8081 (second terminal)
cd services/lab-test && ./mvnw spring-boot:run    # :8082 (third terminal)
cd services/gateway && ./mvnw spring-boot:run     # :8080 (fourth terminal) — the API entry
cd web/catalog && npm start                       # :4200 (fifth terminal) — open this in the browser
```

The config service is optional in dev. Without it the services fall back to in-memory H2 and localhost defaults.

</details>

## Requirements

- Docker with the Compose plugin — that is all for the one-command run
- Development mode additionally: JDK 25+ (developed on OpenJDK 27; Lombok pinned at 1.18.48), Node.js 20+ with npm (developed on Node 26); Maven not required — every service ships its own wrapper (`services/<name>/mvnw`)

## Documentation

Bilingual EN/PL, 1:1 structure — [docs/en](docs/en) / [docs/pl](docs/pl):

- [Vision](docs/en/VISION.md) — what we are building (CORE labs + FUTURE backlog)
- [Conventions](docs/en/CONVENTIONS.md) — how we work (git, docs, quality, definition of done)
- [Decisions](docs/en/DECISIONS.md) — why it is the way it is
- [State](docs/en/STATE.md) — where the project currently stands
- [Changelog](docs/en/CHANGELOG.md) — milestone history
- [Architecture](docs/en/ARCHITECTURE.md) — container and ERD diagrams (Mermaid)
- [ADRs](docs/en/adr) — architecture decision records
- [Lab cheat sheets](docs/en/labs) — presentation prep (lab-1, lab-2)
