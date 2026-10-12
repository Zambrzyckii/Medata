# Medata — aktualny stan

> Wersja polska. Angielski odpowiednik 1:1: [../en/STATE.md](../en/STATE.md)
> Ostatnia aktualizacja: 2026-10-12

## Faza

**Lab 7 UKOŃCZONY (7/7 pkt), a z nim cała faza labowa CORE (laby 1 do 7, potencjalnie 57 pkt).** `docker compose up --build` stawia 9 kontenerów. NGINX (:4200) proxuje do gatewaya (:8080), którego trasy `lb://` balansują między `category` a dwiema równorzędnymi instancjami `lab-test`, z discovery Eureki (:8761), config-serwerem (:8888) i dwiema bazami PostgreSQL na named volumes. Zweryfikowane od zera po wyczyszczeniu wolumenów: jednorazowa migracja Flywaya przy dwóch instancjach (dowód blokady), round-robin widoczny w logach instancji, zdarzenia repliki rozwiązywane przez rejestr, dane przeżywające `down` + `up`.

## Co istnieje

- `services/category` (:8081), `services/lab-test` (:8082) — logika z labu 4 plus klienci Eureki i configu, schematy w rękach Flywaya (seedy `V1` + `V2`, `ddl-auto=validate`, bez javowych initializerów), publisher na kliencie `@LoadBalanced` ze zwykłym builderem `@Primary`
- `services/gateway` (:8080) — trasy `lb://` przez rejestr; `services/discovery` (:8761) — samodzielna Eureka; `services/config` (:8888) — Config Server (native) serwujący ustawienia bazy i eureki; każdy serwis z własnym multi-stage `Dockerfile`
- `web/catalog` — Angular 22 standalone/signals/zoneless, 7 widoków, typed reactive forms; `Dockerfile` (build Node → NGINX) z szablonem proxy na envsubst
- Root `compose.yaml` — 9 kontenerów (web, gateway, discovery, config, category, lab-test ×2, Postgres ×2 na named volumes), healthchecki, publikowane porty 4200/8080/8761/8888; `.env.example` do nadpisywania poświadczeń
- Per moduł: `docs/{pl,en}/README.md` + `CLAUDE.md` (6 modułów: 5 serwisów + frontend)
- `docs/pl|en/`: VISION, CONVENTIONS, DECISIONS, STATE, CHANGELOG, ARCHITECTURE (widok dev + widok runtime compose + sekwencja zdarzeń + ERD), `adr/001–003`, ściągi `labs/lab-1..7.md`
- Root: `CLAUDE.md`, README ×2, `.editorconfig`, `.gitignore`, `.github/workflows/build.yml` (matrix 5 serwisów + job frontendu)
- Repo GitHub: `Zambrzyckii/medata` (main + tagi `lab-1`–`lab-7`)

## Środowisko deweloperskie

- Backend: OpenJDK 27 (`--release 25`), Lombok 1.18.48 (pin), Maven 3.9.16 + wrapper per serwis
- Frontend: Node.js 26.10, npm 12, Angular CLI 22; kod pisze użytkownik ręcznie (szkielety: `ng new`/delegacje na jawne zlecenie)
- Kontenery: Docker 29.8.2 + Compose 5.6.0; tryb dev (H2, hot reload) działa bez Dockera, ale wymaga uruchomionego discovery (:8761), config-serwer jest tam opcjonalny
- IDE: IntelliJ IDEA **Community** (SDK java-27-openjdk); uwaga: wtyczka Javy w VS Code kompiluje ECJ-em do `target/` — po dziwnych błędach startu `./mvnw clean`
- Git: prowadzi agent według globalnych zasad użytkownika (komunikaty rozkazujące na 3 do 8 słów, tagi etapów `lab-N`, bez stopek AI)

## Następny krok

Część kursowa jest zamknięta. Prezentacje idą ze ściąg (`docs/{pl,en}/labs/`) i checkoutów tagów. Dalej, gdy użytkownik zdecyduje, wchodzi **faza FUTURE** z WIZJI, zaczynając od zleceń i wyników z flagowaniem, na gałęziach `feat/*`.
