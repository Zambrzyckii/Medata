# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-09-26

## Faza

Planowanie zakończone. **Kod jeszcze nie istnieje.** Repozytorium git niezainicjalizowane — zrobi to użytkownik przy starcie Labu 1.

## Co istnieje

- `docs/pl/` i `docs/en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG (układ 1:1)
- Root `CLAUDE.md`
- Root `README.md` (EN) i `README.pl.md` oraz `.editorconfig`
- Instrukcje labów (PDF) w katalogu nadrzędnym `/home/bob/Mikro`

## Środowisko deweloperskie

- OpenJDK 26.0.2 zainstalowane
- **Maven brak — bloker Labu 1** (propozycja: `sudo pacman -S maven`, potem Maven Wrapper w repo)
- Gradle brak (niepotrzebny — laby wymagają Mavena)

## Następny krok

Lab 1 (sekcja CORE wizji): katalog badań jako aplikacja konsolowa Java SE — `pom.xml` → encje → DTO → dane startowe → pipeline'y Stream API → serializacja → ForkJoinPool.
