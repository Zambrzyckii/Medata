# Serwis gateway

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Jedyne publiczne wejście do systemu (**:8080**): Spring Cloud Gateway w wariancie reaktywnym (WebFlux, Netty) przekazujący żądania do serwisów `category` (:8081) i `lab-test` (:8082). Zero logiki w Javie — całość to konfiguracja tras w properties. Pakiet `com.medata.gateway`.

## Uruchomienie

```bash
./mvnw spring-boot:run          # start na :8080 ("Netty started")
./mvnw spotless:apply verify    # format + pełny build
```

Wymaga działających serwisów docelowych — bez nich trafione trasy zwracają 500 (connection refused do celu), a nie 404.

## Konfiguracja (`src/main/resources/application.properties`)

| Kolejność | Predykat `Path=` | Cel |
|---|---|---|
| 0 | `/api/categories/*/tests` | `http://localhost:8082` |
| 1 | `/api/categories/**` | `http://localhost:8081` |
| 2 | `/api/tests/**` | `http://localhost:8082` |

Trasy od najszczegółowszej (`*` = jeden segment, `**` = dowolnie wiele). `/internal/**` celowo bez trasy. Adresy celów na sztywno do labu 6 (potem środowisko), w labie 7 zastąpi je discovery (`lb://`).

## Model danych

Brak — gateway nie przechowuje żadnych danych.

## API

Nie definiuje własnego API — przekazuje żądania 1:1 (ścieżka bez zmian). Wykonywalne przykłady całego systemu: `request.http` w tym katalogu (wszystko przez :8080).

## Decyzje

- Wariant **WebFlux/Netty**: gateway głównie przepompowuje bajty — nieblokujące I/O obsługuje wiele połączeń garstką wątków; jedyna reaktywna aplikacja w projekcie.
- Wersje Spring Cloud dobiera BOM `spring-cloud-dependencies 2025.1.3` importowany w `dependencyManagement` (parowanie z Boot 4.0.8 — ADR-002, potwierdzone buildem).
