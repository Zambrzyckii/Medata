# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-10-04

## Faza

**Lab 6 UKOŃCZONY (9/9 pkt), oczekuje na przegląd warstwy dockerowej przez użytkownika + commit z tagiem `lab-6`.** `docker compose up --build` stawia cały system od zera: NGINX serwujący bundle Angulara (:4200, `/api` proxowane do gatewaya przez zmienne środowiskowe) → gateway (:8080) → `category` + `lab-test`, każdy na własnym kontenerze PostgreSQL 18. Zweryfikowane end-to-end: seedy z Postgresa, przepływ zdarzeń repliki między kontenerami, deep linki SPA, serwisy nieosiągalne z hosta. Warstwę dockerową napisał Claude na jawne zlecenie użytkownika (odnotowany jednorazowy wyjątek).

## Co istnieje

- `services/category` (:8081), `services/lab-test` (:8082), `services/gateway` (:8080) — stan z labu 4 (zdarzenia, replika, trasy) + multi-stage `Dockerfile` w każdym, sterownik PostgreSQL w pomach serwisów, usunięty hardkod sterownika H2 (sterownik wnioskowany z URL-a JDBC), URI tras gatewaya jako placeholdery `${CATEGORY_URL:…}`/`${LABTEST_URL:…}`
- `web/catalog` — Angular 22 standalone/signals/zoneless, 7 widoków, typed reactive forms; + `Dockerfile` (build Node → NGINX) i `nginx/default.conf.template` (envsubst: `GATEWAY_URL`, `NGINX_PORT`; fallback SPA)
- Root `compose.yaml` — 6 kontenerów (web, gateway, category, lab-test, category-db, lab-test-db), healthchecki Postgresa, publikowane tylko web :4200 i gateway :8080; `.env.example` do nadpisywania poświadczeń
- Per moduł: `docs/{pl,en}/README.md` + `CLAUDE.md` (4 moduły: 3 serwisy + frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (widok dev + widok runtime compose + sekwencja zdarzeń + ERD), `adr/001–003`, ściągi `labs/lab-1..6.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (matrix 3 serwisów + job frontendu)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`–`lab-5`; `lab-6` po commicie)

## Środowisko deweloperskie

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pin), Maven 3.9.16 + wrapper per serwis
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; kod pisze użytkownik ręcznie (szkielety: `ng new`/delegacje na jawne zlecenie)
- Kontenery: Docker 29.8.2 + Compose 5.6.0; tryb dev (H2, hot reload) dalej działa bez Dockera
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); uwaga: wtyczka Javy w VS Code kompiluje ECJ-em do `target/` — po dziwnych błędach startu `./mvnw clean`

## Następny krok

Użytkownik przegląda zleconą warstwę dockerową, potem commit + tag `lab-6`. Następnie **lab 7 (7 pkt): deployment zaawansowany** — discovery service z rejestracją, 2 instancje lab-test z load balancingiem gatewaya (`lb://`), zewnętrzne bazy z wolumenami i migracjami schematu przy starcie, centralny config service; wszystko pozostaje w Compose.
