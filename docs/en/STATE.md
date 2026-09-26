# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-26

## Phase

**Lab 1 COMPLETE** — all tasks 1–7 (8/8 pts) verified by running the app and `./mvnw verify`. Awaiting the user's commit with the `lab-1` tag.

## What exists

- `catalog/` — the finished lab 1 console application (Maven, Java 25, Lombok, Spotless, `mvnw` wrapper): `model` package (`TestCategory`, `LabTest`, `addLabTest` helper), `dto` package (record `LabTestDto`), `Main` with tasks 2–7 (sample data, `forEach` printout, three Stream API pipelines, serialization to `categories.bin`, parallelism on a custom `ForkJoinPool` with timing)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG plus the presentation cheat sheet `labs/lab-1.md`
- Root: `CLAUDE.md`, `README.md`/`README.pl.md`, `.editorconfig`, `.gitignore`
- Lab instructions (PDF) in the parent directory `/home/bob/Mikro`

## Development environment

- OpenJDK 26.0.2 (compiled with `--release 25` — LTS), Maven 3.9.16 + wrapper in the repo
- Git: branch `main`, documentation-foundation commit done; source code is typed exclusively by the user by hand (the agent shows it in chat)

## Next step

Commit + tag `lab-1` (done by the user; optionally publish the repo to GitHub via `gh repo create`). Then **Lab 2**: Spring Boot + Spring Data JPA (H2 in-memory) — JPA entities, repositories, services, a console CRUD runner; conventions activating in lab 2: `ARCHITECTURE.md` with Mermaid diagrams, first ADRs, CI (GitHub Actions).
