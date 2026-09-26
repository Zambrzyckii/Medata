# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-26

## Faza

**Lab 1 UKOŃCZONY** — komplet zadań 1–7 (8/8 pkt) zweryfikowany uruchomieniem i `./mvnw verify`. Oczekuje na commit użytkownika z tagiem `lab-1`.

## Co istnieje

- `catalog/` — ukończona aplikacja konsolowa labu 1 (Maven, Java 25, Lombok, Spotless, wrapper `mvnw`): pakiet `model` (`TestCategory`, `LabTest`, helper `addLabTest`), pakiet `dto` (rekord `LabTestDto`), `Main` z zadaniami 2–7 (dane przykładowe, wydruk `forEach`, trzy pipeline'y Stream API, serializacja do `categories.bin`, równoległość na własnym `ForkJoinPool` z pomiarem czasu)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG oraz ściąga do prezentacji `labs/lab-1.md`
- Root: `CLAUDE.md`, `README.md`/`README.pl.md`, `.editorconfig`, `.gitignore`
- Instrukcje labów (PDF) w katalogu nadrzędnym `/home/bob/Mikro`

## Środowisko deweloperskie

- OpenJDK 26.0.2 (kompilacja `--release 25` — LTS), Maven 3.9.16 + wrapper w repo
- Git: gałąź `main`, commit fundamentu dokumentacji wykonany; kod pisze wyłącznie użytkownik ręcznie (agent pokazuje go na czacie)

## Następny krok

Commit + tag `lab-1` (wykonuje użytkownik; opcjonalnie publikacja repo na GitHubie przez `gh repo create`). Potem **Lab 2**: Spring Boot + Spring Data JPA (H2 in-memory) — encje JPA, repozytoria, serwisy, konsolowy runner CRUD; konwencje aktywowane w labie 2: `ARCHITECTURE.md` z diagramami Mermaid, pierwsze ADR-y, CI (GitHub Actions).
