# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-10-04

## Phase

**Lab 6 COMPLETE (9/9 pts), awaiting the user's review of the Docker layer + the commit with the `lab-6` tag.** `docker compose up --build` starts the entire system from scratch: NGINX serving the Angular bundle (:4200, `/api` proxied to the gateway via env vars) → gateway (:8080) → `category` + `lab-test`, each on its own PostgreSQL 18 container. End-to-end verified: seeds from Postgres, the event-driven replica flow between containers, SPA deep links, services unreachable from the host. The Docker layer was written by Claude on the user's explicit delegation (a recorded one-off exception).

## What exists

- `services/category` (:8081), `services/lab-test` (:8082), `services/gateway` (:8080) — the lab-4 state (events, replica, routes) + a multi-stage `Dockerfile` each, a PostgreSQL driver in the service poms, the H2 driver hardcode removed (driver inferred from the JDBC URL), gateway route URIs as `${CATEGORY_URL:…}`/`${LABTEST_URL:…}` placeholders
- `web/catalog` — Angular 22 standalone/signals/zoneless, 7 views, typed reactive forms; + `Dockerfile` (Node build → NGINX) and `nginx/default.conf.template` (envsubst: `GATEWAY_URL`, `NGINX_PORT`; SPA fallback)
- Root `compose.yaml` — 6 containers (web, gateway, category, lab-test, category-db, lab-test-db), Postgres healthchecks, only web :4200 and gateway :8080 published; `.env.example` for credential overrides
- Per module: `docs/{pl,en}/README.md` + `CLAUDE.md` (4 modules: 3 services + the frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (dev view + compose runtime view + event sequence + ERD), `adr/001–003`, cheat sheets `labs/lab-1..6.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (3-service matrix + a frontend job)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`–`lab-5`; `lab-6` after the commit)

## Development environment

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pinned), Maven 3.9.16 + a wrapper per service
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; code typed by the user by hand (skeletons: `ng new`/delegations on explicit request)
- Containers: Docker 29.8.2 + Compose 5.6.0; dev mode (H2, hot reload) still works without Docker
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); note: the VS Code Java plugin compiles with ECJ into `target/` — after weird startup errors run `./mvnw clean`

## Next step

The user reviews the delegated Docker layer, then the commit + `lab-6` tag. Then **lab 7 (7 pts): advanced deployment** — a discovery service with registration, 2 lab-test instances with gateway load balancing (`lb://`), external databases with volumes and schema migrations at startup, a central config service; everything stays inside Compose.
