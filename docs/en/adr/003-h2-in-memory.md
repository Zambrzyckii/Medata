# ADR-003: In-memory H2 for the lab phase

- **Status:** accepted (lab 2) · PL: [../../pl/adr/003-h2-in-memory.md](../../pl/adr/003-h2-in-memory.md)
- **Context:** the lab 2 instruction requires an in-memory H2 database; the lab phase must require no installation.
- **Decision:** `jdbc:h2:mem:catalog` + `ddl-auto=create-drop` (schema generated from entity annotations at every start) + a sample data initializer.
- **Consequences:** the database lives in the process RAM and vanishes on shutdown — hence seeding at every start and a repeatable state. From lab 6, external databases in containers are planned (driver and connection string change; JPA code untouched).
