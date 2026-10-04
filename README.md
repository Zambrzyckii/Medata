# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) is a Medical Laboratory Information System (LIS) — a portfolio project built on top of the 7-lab course *Internet Services Architectures* (Gdańsk University of Technology): Java SE → Spring Boot & JPA → REST → microservices → Angular → Docker → service discovery & centralized config.

> Wersja polska: [README.pl.md](README.pl.md)

## Status

Lab 4 complete — microservices `services/category` (:8081) and `services/lab-test` (:8082) with REST event sync behind Spring Cloud Gateway (:8080, the only public entry). Live status: [docs/en/STATE.md](docs/en/STATE.md).

## Quick start

```bash
cd services/category && ./mvnw spring-boot:run    # :8081
cd services/lab-test && ./mvnw spring-boot:run    # :8082 (second terminal)
cd services/gateway && ./mvnw spring-boot:run     # :8080 (third terminal) — talk only to this one
```

Eventually this section will converge to a single `docker compose up` (one-command rule — see conventions).

## Requirements

- JDK 25 or newer (developed on OpenJDK 27; Lombok pinned at 1.18.48 for JDK 27 support)
- Maven not required — every service ships its own Maven Wrapper (`services/<name>/mvnw`)

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
