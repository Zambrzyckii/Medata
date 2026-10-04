# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) is a Medical Laboratory Information System (LIS) — a portfolio project built on top of the 7-lab course *Internet Services Architectures* (Gdańsk University of Technology): Java SE → Spring Boot & JPA → REST → microservices → Angular → Docker → service discovery & centralized config.

> Wersja polska: [README.pl.md](README.pl.md)

## Status

Lab 6 complete — the whole system containerized: `docker compose up` starts the Angular frontend on NGINX (:4200), Spring Cloud Gateway (:8080) and the `category` + `lab-test` microservices, each with its own PostgreSQL database. Live status: [docs/en/STATE.md](docs/en/STATE.md).

## Quick start

```bash
docker compose up --build
```

Then open `http://localhost:4200` (the app) or `http://localhost:8080/api/categories` (the API). Nothing but Docker is required — the images build the services themselves. Credentials can be overridden by copying `.env.example` to `.env`.

<details>
<summary>Development mode (hot reload, H2 in-memory, no Docker)</summary>

```bash
cd services/category && ./mvnw spring-boot:run    # :8081
cd services/lab-test && ./mvnw spring-boot:run    # :8082 (second terminal)
cd services/gateway && ./mvnw spring-boot:run     # :8080 (third terminal) — the API entry
cd web/catalog && npm start                       # :4200 (fourth terminal) — open this in the browser
```

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
