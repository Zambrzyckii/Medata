# Medata — historia zmian

> Wersja polska. Angielski odpowiednik 1:1: [../en/CHANGELOG.md](../en/CHANGELOG.md)
> Wpisy na poziomie kamieni milowych, najnowsze na górze.

- **2026-10-04** — **Lab 5 ukończony (9/9 pkt)**: frontend Angular 22 w `web/catalog` (standalone, signals, zoneless, typed reactive forms z prepopulowaniem) — 7 routowanych widoków CRUD kategorii i badań, cały ruch przez gateway (dev-proxy `/api` → :8080), kolejność tras statyczne-przed-dynamicznymi. Aktywowana konwencja labu 5: ESLint + Prettier; nowy job frontendu w CI. Ściąga: `labs/lab-5.md`.

- **2026-10-04** — **Lab 4 ukończony (8/8 pkt)**: monolit podzielony na `services/category` (:8081) i `services/lab-test` (:8082) z prywatnymi H2 i repliką kategorii (`id`+`name`); zdarzenia REST add/remove (`PUT`/`DELETE /internal/categories/{id}`, idempotentny upsert, best-effort z WARN); `services/gateway` (:8080, Spring Cloud Gateway WebFlux, BOM 2025.1.3) z trasami od najszczegółowszej i `/internal` bez trasy; CI matrix × 3 serwisy; po aktualizacji systemu do JDK 27 Lombok przypięty na 1.18.48. Aktywowana konwencja labu 4: docs modułu + `CLAUDE.md` per serwis, diagram sekwencji zdarzeń w ARCHITECTURE. Ściąga: `labs/lab-4.md`.

- **2026-09-27** — **Lab 3 ukończony (8/8 pkt)**: REST API — 6 DTO (create/update, read, list per encja), kontrolery z hierarchicznymi adresami i kodami 200/201/204/400/404, rozróżnienie pustej i nieistniejącej kategorii, kaskadowe usuwanie, `GlobalExceptionHandler` (walidacja → 400), `request.http` (17 żądań), springdoc/Swagger UI. `ConsoleRunner` usunięty (żyje w tagu `lab-2`). Ściąga: `labs/lab-3.md`.

- **2026-09-27** — **Lab 2 ukończony (8/8 pkt)**: `catalog` przebudowany na Spring Boot 4.0.8 — encje JPA (H2 in-memory, UUID klienta), repozytoria, serwisy z walidacją danych wejściowych, initializer, konsolowy runner CRUD. Aktywowane konwencje labu 2: `ARCHITECTURE.md` (Mermaid: kontenery + ERD), ADR-y 001–003, CI GitHub Actions. Ściąga: `labs/lab-2.md`.

- **2026-09-26** — **Lab 1 ukończony (8/8 pkt)**: aplikacja konsolowa `catalog/` (Java SE, Maven, Lombok, Spotless, wrapper) — encje z builderem i porównywaniem, rekord DTO, dane przykładowe, trzy pipeline'y Stream API, serializacja binarna, równoległość na własnym `ForkJoinPool`. Kod przepisany ręcznie przez użytkownika; ściąga do prezentacji: `labs/lab-1.md`.

- **2026-09-26** — Przyjęto pakiet konwencji rozwijalności (README EN+PL, `.editorconfig`, checklista granicy labu, plan aktywacji konwencji per lab); rozbudowano `CONVENTIONS.md` o sekcje 5–8.

- **2026-09-26** — Powstała struktura dokumentacji (`docs/pl`, `docs/en`), dokument wizji (CORE + FUTURE), konwencje pracy, rejestr decyzji i root `CLAUDE.md`. Ustalona nazwa projektu: **Medata**.
- **2026-08-19** — Wybrana domena projektu: laboratorium diagnostyczne (LIS).
