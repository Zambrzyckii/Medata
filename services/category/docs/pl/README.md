# Serwis category

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Źródło prawdy o kategoriach badań (`TestCategory`). Wystawia CRUD REST, a przy utworzeniu i usunięciu kategorii publikuje zdarzenie REST do serwisu `lab-test`, który utrzymuje u siebie replikę. Port **8081**, pakiet `com.medata.category`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8081 (H2 in-memory)
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach serwis startuje z całosystemowego `compose.yaml` w roocie repo (`docker compose up --build`); tutejszy multi-stage `Dockerfile` buduje jar własnym wrapperem serwisu i uruchamia go na JRE Eclipse Temurin 25. Tryb deweloperski zakłada działające discovery (:8761). Bez niego aplikacja działa, ale loguje WARN-y rejestracji. Config-serwer jest w dev opcjonalny dzięki importowi `optional:`.

## Konfiguracja

Lokalne `application.properties` trzyma defaulty deweloperskie. Pod compose konfiguracja bazy i discovery przychodzi z **config-serwisu**, a środowisko niesie tylko to, czego wymaga zadanie 5 labu 7:

| Ustawienie | Default dev (plik lokalny) | Pod compose |
|---|---|---|
| `server.port` | `8081` | bez zmian (`EXPOSE 8081`, niepublikowany na host) |
| `spring.config.import` | `optional:configserver:` z fallbackiem `http://localhost:8888` | `CONFIG_URL=http://config:8888` |
| `spring.datasource.*` | H2 in-memory | PostgreSQL, serwowany przez config-serwis |
| `eureka.client.service-url.defaultZone` | `http://localhost:8761/eureka` (fallback `DISCOVERY_URL`) | serwowany przez config-serwis |
| `eureka.instance.instance-id` | `category-local` | `INSTANCE_ID=category-1` |
| `labtest.base-url` | `http://lab-test` | ta sama wartość, nazwa logiczna rozwiązywana przez rejestr |

Pierwszeństwo idzie od env przez config-serwer po plik lokalny. Boot wnioskuje sterownik JDBC z URL-a. Właścicielem schematu jest Flyway (`db/migration/V1__init.sql` + `V2__seed.sql`) przy `ddl-auto=validate`, więc Hibernate schemat tylko sprawdza.

## Model danych

Jedna tabela `test_categories` (`id` UUID, `name`, `requires_fasting`). Encja **nie ma** kolekcji badań — badania żyją w `lab-test`. Schemat i seed żyją w migracjach Flywaya, z 3 kategoriami seedu pod stałymi UUID-ami (`1111…`, `2222…`, `3333…`).

## API

| Endpoint | Opis | Kody |
|---|---|---|
| `GET /api/categories` | lista (id + nazwa) | 200 |
| `POST /api/categories` | tworzy + **zdarzenie upsert** do lab-test | 201 |
| `GET /api/categories/{id}` | pełne dane kategorii | 200, 404 |
| `PUT /api/categories/{id}` | aktualizuje (bez zdarzenia — świadoma luka) | 204, 404 |
| `DELETE /api/categories/{id}` | usuwa + **zdarzenie delete** do lab-test | 204, 404 |

Swagger UI: `http://localhost:8081/swagger-ui.html`.

## Decyzje

- Zdarzenia publikuje **kontroler** (POST/DELETE), nie serwis. Seed to czysty SQL w migracji, więc nie strzela zdarzeniami (lab-test seeduje się sam dzięki stałym UUID-om).
- Wysyłka pozostaje best-effort. `CategoryEventPublisher` używa `@LoadBalanced` `RestClient` (zwykły builder jest `@Primary`, więc transport Eureki nie jest przechwytywany) i łapie `RestClientException` oraz `IllegalStateException` (brak znanej instancji), logując WARN. Serwis nigdy nie jest zakładnikiem repliki.
