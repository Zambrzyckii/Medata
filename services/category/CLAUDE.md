# category service — agent context

Source of truth for test categories. Spring Boot MVC, port **8081**, package `com.medata.category`, private DB (dev: H2 `jdbc:h2:mem:category`; compose: PostgreSQL via `SPRING_DATASOURCE_*` env — no `driver-class-name`, Boot infers it from the URL).

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper — no global Maven, no parent pom). Container: multi-stage `Dockerfile` (Temurin 25 JDK build → JRE), started from the root `compose.yaml`; `EXPOSE 8081` but NOT published to the host (gateway-only entry).
- Publishes REST events to lab-test on category **create/delete** only: `CategoryEventPublisher` builds from the `@LoadBalanced` `RestClient.Builder` (the plain builder stays `@Primary`, otherwise the Eureka transport gets intercepted and heartbeats die) with the logical base URL `http://lab-test`, best-effort (`catch RestClientException | IllegalStateException` into a WARN). Seeds are SQL migrations and fire no events.
- Eureka + config client: registers as `category` (instance id from `INSTANCE_ID` env), imports config via `optional:configserver:` (`CONFIG_URL` env). Under compose the datasource and eureka zone come from the config service; dev mode needs discovery running and falls back to local H2 defaults.
- Schema is owned by Flyway (`db/migration/V1__init.sql`, `ddl-auto=validate`; Boot 4 needs `spring-boot-starter-flyway`, bare `flyway-core` does nothing). Seed lives in `V2__seed.sql`: fixed UUIDs (`1111…`/`2222…`/`3333…`) shared **by duplication** with lab-test — never change one side only. The Java initializer is gone (lab 7; lives in the `lab-6` tag).
- Lombok version is pinned in `pom.xml` (`lombok.version`) for JDK 27 — drop the pin once Boot's parent manages ≥ that version.
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules (user types all code, agent edits docs only): root `/CLAUDE.md`.
