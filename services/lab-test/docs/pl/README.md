# Serwis lab-test

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Właściciel badań laboratoryjnych (`LabTest`) wraz z **repliką** kategorii (`TestCategory`: tylko `id`+`name`), utrzymywaną zdarzeniami z serwisu `category`. Port **8082**, pakiet `com.medata.labtest`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8082 (H2 in-memory)
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach serwis startuje z całosystemowego `compose.yaml` w roocie repo (`docker compose up --build`); tutejszy multi-stage `Dockerfile` buduje jar własnym wrapperem serwisu i uruchamia go na JRE Eclipse Temurin 25.

## Konfiguracja

Defaulty w `src/main/resources/application.properties`; każdy klucz można nadpisać zmienną środowiskową (relaxed binding Springa) — i dokładnie to robi `compose.yaml`:

| Właściwość | Default | Nadpisanie env (compose) |
|---|---|---|
| `server.port` | `8082` | — (stały; `EXPOSE 8082`, niepublikowany na host) |
| `spring.datasource.url` | `jdbc:h2:mem:labtest` | `SPRING_DATASOURCE_URL=jdbc:postgresql://lab-test-db:5432/labtest` |
| `spring.datasource.username` / `password` | `sa` / puste | `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` |

Nigdzie nie ma `driver-class-name`: Boot wnioskuje sterownik z URL-a JDBC. `ddl-auto=create-drop` obowiązuje na obu silnikach (schemat + seed per start; migracje wejdą w labie 7).

## Model danych

`test_categories` (replika: `id`, `name` — **bez** losowego domyślnego id, wartości przychodzą ze zdarzeń/seedu) `1 : N` `lab_tests` (`id`, `name`, `unit`, `reference_min`, `reference_max`, `price`, `category_id` FK). Kaskada `REMOVE` + `orphanRemoval`: usunięcie repliki kasuje jej badania. Seed: repliki pod stałymi UUID-ami (`1111…`, `2222…`, `3333…`) + 6 badań.

## API

| Endpoint | Opis | Kody |
|---|---|---|
| `GET /api/tests` | lista badań (id + nazwa) | 200 |
| `GET /api/tests/{id}` | pełne dane (kategoria spłaszczona do nazwy) | 200, 404 |
| `PUT /api/tests/{id}` | aktualizuje badanie | 204, 400, 404 |
| `DELETE /api/tests/{id}` | usuwa badanie | 204, 404 |
| `GET /api/categories/{categoryId}/tests` | badania kategorii (pusta → 200 `[]`, brak → 404) | 200, 404 |
| `POST /api/categories/{categoryId}/tests` | jedyna droga tworzenia badań | 201, 400, 404 |
| `PUT /internal/categories/{id}` | **zdarzenie**: idempotentny upsert repliki | 204 |
| `DELETE /internal/categories/{id}` | **zdarzenie**: usunięcie repliki (+ kaskada badań) | 204 |

`/internal/**` nie ma trasy w gatewayu — dostępne tylko serwis-do-serwisu. Swagger UI: `http://localhost:8082/swagger-ui.html`.

## Decyzje

- Replika odchudzona do minimum potrzebnego relacji i DTO (`id`+`name`, bez `requiresFasting`).
- Handlery zdarzeń idempotentne: `PUT` = upsert (`orElseGet`), `DELETE` nieistniejącej → 204 — powtórki zdarzeń (at-least-once) są bezpieczne.
