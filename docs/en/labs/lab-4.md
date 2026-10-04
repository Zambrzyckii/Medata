# Lab 4 — microservices and Spring Cloud Gateway: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-4.md](../../pl/labs/lab-4.md)

## What was built

The monolith cut into `services/category` (:8081) and `services/lab-test` (:8082) — each with its OWN H2 database and own build (no parent pom); category-replica sync via REST events, plus `services/gateway` (:8080, Spring Cloud Gateway/WebFlux) with three routes — all 3 tasks complete (3+3+2 = 8/8 pts).
Running: three terminals, `./mvnw spring-boot:run` in each service; all traffic via `http://localhost:8080` (`services/gateway/request.http`).

## Critical sections — likely questions

- **Category replica in lab-test:** `id`+`name` only (the minimum for the relation and DTOs); `@Id` without a random default — the replica never invents ids, they come from events/seed.
- **Matching ids across databases without a shared database:** fixed seed UUIDs (`1111…`), duplicated in both services — duplication instead of a shared library (the polyglot-monorepo rule).
- **Events:** published by the category controller on POST/DELETE (`RestClient`), received by `InternalCategoryController`. `PUT` = idempotent upsert (`orElseGet`), `DELETE` of a missing replica → 204 — a repeated (at-least-once) event does no harm.
- **What if lab-test is down:** category operations still return 2xx — publishing sits in `try/catch` → WARN (best-effort, eventual consistency). In production: a broker + the outbox pattern; the instruction explicitly allows REST.
- **A category rename is NOT propagated** — the instruction requires events only on add/remove; a deliberate gap (DECISIONS).
- **Cascade on the delete event:** removing the replica → `CascadeType.REMOVE` + `orphanRemoval` delete its tests — the instruction's "appropriate JPA configuration".
- **Gateway route order:** `/api/categories/*/tests` (→ lab-test) BEFORE `/api/categories/**` (→ category), or the catch-all steals test requests; `*` = one segment, `**` = any number.
- **`/internal/**` has no route** — the event API is invisible from behind the gateway (protection by routing). Diagnostics: gateway 404 = no route; 500 = route matched, target down.
- **Why the gateway runs on WebFlux/Netty:** it pumps bytes — non-blocking I/O serves crowds of connections with a handful of threads; the project's only reactive app (the rest: classic Tomcat).
- **The `spring-cloud-dependencies 2025.1.3` BOM:** imported in `dependencyManagement`, it picks Cloud starter versions matching Boot 4.0.8 (ADR-002 confirmed by the build) — Cloud starters carry no `<version>`.
- **The JDK 27 incident:** Arch's rolling release bumped the JDK → Lombok 1.18.46 crashed (it patches javac's private internals); `lombok.version=1.18.48` pinned in the poms. Contrast with .NET: Roslyn source generators use a public API.
