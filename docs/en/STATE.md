# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-27

## Phase

**Lab 3 COMPLETE** — all tasks 1–3 (8/8 pts) verified by the build, an API smoke test (200/201/204/400/404, cascade) and springdoc. Awaiting the commit with the `lab-3` tag.

## What exists

- `catalog/` — REST API on Spring Boot 4.0.8: JPA entities with delete cascade, 6 DTOs (create/update, read, list × 2 entities), 2 `@RestController`s (hierarchical routes, endpoint table in ARCHITECTURE.md), `GlobalExceptionHandler` (validation → 400), `SampleDataInitializer`, springdoc/Swagger UI, `request.http` (17 requests)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (containers + **API table** + ERD), `adr/001–003`, cheat sheets `labs/lab-1..3.md`
- Root: `CLAUDE.md`, README ×2 (CI badge), `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (CI green)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`, `lab-2`)

## Development environment

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; code typed by the user by hand (exception: lab 2 validation — explicit delegation)
- IDE: IntelliJ IDEA **Community** — `request.http` run via VS Code REST Client (the HTTP Client needs Ultimate); free JetBrains student licence suggested

## Next step

Commit + tag `lab-3` (user). Then **Lab 4 — microservices**: split into a category service and a test service (private databases, simplified category replica), event-style REST communication on category add/remove, Spring Cloud Gateway with routing, `request.http` updated to the gateway port. Lab 4 conventions: module documentation template + event sequence diagram. Note: verify the Spring Cloud `2025.1.x` ↔ Boot 4.0 pairing (ADR-002).
