# Serwis category

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Źródło prawdy o kategoriach badań (`TestCategory`). Wystawia CRUD REST, a przy utworzeniu i usunięciu kategorii publikuje zdarzenie REST do serwisu `lab-test`, który utrzymuje u siebie replikę. Port **8081**, pakiet `com.medata.category`, Spring Boot MVC (Tomcat).

## Uruchomienie

```bash
./mvnw spring-boot:run          # start na :8081
./mvnw spotless:apply verify    # format + pełny build
```

## Konfiguracja (`src/main/resources/application.properties`)

| Klucz | Wartość | Po co |
|---|---|---|
| `server.port` | `8081` | stały port serwisu |
| `spring.datasource.url` | `jdbc:h2:mem:category` | prywatna baza in-memory |
| `labtest.base-url` | `http://localhost:8082` | adres odbiorcy zdarzeń (lab 6/7: nadpisze środowisko) |

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
