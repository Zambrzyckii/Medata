# Lab 4 — mikroserwisy i Spring Cloud Gateway: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-4.md](../../en/labs/lab-4.md)

## Co powstało

Monolit rozcięty na `services/category` (:8081) i `services/lab-test` (:8082) — każdy z WŁASNĄ bazą H2 i własnym buildem (bez parent-poma); synchronizacja repliki kategorii zdarzeniami REST oraz `services/gateway` (:8080, Spring Cloud Gateway/WebFlux) z trzema trasami — komplet 3 zadań (3+3+2 = 8/8 pkt).
Uruchomienie: trzy terminale, w każdym serwisie `./mvnw spring-boot:run`; cały ruch przez `http://localhost:8080` (`services/gateway/request.http`).

## Sekcje krytyczne — o co mogą zapytać

- **Replika kategorii w lab-test:** tylko `id`+`name` (minimum do relacji i DTO); `@Id` bez losowego defaultu — replika nigdy nie wymyśla id, dostaje je ze zdarzeń/seedu.
- **Zgodność id między bazami bez wspólnej bazy:** stałe UUID-y seedu (`1111…`), zduplikowane w obu serwisach — duplikacja zamiast wspólnej biblioteki (reguła poliglotycznego monorepo).
- **Zdarzenia:** nadaje kontroler category po POST/DELETE (`RestClient`), odbiera `InternalCategoryController`. `PUT` = idempotentny upsert (`orElseGet`), `DELETE` nieistniejącej → 204 — powtórka zdarzenia (at-least-once) nie szkodzi.
- **Co gdy lab-test leży:** operacje na kategoriach dalej 2xx — wysyłka w `try/catch` → WARN (best-effort, eventual consistency). Produkcyjnie: broker + wzorzec outbox; instrukcja jawnie dopuszcza REST.
- **Zmiana nazwy kategorii NIE jest propagowana** — instrukcja wymaga zdarzeń tylko add/remove; luka świadoma (DECISIONS).
- **Kaskada przy zdarzeniu delete:** usunięcie repliki → `CascadeType.REMOVE` + `orphanRemoval` kasują jej badania — „appropriate JPA configuration" z instrukcji.
- **Kolejność tras gatewaya:** `/api/categories/*/tests` (→ lab-test) PRZED `/api/categories/**` (→ category), inaczej catch-all ukradnie żądania o badania; `*` = jeden segment, `**` = dowolnie wiele.
- **`/internal/**` bez trasy** — API zdarzeń niewidoczne zza gatewaya (ochrona przez routing). Diagnostyka: 404 z gatewaya = brak trasy; 500 = trasa jest, cel leży.
- **Czemu gateway na WebFlux/Netty:** przepompowuje bajty — nieblokujące I/O obsłuży tłum połączeń garstką wątków; jedyna reaktywna aplikacja projektu (reszta: klasyczny Tomcat).
- **BOM `spring-cloud-dependencies 2025.1.3`:** import w `dependencyManagement` dobiera wersje starterów Cloud do Boota 4.0.8 (ADR-002 potwierdzony buildem) — startery Cloud bez `<version>`.
- **Incydent JDK 27:** rolling Arch podniósł JDK → krach Lomboka 1.18.46 (grzebie w prywatnych wnętrznościach javac); pin `lombok.version=1.18.48` w pomach. Kontrast z .NET: generatory Roslyna używają publicznego API.
