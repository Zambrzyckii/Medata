# Lab 6 — Docker i Compose: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-6.md](../../en/labs/lab-6.md)

## Co powstało

4 multi-stage Dockerfile'e (`category`, `lab-test`, `gateway` — Temurin 25 JDK→JRE; `web/catalog` — Node→NGINX), szablon proxy NGINX na zmiennych środowiskowych, `compose.yaml` w roocie z 6 kontenerami i PostgreSQL 18 ×2 jako bazami zewnętrznymi — komplet zadań 1–6 (9/9 pkt).
Uruchomienie całości: **`docker compose up --build`** → `http://localhost:4200` (frontend) i `:8080` (API). Do prezentacji wystarczy compose (adnotacja z instrukcji).

## Sekcje krytyczne — o co mogą zapytać

- **Multi-stage (wszystkie 4 obrazy):** stage 1 buduje (JDK+mvnw / Node+npm), stage 2 dostaje sam artefakt (JRE / NGINX) — narzędzia budowania nie jadą na produkcję, obraz jest mały. Dokładnie wzorzec .NET: obraz `sdk` → `dotnet publish` → obraz `aspnet`.
- **Cache warstw:** najpierw `COPY pom.xml` + `dependency:go-offline` (odpowiednio `package.json` + `npm ci`), dopiero potem `COPY src` — zmiana kodu nie wymusza ponownego ściągania zależności.
- **Konfiguracja przez env (punktowana!):** Spring czyta zmienne środowiskowe PONAD `application.properties` (relaxed binding: `SPRING_DATASOURCE_URL` → `spring.datasource.url` — jak `ConnectionStrings__X` w ASP.NET Core). Trasy gatewaya: placeholdery `${CATEGORY_URL:http://localhost:8081}` — bez zmiennej działa default, więc tryb dev żyje dalej.
- **NGINX za 3 pkt:** oficjalny obraz przy starcie przepuszcza `/etc/nginx/templates/*.template` przez `envsubst` — `${GATEWAY_URL}` i `${NGINX_PORT}` lądują w realnej konfiguracji. `location /api/` → `proxy_pass` na gateway (przejęta rola dev-proxy z labu 5); `try_files … /index.html` = fallback SPA, bez niego deep link daje 404.
- **DNS Compose:** kontenery widzą się po NAZWACH USŁUG (`http://category:8081`, `http://lab-test:8082`); `localhost` wewnątrz kontenera to TEN kontener — klasyczna pułapka.
- **Topologia sieci:** porty na host publikują tylko `web` (:4200) i `gateway` (:8080); serwisy i bazy są wyłącznie wewnętrzne — sieć wymusza „wszystko przez gateway" (ta sama filozofia co brak trasy `/internal/**`).
- **Bazy zewnętrzne (2×1 pkt):** ten sam jar, inna baza — tylko env (`SPRING_DATASOURCE_*`); sterownik Boot wnioskuje z URL-a JDBC (hardkod `driver-class-name` usunięty). Dwa kontenery Postgresa = prywatne bazy z labu 4 bez zmian.
- **Kolejność startu:** healthcheck `pg_isready` + `depends_on: condition: service_healthy` — Spring przy starcie od razu dobija do bazy i padłby, gdyby Postgres jeszcze wstawał.
- **`ddl-auto=create-drop` zostaje także na Postgresie:** schemat i seedy od zera przy każdym starcie (identycznie jak H2); prawdziwe migracje + wolumeny to lab 7.
- **Sekrety:** defaulty sandboksowe w `compose.yaml` (`${VAR:-medata}`), nadpisywane plikiem `.env` (wzorzec `.env.example`; `.env` w `.gitignore`).
