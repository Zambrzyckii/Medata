# Medata — architecture

> English version. Polish 1:1 counterpart: [../pl/ARCHITECTURE.md](../pl/ARCHITECTURE.md)
> As of: lab 2 (single application). Convention §6: an architecture change without a diagram update = an unfinished change.

## Container view

```mermaid
flowchart LR
    User((User - console)) --> Runner
    subgraph app [catalog - Spring Boot 4.0.8 app, Java 25]
        Init[SampleDataInitializer - Order 1] --> Services
        Runner[ConsoleRunner - Order 2] --> Services
        Services[TestCategoryService / LabTestService] --> Repos[Spring Data JPA repositories]
        Repos --> H2[(H2 in-memory - jdbc:h2:mem:catalog)]
    end
```

## Layers

Runner (console UI) → services (delegation + business validation) → repositories (data access) → H2. Each layer knows only its lower neighbour; dependencies are injected by Spring's DI container.

## Data model (ERD)

```mermaid
erDiagram
    TEST_CATEGORIES ||--o{ LAB_TESTS : contains
    TEST_CATEGORIES {
        uuid id PK
        varchar name
        boolean requires_fasting
    }
    LAB_TESTS {
        uuid id PK
        varchar name
        varchar unit
        double reference_min
        double reference_max
        numeric price
        uuid category_id FK
    }
```

The 1:N relation is bidirectional in code (`TestCategory.labTests` ↔ `LabTest.category`); the database holds only the `category_id` foreign key (owning side). Both directions are lazy.

## Plans

Lab 4 will split the application into two microservices (categories, tests) + a gateway — diagrams will be updated then.
