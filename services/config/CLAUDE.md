# config service — agent context

Spring Cloud Config Server (**:8888**), the central store of database and discovery configuration for `category` and `lab-test`. Package `com.medata.config`, one class with `@EnableConfigServer`.

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper, no global Maven, no parent pom). Inspect served config at `http://localhost:8888/{app}/default`.
- `native` profile serves `src/main/resources/config/*.properties`; the FILE NAME must equal the client's `spring.application.name` (`category.properties`, `lab-test.properties`). Changing served config means rebuilding this image.
- Served values point at compose DNS names (`category-db`, `discovery`); credentials are client-side placeholders (`${CATEGORY_DB_USER:medata}`) resolved against the CLIENT's env, so compose passes plain `*_DB_USER/PASSWORD` vars to the services.
- Precedence at the client: env vars beat config-server values, which beat the local `application.properties` (dev defaults H2/localhost). Clients use `spring.config.import=optional:configserver:${CONFIG_URL:...}`; `optional:` keeps dev alive without this server.
- The gateway is NOT a config client on purpose (task 5 letter). Container: multi-stage `Dockerfile`, `EXPOSE 8888`, published for demo; compose healthchecks it with bash `/dev/tcp` (the JRE image has no curl).
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules: root `/CLAUDE.md`.
