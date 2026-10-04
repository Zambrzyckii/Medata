# Medata — current state

> English version. Polish 1:1 counterpart: [../pl/STATE.md](../pl/STATE.md)
> Last updated: 2026-10-04

## Phase

**Lab 5 COMPLETE (9/9 pts), awaiting the commit + `lab-5` tag.** The full stack: the Angular 22 frontend (`web/catalog`, 7 routed CRUD views, dev :4200) → the gateway (:8080) → `category` (:8081) and `lab-test` (:8082) with replica sync over REST events. The whole loop (lists, pre-populated forms, deletion with the event-driven cascade) verified in the browser.

## What exists

- `services/category` (:8081), `services/lab-test` (:8082), `services/gateway` (:8080) — the lab-4 state (events, replica, routes), unchanged
- `web/catalog` — Angular 22 standalone/signals/zoneless: `models.ts` (mirrored DTOs), `Api` (`HttpClient`, relative `/api`), 7 view components, typed reactive forms, dev proxy → :8080; ESLint + Prettier
- Per module: `docs/{pl,en}/README.md` + `CLAUDE.md` (4 modules: 3 services + the frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (4 containers + event sequence + ERD), `adr/001–003`, cheat sheets `labs/lab-1..5.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (3-service matrix + a frontend job)
- GitHub repo: `Zambrzyckii/medata` (main + tags `lab-1`–`lab-4`; `lab-5` after the commit)

## Development environment

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pinned), Maven 3.9.16 + a wrapper per service
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; code typed by the user by hand (skeletons: `ng new`/delegations on explicit request)
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); note: the VS Code Java plugin compiles with ECJ into `target/` — after weird startup errors run `./mvnw clean`

## Next step

Commit + `lab-5` tag (done by the user). Then **lab 6 (9 pts): containerization** — a Dockerfile per service (Eclipse Temurin, env-based config), a frontend image on NGINX (Angular build + `/api` proxy configured by environment variables), `docker compose up` wiring everything; optionally (+2 pts within the 9) external databases. Lab 6 conventions: `.env.example`, the full one-command rule. To check: `docker` and `docker compose` in the environment.
