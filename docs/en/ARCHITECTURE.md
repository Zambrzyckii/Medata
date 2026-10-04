# Medata — architecture

> English version. Polish 1:1 counterpart: [../pl/ARCHITECTURE.md](../pl/ARCHITECTURE.md)
> As of: lab 6 (containerization). Convention §6: an architecture change without a diagram update = an unfinished change.

## Container view (development mode)

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

Each service owns a **private** database — there is no shared schema and no foreign key across services. Consistency is kept by REST events and the fixed seed UUIDs. In development mode the database is in-memory H2; under Docker Compose the very same jar talks to PostgreSQL — the switch is purely environment variables (see below).

## Runtime view (Docker Compose — lab 6)

```mermaid
flowchart LR
    Browser((Browser)) -->|":4200"| WEB
    Client((HTTP client - request.http, curl)) -->|":8080"| GW
    subgraph net [docker compose - one network, DNS by service name]
        WEB["web - NGINX<br>Angular bundle + proxy /api -> GATEWAY_URL"]
        WEB -->|"http://gateway:8080"| GW[gateway :8080]
        GW -->|"http://category:8081"| CAT[category :8081]
        GW -->|"http://lab-test:8082"| LT[lab-test :8082]
        CAT -. "events /internal/..." .-> LT
        CAT --> CATDB[(category-db<br>PostgreSQL 18)]
        LT --> LTDB[(lab-test-db<br>PostgreSQL 18)]
    end
```

Only `web` (:4200) and `gateway` (:8080) publish host ports; the services and both databases are reachable **solely inside the compose network** — the lab-4 rule "everything through the gateway" is now enforced by the network itself. All cross-container URLs are injected as environment variables in `compose.yaml` (`GATEWAY_URL`, `CATEGORY_URL`, `LABTEST_URL`, `SPRING_DATASOURCE_*`, `LABTEST_BASE_URL`); the images never hardcode an address and the same image would run in any environment. Spring reads env vars over `application.properties` (same idea as `ConnectionStrings__X` in ASP.NET Core), NGINX gets them via `envsubst` on a config template, and the gateway routes use `${VAR:default}` placeholders, so development mode keeps working without any variables set.

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

Lab 7: a discovery service, 2 lab-test instances behind gateway load balancing, database volumes + schema migrations, a central config service — all inside Compose.
