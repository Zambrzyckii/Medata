# Medata — architektura

> Wersja polska. Angielski odpowiednik 1:1: [../en/ARCHITECTURE.md](../en/ARCHITECTURE.md)
> Stan na: lab 6 (konteneryzacja). Konwencja §6: zmiana architektury bez aktualizacji diagramu = zmiana nieukończona.

## Widok kontenerów (tryb deweloperski)

```mermaid
flowchart LR
    Browser((Przegladarka)) --> FE[web/catalog - Angular 22, dev :4200]
    FE -->|"/api przez dev-proxy"| GW
    Client((Klient HTTP - request.http, curl)) -->|"JSON :8080"| GW
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

Każdy serwis ma **prywatną** bazę — nie istnieje wspólny schemat ani klucz obcy między serwisami. Spójność utrzymują zdarzenia REST i stałe UUID-y seedu. W trybie deweloperskim bazą jest H2 in-memory; pod Docker Compose ten sam jar rozmawia z PostgreSQL — przełącznikiem są wyłącznie zmienne środowiskowe (patrz niżej).

## Widok runtime (Docker Compose — lab 6)

```mermaid
flowchart LR
    Browser((Przegladarka)) -->|":4200"| WEB
    Client((Klient HTTP - request.http, curl)) -->|":8080"| GW
    subgraph net [docker compose - jedna siec, DNS po nazwie uslugi]
        WEB["web - NGINX<br>bundle Angulara + proxy /api -> GATEWAY_URL"]
        WEB -->|"http://gateway:8080"| GW[gateway :8080]
        GW -->|"http://category:8081"| CAT[category :8081]
        GW -->|"http://lab-test:8082"| LT[lab-test :8082]
        CAT -. "zdarzenia /internal/..." .-> LT
        CAT --> CATDB[(category-db<br>PostgreSQL 18)]
        LT --> LTDB[(lab-test-db<br>PostgreSQL 18)]
    end
```

Porty na hoście publikują tylko `web` (:4200) i `gateway` (:8080); serwisy i obie bazy są osiągalne **wyłącznie wewnątrz sieci compose** — zasada z labu 4 „wszystko przez gateway" jest teraz egzekwowana przez samą sieć. Wszystkie między-kontenerowe adresy wstrzykuje `compose.yaml` jako zmienne środowiskowe (`GATEWAY_URL`, `CATEGORY_URL`, `LABTEST_URL`, `SPRING_DATASOURCE_*`, `LABTEST_BASE_URL`); obrazy nigdy nie hardkodują adresu i ten sam obraz pojechałby na dowolne środowisko. Spring czyta zmienne środowiskowe ponad `application.properties` (ta sama idea co `ConnectionStrings__X` w ASP.NET Core), NGINX dostaje je przez `envsubst` na szablonie konfiguracji, a trasy gatewaya używają placeholderów `${VAR:default}`, więc tryb deweloperski działa dalej bez żadnych zmiennych.

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

Lab 7: discovery service, 2 instancje lab-test za load balancingiem gatewaya, wolumeny baz + migracje schematu, centralny config service — wszystko w Compose.
