# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) to system informatyczny laboratorium medycznego (LIS) — projekt portfolio budowany na bazie 7 laboratoriów kursu *Internet Services Architectures* (Politechnika Gdańska): Java SE → Spring Boot i JPA → REST → mikroserwisy → Angular → Docker → discovery i centralna konfiguracja.

> English version: [README.md](README.md)

## Status

Część kursowa ukończona (laby 1 do 7). `docker compose up` stawia 9 kontenerów, frontend Angulara na NGINX (:4200), Spring Cloud Gateway z trasami `lb://` (:8080), serwis `category` i dwie balansowane instancje `lab-test`, discovery Eureki (:8761), centralny config-serwer (:8888) oraz dwie bazy PostgreSQL na named volumes z migracjami Flywaya. Bieżący status: [docs/pl/STATE.md](docs/pl/STATE.md).

## Szybki start

```bash
docker compose up --build
```

Potem otwórz `http://localhost:4200` (aplikacja) albo `http://localhost:8080/api/categories` (API). Poza Dockerem nic nie jest wymagane — obrazy same budują serwisy. Poświadczenia można nadpisać, kopiując `.env.example` do `.env`.

<details>
<summary>Tryb deweloperski (hot reload, H2 in-memory, bez Dockera)</summary>

```bash
cd services/discovery && ./mvnw spring-boot:run   # :8761 — rejestr, startuje pierwszy
cd services/category && ./mvnw spring-boot:run    # :8081 (drugi terminal)
cd services/lab-test && ./mvnw spring-boot:run    # :8082 (trzeci terminal)
cd services/gateway && ./mvnw spring-boot:run     # :8080 (czwarty terminal) — wejście API
cd web/catalog && npm start                       # :4200 (piąty terminal) — to otwórz w przeglądarce
```

Config-serwer jest w dev opcjonalny. Bez niego serwisy wracają do H2 in-memory i defaultów na localhost.

</details>

## Wymagania

- Docker z pluginem Compose — tyle wystarcza do uruchomienia jedną komendą
- Tryb deweloperski dodatkowo: JDK 25+ (rozwijane na OpenJDK 27; Lombok przypięty na 1.18.48), Node.js 20+ z npm (rozwijane na Node 26); Maven niewymagany — każdy serwis ma własny wrapper (`services/<nazwa>/mvnw`)

## Dokumentacja

Dwujęzyczna EN/PL, struktura 1:1 — [docs/en](docs/en) / [docs/pl](docs/pl):

- [Wizja](docs/pl/VISION.md) — co budujemy (laby CORE + backlog FUTURE)
- [Konwencje](docs/pl/CONVENTIONS.md) — jak pracujemy (git, dokumentacja, jakość, definition of done)
- [Decyzje](docs/pl/DECISIONS.md) — dlaczego jest tak, jak jest
- [Stan](docs/pl/STATE.md) — gdzie projekt aktualnie stoi
- [Historia zmian](docs/pl/CHANGELOG.md) — kamienie milowe
- [Architektura](docs/pl/ARCHITECTURE.md) — diagramy kontenerów i ERD (Mermaid)
- [ADR-y](docs/pl/adr) — rejestr decyzji architektonicznych
- [Ściągi labów](docs/pl/labs) — przygotowanie do prezentacji (lab-1, lab-2)
