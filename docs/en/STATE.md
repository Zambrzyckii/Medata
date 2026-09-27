# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-27

## Phase

**Lab 2 COMPLETE** — all tasks 1–5 (8/8 pts) + input validation (requested by the user), verified with a full CRUD-cycle test. Awaiting the commit with the `lab-2` tag and the user creating the CI file `.github/workflows/build.yml` (content provided in chat).

## What exists

- `catalog/` — Spring Boot 4.0.8 app (Java 25, Lombok, Spotless): JPA entities `TestCategory`/`LabTest` (in-memory H2, client UUIDs, plural snake_case tables, lazy relations), record `LabTestDto`, repositories (`findAllByCategory`), services (validation in `LabTestService.save`), `SampleDataInitializer` (`@Order(1)`), `ConsoleRunner` (`@Order(2)`, commands help/categories/tests/add/delete/stop)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, **ARCHITECTURE (Mermaid: containers + ERD)**, `adr/001–003`, cheat sheets `labs/lab-1.md`, `labs/lab-2.md`
- Root: `CLAUDE.md`, README ×2 (with CI badge), `.editorconfig` (`[*.java] indent_size = 2`), `.gitignore`
- GitHub repo: `Zambrzyckii/medata` (main + tag `lab-1`)

## Development environment

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; code is typed by the user by hand (exception: lab 2 validation — explicit delegation)

## Next step

Commit + tag `lab-2` and the CI file (user). Then **Lab 3**: Spring MVC REST — separate DTOs (create/update, read, list), controllers with full CRUD and hierarchical URLs, correct HTTP codes, cascade delete of a category with its tests, `request.http` files; lab 3 convention: OpenAPI/springdoc.
