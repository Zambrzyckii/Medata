# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-10-04

## Faza

**Lab 4 UKOŃCZONY (8/8 pkt), oczekuje na commit + tag `lab-4`.** System to trzy niezależne aplikacje: `services/category` (:8081) i `services/lab-test` (:8082) z prywatnymi H2 i repliką kategorii synchronizowaną zdarzeniami REST (idempotentny upsert, best-effort z WARN, kaskada badań) oraz `services/gateway` (:8080, Spring Cloud Gateway WebFlux) — jedyne publiczne wejście; `/internal/**` bez trasy. Całość zweryfikowana e2e przez :8080, łącznie z testem odporności na leżący lab-test.

## Co istnieje

- `services/category` (:8081) — kategorie: CRUD REST, `GlobalExceptionHandler`, springdoc, seed o stałych UUID-ach; `CategoryEventPublisher` (`RestClient`, zdarzenia przy POST/DELETE)
- `services/lab-test` (:8082) — badania + replika (`id`+`name`): pełny stos LabTest, `PUT`/`DELETE /internal/categories/{id}` (idempotentne), kaskada, springdoc, seed pod stałymi UUID-ami
- `services/gateway` (:8080) — trasy `Path=` od najszczegółowszej (tabela w `docs/`), `request.http` całego systemu
- Per serwis: `docs/{pl,en}/README.md` (szablon modułu) + `CLAUDE.md` + własny wrapper Mavena
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (3 kontenery + sekwencja zdarzeń + ERD dwóch baz), `adr/001–003` (ADR-002 zweryfikowany), ściągi `labs/lab-1..4.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (matrix `category`/`lab-test`/`gateway`)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`–`lab-3`; `lab-4` po commicie)

## Środowisko deweloperskie

- OpenJDK 27 (`--release 25`; rolling-aktualizacja Archa z 3.10), Lombok 1.18.48 (pin w pomach, wsparcie JDK 27), Maven 3.9.16 + wrapper per serwis; kod pisze użytkownik ręcznie (wyjątki na jawne zlecenie: walidacja labu 2, restrukturyzacja `services/`, fix seedu partii 1)
- IDE: IntelliJ IDEA **Community** (Project SDK: java-27-openjdk) — `request.http` przez VS Code REST Client; zasugerowana darmowa licencja studencka JetBrains

## Następny krok

Commit + tag `lab-4` (wykonuje użytkownik). Potem **lab 5 (9 pkt): frontend Angular** — 7 widoków (listy, formularze, szczegóły kategorii i badań) z routingiem, cały ruch przez gateway (:8080); nowy katalog `web/`, aktywacja konwencji ESLint + Prettier; do sprawdzenia: Node.js w środowisku.
