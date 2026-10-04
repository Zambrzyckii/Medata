# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-10-04

## Phase

**Lab 4 COMPLETE (8/8 pts), awaiting the commit + `lab-4` tag.** The system is three independent applications: `services/category` (:8081) and `services/lab-test` (:8082) with private H2s and a category replica synced by REST events (idempotent upsert, best-effort with WARN, test cascade), plus `services/gateway` (:8080, Spring Cloud Gateway WebFlux) — the only public entry; `/internal/**` has no route. Everything verified e2e through :8080, including the resilience test with lab-test down.

## What exists

- `services/category` (:8081) — categories: REST CRUD, `GlobalExceptionHandler`, springdoc, fixed-UUID seed; `CategoryEventPublisher` (`RestClient`, events on POST/DELETE)
- `services/lab-test` (:8082) — tests + replica (`id`+`name`): the full LabTest stack, `PUT`/`DELETE /internal/categories/{id}` (idempotent), cascade, springdoc, seed under the fixed UUIDs
- `services/gateway` (:8080) — `Path=` routes, most specific first (table in `docs/`), the whole system's `request.http`
- Per service: `docs/{pl,en}/README.md` (module template) + `CLAUDE.md` + its own Maven wrapper
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (3 containers + event sequence + two-database ERD), `adr/001–003` (ADR-002 verified), cheat sheets `labs/lab-1..4.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (matrix `category`/`lab-test`/`gateway`)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`–`lab-3`; `lab-4` after the commit)

## Development environment

- OpenJDK 27 (`--release 25`; Arch rolling upgrade of Oct 3), Lombok 1.18.48 (pinned in the poms, JDK 27 support), Maven 3.9.16 + a wrapper per service; code typed by the user by hand (exceptions on explicit request: lab 2 validation, the `services/` restructuring, the batch 1 seed fix)
- IDE: IntelliJ IDEA **Community** (Project SDK: java-27-openjdk) — `request.http` via VS Code REST Client; free JetBrains student licence suggested

## Next step

Commit + `lab-4` tag (done by the user). Then **lab 5 (9 pts): the Angular frontend** — 7 views (lists, forms, category and test details) with routing, all traffic through the gateway (:8080); a new `web/` directory, ESLint + Prettier convention activation; to check: Node.js in the environment.
