# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-27

## Phase

**Lab 4 in progress** — the monorepo restructured onto `services/`: the `catalog` monolith split into `services/category` (8081, categories only, entity without the tests collection) and `services/lab-test` (8082, tests + a still-full category copy to be slimmed), `request.http` moved to `services/gateway/`. Both services build and run simultaneously (verified). The file restructuring was done by Claude at the user's explicit request.

## What exists

- `catalog/` — REST API on Spring Boot 4.0.8: JPA entities with delete cascade, 6 DTOs (create/update, read, list × 2 entities), 2 `@RestController`s (hierarchical routes, endpoint table in ARCHITECTURE.md), `GlobalExceptionHandler` (validation → 400), `SampleDataInitializer`, springdoc/Swagger UI, `request.http` (17 requests)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (containers + **API table** + ERD), `adr/001–003`, cheat sheets `labs/lab-1..3.md`
- Root: `CLAUDE.md`, README ×2 (CI badge), `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (CI green)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`, `lab-2`)

## Development environment

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; code typed by the user by hand (exception: lab 2 validation — explicit delegation)
- IDE: IntelliJ IDEA **Community** — `request.http` run via VS Code REST Client (the HTTP Client needs Ultimate); free JetBrains student licence suggested

## Next step

Lab 4, batch 1: slim the category replica in `lab-test` (entity `id`+`name`) + fixed UUIDs in both services' initializers. Then: internal event endpoints → publishing events via `RestClient` → the gateway project (verify ADR-002!) → end-to-end tests, a new CI workflow (matrix), the module docs template and the lab boundary.
