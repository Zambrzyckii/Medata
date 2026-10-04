# category service — agent context

Source of truth for test categories. Spring Boot MVC, port **8081**, package `com.medata.category`, private DB (dev: H2 `jdbc:h2:mem:category`; compose: PostgreSQL via `SPRING_DATASOURCE_*` env — no `driver-class-name`, Boot infers it from the URL).

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper — no global Maven, no parent pom). Container: multi-stage `Dockerfile` (Temurin 25 JDK build → JRE), started from the root `compose.yaml`; `EXPOSE 8081` but NOT published to the host (gateway-only entry).
- Publishes REST events to lab-test on category **create/delete** only: `CategoryEventPublisher` (`RestClient`, base URL from the `labtest.base-url` property), called from the controller, best-effort (`try/catch` → WARN). The initializer never publishes.
- Seed uses fixed UUIDs (`1111…`/`2222…`/`3333…`) shared **by duplication** with lab-test — never change one side only.
- Lombok version is pinned in `pom.xml` (`lombok.version`) for JDK 27 — drop the pin once Boot's parent manages ≥ that version.
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules (user types all code, agent edits docs only): root `/CLAUDE.md`.
