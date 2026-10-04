# Lab 6 — Docker and Compose: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-6.md](../../pl/labs/lab-6.md)

## What was built

4 multi-stage Dockerfiles (`category`, `lab-test`, `gateway` — Temurin 25 JDK→JRE; `web/catalog` — Node→NGINX), an env-variable-driven NGINX proxy template, a root `compose.yaml` with 6 containers and PostgreSQL 18 ×2 as the external databases — the full task set 1–6 (9/9 pts).
Running everything: **`docker compose up --build`** → `http://localhost:4200` (frontend) and `:8080` (API). Presenting compose alone suffices (per the instruction's note).

## Critical sections — likely questions

- **Multi-stage (all 4 images):** stage 1 builds (JDK+mvnw / Node+npm), stage 2 gets only the artifact (JRE / NGINX) — build tools never ship, the image stays small. Exactly the .NET pattern: `sdk` image → `dotnet publish` → `aspnet` image.
- **Layer caching:** first `COPY pom.xml` + `dependency:go-offline` (resp. `package.json` + `npm ci`), only then `COPY src` — a code change does not re-download dependencies.
- **Env-based config (graded!):** Spring reads environment variables ABOVE `application.properties` (relaxed binding: `SPRING_DATASOURCE_URL` → `spring.datasource.url` — like `ConnectionStrings__X` in ASP.NET Core). Gateway routes: `${CATEGORY_URL:http://localhost:8081}` placeholders — without the variable the default applies, so dev mode keeps living.
- **The 3-point NGINX:** the official image pipes `/etc/nginx/templates/*.template` through `envsubst` at startup — `${GATEWAY_URL}` and `${NGINX_PORT}` land in the real config. `location /api/` → `proxy_pass` to the gateway (the lab-5 dev-proxy role taken over); `try_files … /index.html` = SPA fallback, without it a deep link 404s.
- **Compose DNS:** containers see each other by SERVICE NAME (`http://category:8081`, `http://lab-test:8082`); `localhost` inside a container is THAT container — the classic trap.
- **Network topology:** only `web` (:4200) and `gateway` (:8080) publish host ports; services and databases are internal-only — the network enforces "everything through the gateway" (same philosophy as the unrouted `/internal/**`).
- **External databases (2×1 pt):** same jar, different database — env vars only (`SPRING_DATASOURCE_*`); Boot infers the driver from the JDBC URL (the `driver-class-name` hardcode is gone). Two Postgres containers = the lab-4 private databases, unchanged.
- **Startup order:** the `pg_isready` healthcheck + `depends_on: condition: service_healthy` — Spring connects to the database eagerly at startup and would die if Postgres were still booting.
- **`ddl-auto=create-drop` stays on Postgres too:** schema and seeds from scratch at every start (exactly like H2); real migrations + volumes are lab 7.
- **Secrets:** sandbox defaults in `compose.yaml` (`${VAR:-medata}`), overridable via a `.env` file (the `.env.example` pattern; `.env` is gitignored).
