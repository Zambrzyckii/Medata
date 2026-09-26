# Medata — Current State

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-09-26

## Phase

Planning finished. **No code exists yet.** The git repository is not initialized — the user will do it when Lab 1 starts.

## What exists

- `docs/pl/` and `docs/en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG (1:1 layout)
- Root `CLAUDE.md`
- Root `README.md` (EN) and `README.pl.md`, plus `.editorconfig`
- Lab instructions (PDF) in the parent directory `/home/bob/Mikro`

## Development environment

- OpenJDK 26.0.2 installed
- **Maven missing — Lab 1 blocker** (proposal: `sudo pacman -S maven`, then a Maven Wrapper in the repo)
- Gradle missing (not needed — the labs require Maven)

## Next step

Lab 1 (CORE section of the vision): the test catalog as a Java SE console application — `pom.xml` → entities → DTO → seed data → Stream API pipelines → serialization → ForkJoinPool.
