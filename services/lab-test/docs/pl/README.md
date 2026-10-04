# Serwis lab-test

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Właściciel badań laboratoryjnych (`LabTest`) wraz z **repliką** kategorii (`TestCategory`: tylko `id`+`name`), utrzymywaną zdarzeniami z serwisu `category`. Port **8082**, pakiet `com.medata.labtest`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # start na :8082
./mvnw spotless:apply verify    # format + pełny build
```

## Konfiguracja (`src/main/resources/application.properties`)

| Klucz | Wartość | Po co |
|---|---|---|
| `server.port` | `8082` | stały port serwisu |
| `spring.datasource.url` | `jdbc:h2:mem:labtest` | prywatna baza in-memory |

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
