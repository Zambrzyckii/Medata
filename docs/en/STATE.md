# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-26

## Phase

**Lab 2 in progress** (lab 1 closed with a commit and the `lab-1` tag, repo on GitHub: `Zambrzyckii/medata`). Batches 1–4/6 done: rebuild on Spring Boot 4.0.8, JPA entities (plural snake_case tables, lazy relations, client UUIDs), `@Repository` repositories (incl. `findAllByCategory`) and `@Service` services with constructor injection. `.editorconfig` gained a `[*.java] indent_size = 2` override (google-java-format compliance).

## What exists

- `catalog/` — the finished lab 1 console application (Maven, Java 25, Lombok, Spotless, `mvnw` wrapper): `model` package (`TestCategory`, `LabTest`, `addLabTest` helper), `dto` package (record `LabTestDto`), `Main` with tasks 2–7 (sample data, `forEach` printout, three Stream API pipelines, serialization to `categories.bin`, parallelism on a custom `ForkJoinPool` with timing)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG plus the presentation cheat sheet `labs/lab-1.md`
- Root: `CLAUDE.md`, `README.md`/`README.pl.md`, `.editorconfig`, `.gitignore`
- Lab instructions (PDF) in the parent directory `/home/bob/Mikro`

## Development environment

- OpenJDK 26.0.2 (compiled with `--release 25` — LTS), Maven 3.9.16 + wrapper in the repo
- Git: branch `main`, documentation-foundation commit done; source code is typed exclusively by the user by hand (the agent shows it in chat)

## Next step

Lab 2, batch 5/6 (task 4): sample data initializer as a `@Component`/`CommandLineRunner` with `@Order(1)`. Then: CRUD runner (task 5); at the lab boundary: `ARCHITECTURE.md` with Mermaid, first ADRs, CI (GitHub Actions), `labs/lab-2.md` cheat sheet.
