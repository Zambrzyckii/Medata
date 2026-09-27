# Medata — historia zmian

> Wersja polska. Angielski odpowiednik 1:1: [../en/CHANGELOG.md](../en/CHANGELOG.md)
> Wpisy na poziomie kamieni milowych, najnowsze na górze.

- **2026-09-27** — **Lab 2 ukończony (8/8 pkt)**: `catalog` przebudowany na Spring Boot 4.0.8 — encje JPA (H2 in-memory, UUID klienta), repozytoria, serwisy z walidacją danych wejściowych, initializer, konsolowy runner CRUD. Aktywowane konwencje labu 2: `ARCHITECTURE.md` (Mermaid: kontenery + ERD), ADR-y 001–003, CI GitHub Actions. Ściąga: `labs/lab-2.md`.

- **2026-09-26** — **Lab 1 ukończony (8/8 pkt)**: aplikacja konsolowa `catalog/` (Java SE, Maven, Lombok, Spotless, wrapper) — encje z builderem i porównywaniem, rekord DTO, dane przykładowe, trzy pipeline'y Stream API, serializacja binarna, równoległość na własnym `ForkJoinPool`. Kod przepisany ręcznie przez użytkownika; ściąga do prezentacji: `labs/lab-1.md`.

- **2026-09-26** — Przyjęto pakiet konwencji rozwijalności (README EN+PL, `.editorconfig`, checklista granicy labu, plan aktywacji konwencji per lab); rozbudowano `CONVENTIONS.md` o sekcje 5–8.

- **2026-09-26** — Powstała struktura dokumentacji (`docs/pl`, `docs/en`), dokument wizji (CORE + FUTURE), konwencje pracy, rejestr decyzji i root `CLAUDE.md`. Ustalona nazwa projektu: **Medata**.
- **2026-08-19** — Wybrana domena projektu: laboratorium diagnostyczne (LIS).
