# Serwis lab-test

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Właściciel badań laboratoryjnych (`LabTest`) wraz z **repliką** kategorii (`TestCategory`: tylko `id`+`name`), utrzymywaną zdarzeniami z serwisu `category`. Port **8082**, pakiet `com.medata.labtest`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8082 (H2 in-memory)
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach rootowy `compose.yaml` uruchamia **dwie równorzędne instancje** tego obrazu (`lab-test-1`, `lab-test-2`) na jednej wspólnej bazie, a gateway balansuje między nimi żądania. Tryb deweloperski uruchamia jedną instancję i zakłada działające discovery na :8761. Config-serwer jest w dev opcjonalny dzięki importowi `optional:`.

## Konfiguracja

Lokalne `application.properties` trzyma defaulty deweloperskie. Pod compose konfiguracja bazy i discovery przychodzi z **config-serwisu**. Obie instancje pytają go o tę samą konfigurację `lab-test` i właśnie to trzyma je na jednej bazie:

| Ustawienie | Default dev (plik lokalny) | Pod compose |
|---|---|---|
| `server.port` | `8082` | bez zmian (`EXPOSE 8082`, niepublikowany na host) |
| `spring.config.import` | `optional:configserver:` z fallbackiem `http://localhost:8888` | `CONFIG_URL=http://config:8888` |
| `spring.datasource.*` | H2 in-memory | PostgreSQL, serwowany przez config-serwis |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka` (fallback `DISCOVERY_URL`) | serwowany przez config-serwis |
| `eureka.instance.instance-id` | `lab-test-local` | `INSTANCE_ID=lab-test-1` oraz `lab-test-2` |

Pierwszeństwo idzie od env przez config-serwer po plik lokalny. Właścicielem schematu jest Flyway (`db/migration/V1__init.sql` + `V2__seed.sql`) przy `ddl-auto=validate`. Na wspólnej bazie migracje aplikuje tylko jedna instancja, druga czeka na blokadzie Flywaya i odpuszcza.

## Model danych

`test_categories` (replika: `id`, `name` — **bez** losowego domyślnego id, wartości przychodzą ze zdarzeń/seedu) `1 : N` `lab_tests` (`id`, `name`, `unit`, `reference_min`, `reference_max`, `price`, `category_id` FK). Kaskada `REMOVE` + `orphanRemoval`: usunięcie repliki kasuje jej badania. Schemat i seed żyją w migracjach Flywaya. Repliki zachowują stałe UUID-y (`1111…`, `2222…`, `3333…`), a 6 badań seedu ma stałe idki `aaaaaaaa-…-000N`, więc ponowny seed nigdy ich nie zduplikuje.

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
