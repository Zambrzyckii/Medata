# Medata — Changelog

> English version. Polish 1:1 counterpart: [../pl/CHANGELOG.md](../pl/CHANGELOG.md)
> Milestone-level entries, newest first.

- **2026-10-04** — **Lab 6 complete (9/9 pts)**: the whole system containerized — multi-stage Dockerfiles for `category`, `lab-test`, `gateway` (Temurin 25 JDK build → JRE runtime) and `web/catalog` (Node build → NGINX with an `envsubst` template proxying `/api` to `GATEWAY_URL`); root `compose.yaml` wiring 6 containers (only web :4200 and gateway :8080 published) with PostgreSQL 18 as both services' external databases (healthchecks + `depends_on`); gateway routes and the datasource switched to env-var configuration (`${VAR:default}` placeholders, driver inferred from the JDBC URL). Lab 6 conventions activated: `.env.example`, the full one-command rule (`docker compose up --build`). Docker layer written by Claude on explicit delegation. Cheat sheet: `labs/lab-6.md`.

- **2026-10-04** — **Lab 5 complete (9/9 pts)**: the Angular 22 frontend in `web/catalog` (standalone, signals, zoneless, typed reactive forms with pre-population) — 7 routed CRUD views for categories and tests, all traffic through the gateway (dev proxy `/api` → :8080), static-before-dynamic route order. Lab 5 convention activated: ESLint + Prettier; a new frontend job in CI. Cheat sheet: `labs/lab-5.md`.

- **2026-10-04** — **Lab 4 complete (8/8 pts)**: the monolith split into `services/category` (:8081) and `services/lab-test` (:8082) with private H2s and a category replica (`id`+`name`); REST add/remove events (`PUT`/`DELETE /internal/categories/{id}`, idempotent upsert, best-effort with WARN); `services/gateway` (:8080, Spring Cloud Gateway WebFlux, BOM 2025.1.3) with most-specific-first routes and no route for `/internal`; CI matrix × 3 services; Lombok pinned at 1.18.48 after the system's JDK 27 upgrade. Lab 4 convention activated: module docs + per-service `CLAUDE.md`, event sequence diagram in ARCHITECTURE. Cheat sheet: `labs/lab-4.md`.

- **2026-09-27** — **Lab 3 complete (8/8 pts)**: REST API — 6 DTOs (create/update, read, list per entity), controllers with hierarchical routes and 200/201/204/400/404 codes, empty vs non-existing category distinction, cascade delete, `GlobalExceptionHandler` (validation → 400), `request.http` (17 requests), springdoc/Swagger UI. `ConsoleRunner` deleted (lives in the `lab-2` tag). Cheat sheet: `labs/lab-3.md`.

- **2026-09-27** — **Lab 2 complete (8/8 pts)**: `catalog` rebuilt on Spring Boot 4.0.8 — JPA entities (in-memory H2, client UUIDs), repositories, services with input validation, initializer, console CRUD runner. Lab 2 conventions activated: `ARCHITECTURE.md` (Mermaid: containers + ERD), ADRs 001–003, GitHub Actions CI. Cheat sheet: `labs/lab-2.md`.

- **2026-09-26** — **Lab 1 complete (8/8 pts)**: console application `catalog/` (Java SE, Maven, Lombok, Spotless, wrapper) — entities with builder and comparison, DTO record, sample data, three Stream API pipelines, binary serialization, parallelism on a custom `ForkJoinPool`. Code typed by hand by the user; presentation cheat sheet: `labs/lab-1.md`.

- **2026-09-26** — Growability-conventions package adopted (README EN+PL, `.editorconfig`, lab-boundary checklist, per-lab convention activation plan); `CONVENTIONS.md` extended with sections 5–8.

- **2026-09-26** — Documentation structure created (`docs/pl`, `docs/en`): vision document (CORE + FUTURE), working conventions, decision log and root `CLAUDE.md`. Project name settled: **Medata**.
- **2026-08-19** — Project domain chosen: diagnostic laboratory (LIS).
