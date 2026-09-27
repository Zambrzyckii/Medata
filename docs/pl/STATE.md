# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-27

## Faza

**Lab 3 w toku** (lab 2 zamknięty tagiem `lab-2`, CI na GitHub Actions zielone). Ukończone partie 1–2/6: starter web (Tomcat na 8080, `ConsoleRunner` usunięty) oraz kaskada usuwania (`CascadeType.REMOVE` + `orphanRemoval`) i trzy DTO kategorii (create/update, read, list).

## Co istnieje

- `catalog/` — aplikacja Spring Boot 4.0.8 (Java 25, Lombok, Spotless): encje JPA `TestCategory`/`LabTest` (H2 in-memory, UUID klienta, tabele mnogie snake_case, relacje lazy), rekord `LabTestDto`, repozytoria (`findAllByCategory`), serwisy (walidacja w `LabTestService.save`), `SampleDataInitializer` (`@Order(1)`), `ConsoleRunner` (`@Order(2)`, komendy help/categories/tests/add/delete/stop)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, **ARCHITECTURE (Mermaid: kontenery + ERD)**, `adr/001–003`, ściągi `labs/lab-1.md`, `labs/lab-2.md`
- Root: `CLAUDE.md`, README ×2 (z badge CI), `.editorconfig` (`[*.java] indent_size = 2`), `.gitignore`
- Repo GitHub: `Zambrzyckii/medata` (main + tag `lab-1`)

## Środowisko deweloperskie

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; kod pisze użytkownik ręcznie (wyjątek: walidacja labu 2 — jawne zlecenie)

## Następny krok

Lab 3, partia 3/6: trzy DTO badań (przebudowa `LabTestDto` z labu 1 na `LabTestReadDto` z id). Dalej: kontroler kategorii → kontroler badań (+ obsługa 404/400) → `request.http` + springdoc i granica labu.
