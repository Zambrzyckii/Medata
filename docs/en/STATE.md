# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-27

## Phase

**Lab 3 in progress** (lab 2 closed with the `lab-2` tag, GitHub Actions CI green). Batches 1–2/6 done: web starter (Tomcat on 8080, `ConsoleRunner` deleted) plus delete cascade (`CascadeType.REMOVE` + `orphanRemoval`) and three category DTOs (create/update, read, list).

## What exists

- `catalog/` — Spring Boot 4.0.8 app (Java 25, Lombok, Spotless): JPA entities `TestCategory`/`LabTest` (in-memory H2, client UUIDs, plural snake_case tables, lazy relations), record `LabTestDto`, repositories (`findAllByCategory`), services (validation in `LabTestService.save`), `SampleDataInitializer` (`@Order(1)`), `ConsoleRunner` (`@Order(2)`, commands help/categories/tests/add/delete/stop)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, **ARCHITECTURE (Mermaid: containers + ERD)**, `adr/001–003`, cheat sheets `labs/lab-1.md`, `labs/lab-2.md`
- Root: `CLAUDE.md`, README ×2 (with CI badge), `.editorconfig` (`[*.java] indent_size = 2`), `.gitignore`
- GitHub repo: `Zambrzyckii/medata` (main + tag `lab-1`)

## Development environment

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; code is typed by the user by hand (exception: lab 2 validation — explicit delegation)

## Next step

Lab 3, batch 3/6: three lab-test DTOs (reworking lab 1's `LabTestDto` into `LabTestReadDto` with an id). Then: category controller → test controller (+ 404/400 handling) → `request.http` + springdoc and the lab boundary.
