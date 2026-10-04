# Serwis category

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Źródło prawdy o kategoriach badań (`TestCategory`). Wystawia CRUD REST, a przy utworzeniu i usunięciu kategorii publikuje zdarzenie REST do serwisu `lab-test`, który utrzymuje u siebie replikę. Port **8081**, pakiet `com.medata.category`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8081 (H2 in-memory)
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach serwis startuje z całosystemowego `compose.yaml` w roocie repo (`docker compose up --build`); tutejszy multi-stage `Dockerfile` buduje jar własnym wrapperem serwisu i uruchamia go na JRE Eclipse Temurin 25.

## Konfiguracja

Defaulty w `src/main/resources/application.properties`; każdy klucz można nadpisać zmienną środowiskową (relaxed binding Springa) — i dokładnie to robi `compose.yaml`:

| Właściwość | Default | Nadpisanie env (compose) |
|---|---|---|
| `server.port` | `8081` | — (stały; `EXPOSE 8081`, niepublikowany na host) |
| `spring.datasource.url` | `jdbc:h2:mem:category` | `SPRING_DATASOURCE_URL=jdbc:postgresql://category-db:5432/category` |
| `spring.datasource.username` / `password` | `sa` / puste | `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` |
| `labtest.base-url` | `http://localhost:8082` | `LABTEST_BASE_URL=http://lab-test:8082` |

Nigdzie nie ma `driver-class-name`: Boot wnioskuje sterownik z URL-a JDBC, więc przełączenie H2↔PostgreSQL to wyłącznie URL. `ddl-auto=create-drop` obowiązuje na obu silnikach (schemat + seed per start; migracje wejdą w labie 7).

## Model danych

Jedna tabela `test_categories` (`id` UUID, `name`, `requires_fasting`). Encja **nie ma** kolekcji badań — badania żyją w `lab-test`. Seed: 3 kategorie o stałych UUID-ach (`1111…`, `2222…`, `3333…`).

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

- Zdarzenia publikuje **kontroler** (POST/DELETE), nie serwis — seed nie strzela zbędnymi zdarzeniami (lab-test seeduje się sam dzięki stałym UUID-om).
- Wysyłka best-effort: `CategoryEventPublisher` łapie `RestClientException` i loguje WARN — serwis nie jest zakładnikiem repliki.
