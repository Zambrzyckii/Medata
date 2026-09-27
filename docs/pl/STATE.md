# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-27

## Faza

**Lab 2 UKOŃCZONY** — komplet zadań 1–5 (8/8 pkt) + walidacja danych wejściowych (zlecona przez użytkownika), zweryfikowany testem pełnego cyklu CRUD. Oczekuje na commit z tagiem `lab-2` oraz na utworzenie przez użytkownika pliku CI `.github/workflows/build.yml` (treść podana na czacie).

## Co istnieje

- `catalog/` — aplikacja Spring Boot 4.0.8 (Java 25, Lombok, Spotless): encje JPA `TestCategory`/`LabTest` (H2 in-memory, UUID klienta, tabele mnogie snake_case, relacje lazy), rekord `LabTestDto`, repozytoria (`findAllByCategory`), serwisy (walidacja w `LabTestService.save`), `SampleDataInitializer` (`@Order(1)`), `ConsoleRunner` (`@Order(2)`, komendy help/categories/tests/add/delete/stop)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, **ARCHITECTURE (Mermaid: kontenery + ERD)**, `adr/001–003`, ściągi `labs/lab-1.md`, `labs/lab-2.md`
- Root: `CLAUDE.md`, README ×2 (z badge CI), `.editorconfig` (`[*.java] indent_size = 2`), `.gitignore`
- Repo GitHub: `Zambrzyckii/medata` (main + tag `lab-1`)

## Środowisko deweloperskie

- OpenJDK 26.0.2 (`--release 25`), Maven 3.9.16 + wrapper; kod pisze użytkownik ręcznie (wyjątek: walidacja labu 2 — jawne zlecenie)

## Następny krok

Commit + tag `lab-2` i plik CI (użytkownik). Potem **Lab 3**: Spring MVC REST — osobne DTO (create/update, read, list), kontrolery z pełnym CRUD i hierarchicznymi adresami, poprawne kody HTTP, kaskadowe usuwanie kategorii z badaniami, pliki `request.http`; konwencja labu 3: OpenAPI/springdoc.
