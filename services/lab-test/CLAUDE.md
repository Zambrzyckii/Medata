# lab-test service — agent context

Owner of lab tests + a slim category replica. Spring Boot MVC, port **8082**, package `com.medata.labtest`, private H2 (`jdbc:h2:mem:labtest`).

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper — no global Maven, no parent pom).
- The `TestCategory` entity here is a **replica**: `id`+`name` only, NO random default id (ids always come from events or the seed and must equal the original in the category service).
- Event intake: `InternalCategoryController` — `PUT /internal/categories/{id}` (idempotent upsert via `orElseGet`), `DELETE` (replica removal; the `REMOVE`+`orphanRemoval` cascade deletes its tests). `/internal/**` has no gateway route on purpose.
- Seed uses fixed UUIDs (`1111…`/`2222…`/`3333…`) shared **by duplication** with the category service — never change one side only.
- Lombok version is pinned in `pom.xml` (`lombok.version`) for JDK 27 — drop the pin once Boot's parent manages ≥ that version.
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules (user types all code, agent edits docs only): root `/CLAUDE.md`.
