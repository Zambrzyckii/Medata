# Medata — architecture

> English version. Polish 1:1 counterpart: [../pl/ARCHITECTURE.md](../pl/ARCHITECTURE.md)
> As of: lab 4 (microservices + gateway). Convention §6: an architecture change without a diagram update = an unfinished change.

## Container view

```mermaid
flowchart LR
    Browser((Browser)) --> FE[web/catalog - Angular 22, dev :4200]
    FE -->|"/api via dev proxy"| GW
    Client((HTTP client - request.http, curl)) -->|"JSON :8080"| GW
    subgraph gw [gateway - Spring Cloud Gateway WebFlux, Netty :8080]
        GW[Path= routes, most specific first]
    end
    GW -->|"/api/categories/**"| CATC
    GW -->|"/api/categories/*/tests and /api/tests/**"| LTC
    subgraph cat [category - Spring Boot MVC, Tomcat :8081]
        CATC[TestCategoryController] --> CATS[TestCategoryService] --> CATDB[(H2 in-mem: category)]
        CATC --> PUB[CategoryEventPublisher - RestClient]
    end
    subgraph lt [lab-test - Spring Boot MVC, Tomcat :8082]
        LTC[LabTestController + InternalCategoryController] --> LTS[LabTestService / TestCategoryService] --> LTDB[(H2 in-mem: labtest)]
    end
    PUB -. "PUT/DELETE /internal/categories/id events" .-> LTC
```

Each service owns a **private** H2 database — there is no shared schema and no foreign key across services. Consistency is kept by REST events and the fixed seed UUIDs.

## Event flow (replica sync)

```mermaid
sequenceDiagram
    participant K as Client
    participant G as gateway :8080
    participant C as category :8081
    participant L as lab-test :8082
    K->>G: POST /api/categories {name,...}
    G->>C: POST /api/categories
    C->>C: INSERT into H2 category
    C-)L: PUT /internal/categories/{id} {name}
    L->>L: idempotent replica upsert
    C-->>G: 201 Created
    G-->>K: 201 Created
    K->>G: DELETE /api/categories/{id}
    G->>C: DELETE /api/categories/{id}
    C->>C: DELETE from H2 category
    C-)L: DELETE /internal/categories/{id}
    L->>L: remove replica + cascade tests
    C-->>G: 204 No Content
    G-->>K: 204 No Content
```

Publishing is **best-effort**: a network failure does not break the category operation (`try/catch` → WARN in the log). The `lab-test` handlers are **idempotent** (PUT = upsert, DELETE of a missing replica → 204), so a repeated event does no harm. A category rename is NOT propagated (a deliberate gap — the instruction requires events only on add/remove).

## Routing (gateway)

| Order | `Path=` predicate | Target |
|---|---|---|
| 0 | `/api/categories/*/tests` | lab-test :8082 |
| 1 | `/api/categories/**` | category :8081 |
| 2 | `/api/tests/**` | lab-test :8082 |

Most specific first — the category catch-all would otherwise steal test requests. `/internal/**` deliberately has **no route**: the event API is unreachable from outside. Executable examples: `services/gateway/request.http` (everything via :8080).

## API per service

- **category (:8081):** `GET/POST /api/categories`, `GET/PUT/DELETE /api/categories/{id}` — semantics and codes as in lab 3; POST and DELETE additionally publish an event to lab-test.
- **lab-test (:8082):** `GET /api/tests`, `GET/PUT/DELETE /api/tests/{id}`, `GET/POST /api/categories/{categoryId}/tests` plus the internal `PUT/DELETE /internal/categories/{id}`. Detailed tables: `services/<name>/docs/en/README.md`.

## Data model (ERD) — two private databases

```mermaid
erDiagram
    CATEGORY__TEST_CATEGORIES {
        uuid id PK "seed: fixed UUIDs"
        varchar name
        boolean requires_fasting
    }
    LABTEST__TEST_CATEGORIES ||--o{ LABTEST__LAB_TESTS : contains
    LABTEST__TEST_CATEGORIES {
        uuid id PK "replica: id equals the original"
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

The prefix names the database (`category` / `labtest`). The replica holds the minimum needed for the relation and DTOs; the `REMOVE` + `orphanRemoval` cascade works locally inside the labtest database.

## Plans

Lab 6: a Dockerfile per service + an NGINX image for the frontend (taking over the dev-proxy role) + `docker compose up`. Lab 7: discovery, 2 lab-test instances, load balancing at the gateway, external databases, a config service.
