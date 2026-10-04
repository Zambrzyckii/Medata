# Medata

[![Build](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml/badge.svg)](https://github.com/Zambrzyckii/medata/actions/workflows/build.yml)

**Medata** (Med + data) to system informatyczny laboratorium medycznego (LIS) — projekt portfolio budowany na bazie 7 laboratoriów kursu *Internet Services Architectures* (Politechnika Gdańska): Java SE → Spring Boot i JPA → REST → mikroserwisy → Angular → Docker → discovery i centralna konfiguracja.

> English version: [README.md](README.md)

## Status

Lab 4 ukończony — mikroserwisy `services/category` (:8081) i `services/lab-test` (:8082) z synchronizacją zdarzeniami REST za Spring Cloud Gateway (:8080, jedyne publiczne wejście). Bieżący status: [docs/pl/STATE.md](docs/pl/STATE.md).

## Szybki start

```bash
cd services/category && ./mvnw spring-boot:run    # :8081
cd services/lab-test && ./mvnw spring-boot:run    # :8082 (drugi terminal)
cd services/gateway && ./mvnw spring-boot:run     # :8080 (trzeci terminal) — rozmawiaj tylko z tym
```

Docelowo ta sekcja zbiegnie do pojedynczego `docker compose up` (zasada jednej komendy — patrz konwencje).

## Wymagania

- JDK 25 lub nowsze (rozwijane na OpenJDK 27; Lombok przypięty na 1.18.48 ze wsparciem JDK 27)
- Maven niewymagany — każdy serwis ma własny Maven Wrapper (`services/<nazwa>/mvnw`)

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
