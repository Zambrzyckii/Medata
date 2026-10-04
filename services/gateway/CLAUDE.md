# gateway service — agent context

The only public entry (**:8080**). Spring Cloud Gateway, **reactive flavor** (`spring-cloud-starter-gateway-server-webflux`, Netty) — the project's only non-servlet app. Package `com.medata.gateway`; zero Java logic, routes live in `application.properties`.

- Build & run: `./mvnw spotless:apply verify` / `./mvnw spring-boot:run` (own wrapper — no global Maven, no parent pom).
- Route namespace: `spring.cloud.gateway.server.webflux.routes[n]`. Order matters — most specific first (`/api/categories/*/tests` → lab-test BEFORE `/api/categories/**` → category; `/api/tests/**` → lab-test). `/internal/**` must never get a route.
- Spring Cloud versions come from the `spring-cloud-dependencies` BOM import (ADR-002 pairs the train with the Boot line) — no versions on Cloud starters.
- Diagnostics: a 404 through the gateway = no matching route; a 500 = route matched but the target service is down.
- `request.http` here exercises the whole system via :8080. Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules: root `/CLAUDE.md`.
