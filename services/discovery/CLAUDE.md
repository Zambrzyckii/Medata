# discovery service — agent context

Eureka Server, the registry of all backend apps (**:8761**). Package `com.medata.discovery`, one class with `@EnableEurekaServer`, everything else is configuration.

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper, no global Maven, no parent pom). Dashboard at `http://localhost:8761`.
- Standalone-node flags in `application.properties`: `register-with-eureka=false` and `fetch-registry=false`. Without them the server tries to register with itself and logs connection errors.
- Clients register under their `spring.application.name`; instance ids come from the `INSTANCE_ID` env (task 5 of lab 7). Start discovery FIRST in dev mode, other apps only spam retry WARNs without it.
- Container: multi-stage `Dockerfile` (Temurin 25 JDK build, then JRE), `EXPOSE 8761`, published to the host in compose because the dashboard is a demo artifact.
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules: root `/CLAUDE.md`.
