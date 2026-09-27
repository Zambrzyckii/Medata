# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) is a Medical Laboratory Information System (LIS) — a portfolio project built on top of the 7-lab course *Internet Services Architectures* (Gdańsk University of Technology): Java SE → Spring Boot & JPA → REST → microservices → Angular → Docker → service discovery & centralized config.

> Wersja polska: [README.pl.md](README.pl.md)

## Status

Lab 2 complete — the test catalog on Spring Boot + Spring Data JPA (in-memory H2), with a console CRUD. Live status: [docs/en/STATE.md](docs/en/STATE.md).

## Quick start

```bash
cd catalog && ./mvnw compile && java -cp target/classes com.medata.catalog.Main
```

Eventually this section will converge to a single `docker compose up` (one-command rule — see conventions).

## Requirements

- JDK 25 or newer (developed on OpenJDK 26)
- Maven not required — the repo ships a Maven Wrapper (`catalog/mvnw`)

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
