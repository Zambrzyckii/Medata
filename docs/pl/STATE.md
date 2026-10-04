# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-10-04

## Faza

**Lab 5 UKOŃCZONY (9/9 pkt), oczekuje na commit + tag `lab-5`.** Pełny stos: frontend Angular 22 (`web/catalog`, 7 routowanych widoków CRUD, dev :4200) → gateway (:8080) → `category` (:8081) i `lab-test` (:8082) z synchronizacją repliki zdarzeniami REST. Cała pętla (listy, formularze z prepopulowaniem, usuwanie z kaskadą przez zdarzenie) zweryfikowana w przeglądarce.

## Co istnieje

- `services/category` (:8081), `services/lab-test` (:8082), `services/gateway` (:8080) — stan z labu 4 (zdarzenia, replika, trasy), bez zmian
- `web/catalog` — Angular 22 standalone/signals/zoneless: `models.ts` (lustrzane DTO), `Api` (`HttpClient`, względny `/api`), 7 komponentów-widoków, typed reactive forms, dev-proxy → :8080; ESLint + Prettier
- Per moduł: `docs/{pl,en}/README.md` + `CLAUDE.md` (4 moduły: 3 serwisy + frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (4 kontenery + sekwencja zdarzeń + ERD), `adr/001–003`, ściągi `labs/lab-1..5.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (matrix 3 serwisów + job frontendu)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`–`lab-4`; `lab-5` po commicie)

## Środowisko deweloperskie

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pin), Maven 3.9.16 + wrapper per serwis
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; kod pisze użytkownik ręcznie (szkielety: `ng new`/delegacje na jawne zlecenie)
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); uwaga: wtyczka Javy w VS Code kompiluje ECJ-em do `target/` — po dziwnych błędach startu `./mvnw clean`

## Następny krok

Commit + tag `lab-5` (wykonuje użytkownik). Potem **lab 6 (9 pkt): konteneryzacja** — Dockerfile per serwis (Eclipse Temurin, konfiguracja przez env), obraz frontendu na NGINX (build Angulara + proxy `/api` konfigurowane zmiennymi środowiskowymi), `docker compose up` spinający całość; opcjonalnie (+2 pkt w ramach 9) zewnętrzne bazy danych. Konwencje labu 6: `.env.example`, pełna zasada jednej komendy. Do sprawdzenia: `docker` i `docker compose` w środowisku.
