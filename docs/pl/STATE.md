# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-26

## Faza

**Lab 2 w toku** (lab 1 zamknięty commitem i tagiem `lab-1`, repo na GitHubie: `Zambrzyckii/medata`). Ukończone partie 1–4/6: przebudowa na Spring Boot 4.0.8, encje JPA (tabele mnogie snake_case, relacje lazy, UUID klienta), repozytoria `@Repository` (w tym `findAllByCategory`) i serwisy `@Service` z wstrzykiwaniem przez konstruktor. W `.editorconfig` dopisany wyjątek `[*.java] indent_size = 2` (zgodność z google-java-format).

## Co istnieje

- `catalog/` — ukończona aplikacja konsolowa labu 1 (Maven, Java 25, Lombok, Spotless, wrapper `mvnw`): pakiet `model` (`TestCategory`, `LabTest`, helper `addLabTest`), pakiet `dto` (rekord `LabTestDto`), `Main` z zadaniami 2–7 (dane przykładowe, wydruk `forEach`, trzy pipeline'y Stream API, serializacja do `categories.bin`, równoległość na własnym `ForkJoinPool` z pomiarem czasu)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG oraz ściąga do prezentacji `labs/lab-1.md`
- Root: `CLAUDE.md`, `README.md`/`README.pl.md`, `.editorconfig`, `.gitignore`
- Instrukcje labów (PDF) w katalogu nadrzędnym `/home/bob/Mikro`

## Środowisko deweloperskie

- OpenJDK 26.0.2 (kompilacja `--release 25` — LTS), Maven 3.9.16 + wrapper w repo
- Git: gałąź `main`, commit fundamentu dokumentacji wykonany; kod pisze wyłącznie użytkownik ręcznie (agent pokazuje go na czacie)

## Następny krok

Lab 2, partia 5/6 (zadanie 4): initializer danych przykładowych jako `@Component`/`CommandLineRunner` z `@Order(1)`. Dalej: runner CRUD (zad. 5); na granicy labu: `ARCHITECTURE.md` z Mermaid, pierwsze ADR-y, CI (GitHub Actions), ściąga `labs/lab-2.md`.
