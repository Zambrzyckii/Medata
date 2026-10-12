# lab-test service — agent context

Owner of lab tests + a slim category replica. Spring Boot MVC, port **8082**, package `com.medata.labtest`, private DB (dev: H2 `jdbc:h2:mem:labtest`; compose: PostgreSQL via `SPRING_DATASOURCE_*` env — no `driver-class-name`, Boot infers it from the URL).

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper — no global Maven, no parent pom). Container: multi-stage `Dockerfile` (Temurin 25 JDK build → JRE), started from the root `compose.yaml`; `EXPOSE 8082` but NOT published to the host (gateway-only entry).
- The `TestCategory` entity here is a **replica**: `id`+`name` only, NO random default id (ids always come from events or the seed and must equal the original in the category service).
- Event intake: `InternalCategoryController` — `PUT /internal/categories/{id}` (idempotent upsert via `orElseGet`), `DELETE` (replica removal; the `REMOVE`+`orphanRemoval` cascade deletes its tests). `/internal/**` has no gateway route on purpose.
- Eureka + config client: registers as `lab-test` (instance id from `INSTANCE_ID` env), imports config via `optional:configserver:` (`CONFIG_URL` env). Compose runs TWO peer instances of this image (`lab-test-1`/`lab-test-2`) on one shared database; both fetch the same `lab-test` config and the Flyway lock makes migration one-time.
- Schema is owned by Flyway (`db/migration/V1__init.sql`, `ddl-auto=validate`; Boot 4 needs `spring-boot-starter-flyway`, bare `flyway-core` does nothing). Seed lives in `V2__seed.sql`: category replicas under the fixed UUIDs (`1111…`/`2222…`/`3333…`, duplicated with the category service — never change one side only) + 6 tests under fixed `aaaaaaaa-…-000N` ids. The Java initializer is gone (lab 7; lives in the `lab-6` tag).
- Lombok version is pinned in `pom.xml` (`lombok.version`) for JDK 27 — drop the pin once Boot's parent manages ≥ that version.
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules (user types all code, agent edits docs only): root `/CLAUDE.md`.
