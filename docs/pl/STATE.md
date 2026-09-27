# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-27

## Faza

**Lab 3 UKOŃCZONY** — komplet zadań 1–3 (8/8 pkt) zweryfikowany buildem, smoke testem API (200/201/204/400/404, kaskada) i springdociem. Oczekuje na commit z tagiem `lab-3`.

## Co istnieje

- `catalog/` — REST API na Spring Boot 4.0.8: encje JPA z kaskadą usuwania, 6 DTO (create/update, read, list × 2 encje), 2 kontrolery `@RestController` (hierarchiczne adresy, tabela endpointów w ARCHITECTURE.md), `GlobalExceptionHandler` (walidacja → 400), `SampleDataInitializer`, springdoc/Swagger UI, `request.http` (17 żądań)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (kontenery + **tabela API** + ERD), `adr/001–003`, ściągi `labs/lab-1..3.md`
- Root: `CLAUDE.md`, README ×2 (badge CI), `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (CI zielone)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`, `lab-2`)

## Środowisko deweloperskie

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; kod pisze użytkownik ręcznie (wyjątek: walidacja labu 2 — jawne zlecenie)
- IDE: IntelliJ IDEA **Community** — `request.http` odpalane przez VS Code REST Client (HTTP Client wymaga Ultimate); zasugerowana darmowa licencja studencka JetBrains

## Następny krok

Commit + tag `lab-3` (użytkownik). Potem **Lab 4 — mikroserwisy**: podział na serwis kategorii i serwis badań (prywatne bazy, uproszczona replika kategorii), komunikacja zdarzeniowa REST przy dodaniu/usunięciu kategorii, Spring Cloud Gateway z routingiem, aktualizacja `request.http` na port gatewaya. Konwencje labu 4: szablon dokumentacji modułu + diagram sekwencji zdarzeń. Uwaga: zweryfikować parowanie Spring Cloud `2025.1.x` ↔ Boot 4.0 (ADR-002).
