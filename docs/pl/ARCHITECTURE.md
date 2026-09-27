# Medata — architektura

> Wersja polska. Angielski odpowiednik 1:1: [../en/ARCHITECTURE.md](../en/ARCHITECTURE.md)
> Stan na: lab 2 (pojedyncza aplikacja). Konwencja §6: zmiana architektury bez aktualizacji diagramu = zmiana nieukończona.

## Widok kontenerów

```mermaid
flowchart LR
    User((Uzytkownik - konsola)) --> Runner
    subgraph app [catalog - aplikacja Spring Boot 4.0.8, Java 25]
        Init[SampleDataInitializer - Order 1] --> Services
        Runner[ConsoleRunner - Order 2] --> Services
        Services[TestCategoryService / LabTestService] --> Repos[Repozytoria Spring Data JPA]
        Repos --> H2[(H2 in-memory - jdbc:h2:mem:catalog)]
    end
```

## Warstwy

Runner (UI konsolowe) → serwisy (delegacja + walidacja biznesowa) → repozytoria (dostęp do danych) → H2. Każda warstwa zna tylko sąsiada niżej; zależności wstrzykuje kontener DI Springa.

## Model danych (ERD)

```mermaid
erDiagram
    TEST_CATEGORIES ||--o{ LAB_TESTS : zawiera
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

Relacja 1:N dwustronna w kodzie (`TestCategory.labTests` ↔ `LabTest.category`), w bazie tylko klucz obcy `category_id` (strona właściciela). Obie kolekcje leniwe.

## Plany

Lab 4 rozetnie aplikację na dwa mikroserwisy (kategorie, badania) + gateway — diagramy zostaną wtedy zaktualizowane.
