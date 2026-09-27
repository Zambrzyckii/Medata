# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-27

## Faza

**Lab 4 w toku** — struktura monorepo przestawiona na `services/`: monolit `catalog` podzielony na `services/category` (8081, tylko kategorie, encja bez kolekcji badań) i `services/lab-test` (8082, badania + pełna jeszcze kopia kategorii do odchudzenia), `request.http` przeniesiony do `services/gateway/`. Oba serwisy budują się i działają równocześnie (zweryfikowane). Restrukturyzację plików wykonał Claude na jawne zlecenie użytkownika.

## Co istnieje

- `catalog/` — REST API na Spring Boot 4.0.8: encje JPA z kaskadą usuwania, 6 DTO (create/update, read, list × 2 encje), 2 kontrolery `@RestController` (hierarchiczne adresy, tabela endpointów w ARCHITECTURE.md), `GlobalExceptionHandler` (walidacja → 400), `SampleDataInitializer`, springdoc/Swagger UI, `request.http` (17 żądań)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (kontenery + **tabela API** + ERD), `adr/001–003`, ściągi `labs/lab-1..3.md`
- Root: `CLAUDE.md`, README ×2 (badge CI), `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (CI zielone)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`, `lab-2`)

## Środowisko deweloperskie

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; kod pisze użytkownik ręcznie (wyjątek: walidacja labu 2 — jawne zlecenie)
- IDE: IntelliJ IDEA **Community** — `request.http` odpalane przez VS Code REST Client (HTTP Client wymaga Ultimate); zasugerowana darmowa licencja studencka JetBrains

## Następny krok

Lab 4, partia 1: odchudzenie repliki kategorii w `lab-test` (encja `id`+`name`) + stałe UUID-y w initializerach obu serwisów. Dalej: wewnętrzne endpointy zdarzeń → nadawanie zdarzeń `RestClient`em → projekt gatewaya (weryfikacja ADR-002!) → testy całości, nowy workflow CI (matrix), szablon docs modułów i granica labu.
