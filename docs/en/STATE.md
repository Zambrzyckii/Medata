# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-10-12

## Phase

**Lab 7 COMPLETE (7/7 pts) and with it the whole CORE lab phase (labs 1 to 7, 57 pts potential).** `docker compose up --build` starts 9 containers. NGINX (:4200) proxies to the gateway (:8080), whose `lb://` routes balance between `category` and two peer `lab-test` instances, with Eureka discovery (:8761), the config server (:8888) and two PostgreSQL databases on named volumes. Verified from scratch after wiping volumes: one-time Flyway migration across the two instances (lock proof), round-robin visible in instance logs, replica events resolved through the registry, data surviving `down` + `up`.

## What exists

- `services/category` (:8081), `services/lab-test` (:8082) — lab-4 logic plus Eureka and config clients, Flyway-owned schemas (`V1` + `V2` seeds, `ddl-auto=validate`, no Java initializers), the publisher on a `@LoadBalanced` client with the plain builder `@Primary`
- `services/gateway` (:8080) — `lb://` routes through the registry; `services/discovery` (:8761) — standalone Eureka; `services/config` (:8888) — Config Server (native) serving database + eureka settings; every service with its own multi-stage `Dockerfile`
- `web/catalog` — Angular 22 standalone/signals/zoneless, 7 views, typed reactive forms; `Dockerfile` (Node build → NGINX) with the envsubst proxy template
- Root `compose.yaml` — 9 containers (web, gateway, discovery, config, category, lab-test ×2, Postgres ×2 on named volumes), healthchecks, published ports 4200/8080/8761/8888; `.env.example` for credential overrides
- Per module: `docs/{pl,en}/README.md` + `CLAUDE.md` (6 modules: 5 services + the frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (dev view + compose runtime view + event sequence + ERD), `adr/001–003`, cheat sheets `labs/lab-1..7.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (5-service matrix + a frontend job)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`–`lab-7`)

## Development environment

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pinned), Maven 3.9.16 + a wrapper per service
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; code typed by the user by hand (skeletons: `ng new`/delegations on explicit request)
- Containers: Docker 29.8.2 + Compose 5.6.0; dev mode (H2, hot reload) works without Docker but needs the discovery service running (:8761), the config service is optional there
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); note: the VS Code Java plugin compiles with ECJ into `target/` — after weird startup errors run `./mvnw clean`
- Git: driven by the agent under the user's global rules (imperative messages of 3 to 8 words, stage tags `lab-N`, no AI footers)

## Next step

The course part is closed. Presentations run off the cheat sheets (`docs/{pl,en}/labs/`) and tag checkouts. Next, when the user decides, comes the **FUTURE phase** from VISION, starting with orders and results with flagging, on `feat/*` branches.
