# Medata

**Medata** (Med + data) to system informatyczny laboratorium medycznego (LIS) — projekt portfolio budowany na bazie 7 laboratoriów kursu *Internet Services Architectures* (Politechnika Gdańska): Java SE → Spring Boot i JPA → REST → mikroserwisy → Angular → Docker → discovery i centralna konfiguracja.

> English version: [README.md](README.md)

## Status

Lab 1 ukończony — działający konsolowy katalog badań (Java SE, Maven, Lombok). Bieżący status: [docs/pl/STATE.md](docs/pl/STATE.md).

## Szybki start

```bash
cd catalog && ./mvnw compile && java -cp target/classes com.medata.catalog.Main
```

Docelowo ta sekcja zbiegnie do pojedynczego `docker compose up` (zasada jednej komendy — patrz konwencje).

## Wymagania

- JDK 25 lub nowsze (rozwijane na OpenJDK 26)
- Maven niewymagany — repo zawiera Maven Wrapper (`catalog/mvnw`)

## Dokumentacja

Dwujęzyczna EN/PL, struktura 1:1 — [docs/en](docs/en) / [docs/pl](docs/pl):

- [Wizja](docs/pl/VISION.md) — co budujemy (laby CORE + backlog FUTURE)
- [Konwencje](docs/pl/CONVENTIONS.md) — jak pracujemy (git, dokumentacja, jakość, definition of done)
- [Decyzje](docs/pl/DECISIONS.md) — dlaczego jest tak, jak jest
- [Stan](docs/pl/STATE.md) — gdzie projekt aktualnie stoi
- [Historia zmian](docs/pl/CHANGELOG.md) — kamienie milowe
- [Ściąga labu 1](docs/pl/labs/lab-1.md) — przygotowanie do prezentacji
