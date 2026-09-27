# Lab 2 — Spring Boot & Spring Data JPA: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-2.md](../../pl/labs/lab-2.md)

## What was built

`catalog` rebuilt on Spring Boot 4.0.8: JPA entities on in-memory H2, repositories, services with validation, a data initializer and a console CRUD runner — all 5 tasks (8/8 pts).
Run: `cd catalog && ./mvnw spring-boot:run` (commands: help, categories, tests, add, delete, stop).

## Critical sections — likely questions

- **DI / IoC (the heart of the lab):** Spring's container creates objects (beans) and injects dependencies. `@SpringBootApplication` enables package component scanning — classes with `@Component`/`@Service`/`@Repository` register themselves. Constructor injection: `final` field + `@RequiredArgsConstructor` (Lombok generates the constructor); a single constructor = Spring injects without `@Autowired`.
- **`@Component` vs `@Service` vs `@Repository`:** technically the same (a bean); the difference is semantic — the layer name. `@Repository` additionally translates database exceptions into Spring's hierarchy.
- **Repositories with no implementation:** Spring Data generates a proxy from the `extends JpaRepository<Entity, UUID>` interface (CRUD for free). `findAllByCategory` is a *derived query* — derived from the method name.
- **Why services that just delegate:** layered architecture (runner → service → repository); business logic has a home — proof: validation in `LabTestService.save` (name/unit not blank, min ≤ max, price ≥ 0) throws `IllegalArgumentException`, the runner catches and prints the message.
- **Client-generated UUID (ADR-001):** `@Id` without `@GeneratedValue` + `UUID.randomUUID()` in the field. Effect: `save()` cannot tell if the entity is new → merge → SELECT before INSERT (visible in the SQL logs).
- **Tables and columns:** plural explicitly via `@Table(name = "lab_tests")`; snake_case for free — the default naming strategy translates `requiresFasting` → `requires_fasting` (zero `@Column`).
- **Lazy:** `@OneToMany` is LAZY by default, but `@ManyToOne` is EAGER by default — overridden explicitly. Touching a lazy proxy after the session closes = `LazyInitializationException`; that is why printouts never reach for `category`.
- **Two runners, ordering:** `SampleDataInitializer` `@Order(1)` seeds through services, `ConsoleRunner` `@Order(2)` starts after it. `stop` ends the loop → the container shuts the app down gracefully by itself (no `System.exit`).
- **H2 (ADR-003):** a SQL database inside the JVM process (like SQLite `:memory:`); `ddl-auto=create-drop` generates the schema from entities at startup; data lives until shutdown — hence seeding at every start.
