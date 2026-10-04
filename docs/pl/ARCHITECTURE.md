# Medata — architektura

> Wersja polska. Angielski odpowiednik 1:1: [../en/ARCHITECTURE.md](../en/ARCHITECTURE.md)
> Stan na: lab 4 (mikroserwisy + gateway). Konwencja §6: zmiana architektury bez aktualizacji diagramu = zmiana nieukończona.

## Widok kontenerów

```mermaid
flowchart LR
    Client((Klient HTTP - przegladarka, request.http, curl)) -->|"JSON :8080"| GW
    subgraph gw [gateway - Spring Cloud Gateway WebFlux, Netty :8080]
        GW[trasy Path= od najszczegolowszej]
    end
    GW -->|"/api/categories/**"| CATC
    GW -->|"/api/categories/*/tests oraz /api/tests/**"| LTC
    subgraph cat [category - Spring Boot MVC, Tomcat :8081]
        CATC[TestCategoryController] --> CATS[TestCategoryService] --> CATDB[(H2 in-mem: category)]
        CATC --> PUB[CategoryEventPublisher - RestClient]
    end
    subgraph lt [lab-test - Spring Boot MVC, Tomcat :8082]
        LTC[LabTestController + InternalCategoryController] --> LTS[LabTestService / TestCategoryService] --> LTDB[(H2 in-mem: labtest)]
    end
    PUB -. "zdarzenia PUT/DELETE /internal/categories/id" .-> LTC
```

Każdy serwis ma **prywatną** bazę H2 — nie istnieje wspólny schemat ani klucz obcy między serwisami. Spójność utrzymują zdarzenia REST i stałe UUID-y seedu.

## Przepływ zdarzeń (synchronizacja repliki)

```mermaid
sequenceDiagram
    participant K as Klient
    participant G as gateway :8080
    participant C as category :8081
    participant L as lab-test :8082
    K->>G: POST /api/categories {name,...}
    G->>C: POST /api/categories
    C->>C: INSERT do H2 category
    C-)L: PUT /internal/categories/{id} {name}
    L->>L: idempotentny upsert repliki
    C-->>G: 201 Created
    G-->>K: 201 Created
    K->>G: DELETE /api/categories/{id}
    G->>C: DELETE /api/categories/{id}
    C->>C: DELETE z H2 category
    C-)L: DELETE /internal/categories/{id}
    L->>L: usuniecie repliki + kaskada badan
    C-->>G: 204 No Content
    G-->>K: 204 No Content
```

Wysyłka jest **best-effort**: błąd sieci nie psuje operacji na kategorii (`try/catch` → WARN w logu). Handlery w `lab-test` są **idempotentne** (PUT = upsert, DELETE nieistniejącej → 204), więc powtórzone zdarzenie nie szkodzi. Zmiana nazwy kategorii NIE jest propagowana (świadoma luka — instrukcja wymaga zdarzeń tylko przy dodaniu/usunięciu).

## Routing (gateway)

| Kolejność | Predykat `Path=` | Cel |
|---|---|---|
| 0 | `/api/categories/*/tests` | lab-test :8082 |
| 1 | `/api/categories/**` | category :8081 |
| 2 | `/api/tests/**` | lab-test :8082 |

Kolejność od najszczegółowszej — catch-all kategorii przechwyciłby żądania o badania. `/internal/**` celowo **bez trasy**: API zdarzeń jest nieosiągalne z zewnątrz. Wykonywalne przykłady: `services/gateway/request.http` (wszystko przez :8080).

## API per serwis

- **category (:8081):** `GET/POST /api/categories`, `GET/PUT/DELETE /api/categories/{id}` — semantyka i kody jak w labie 3; POST i DELETE dodatkowo publikują zdarzenie do lab-test.
- **lab-test (:8082):** `GET /api/tests`, `GET/PUT/DELETE /api/tests/{id}`, `GET/POST /api/categories/{categoryId}/tests` oraz wewnętrzne `PUT/DELETE /internal/categories/{id}`. Szczegółowe tabele: `services/<nazwa>/docs/pl/README.md`.

## Model danych (ERD) — dwie prywatne bazy

```mermaid
erDiagram
    CATEGORY__TEST_CATEGORIES {
        uuid id PK "seed: stale UUID-y"
        varchar name
        boolean requires_fasting
    }
    LABTEST__TEST_CATEGORIES ||--o{ LABTEST__LAB_TESTS : zawiera
    LABTEST__TEST_CATEGORIES {
        uuid id PK "replika: id rowne oryginalowi"
        varchar name
    }
    LABTEST__LAB_TESTS {
        uuid id PK
        varchar name
        varchar unit
        double reference_min
        double reference_max
        numeric price
        uuid category_id FK
    }
```

Prefiks oznacza bazę (`category` / `labtest`). Replika trzyma minimum potrzebne do relacji i DTO; kaskada `REMOVE` + `orphanRemoval` działa lokalnie w bazie labtest.

## Plany

Lab 5: frontend Angular (przez gateway). Lab 6: Dockerfile per serwis + `docker compose up`. Lab 7: discovery, 2 instancje lab-test, load balancing na gatewayu, zewnętrzne bazy, config service.
