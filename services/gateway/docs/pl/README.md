# Serwis gateway

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Jedyne publiczne wejście do systemu (**:8080**): Spring Cloud Gateway w wariancie reaktywnym (WebFlux, Netty) przekazujący żądania do serwisów `category` (:8081) i `lab-test` (:8082). Zero logiki w Javie — całość to konfiguracja tras w properties. Pakiet `com.medata.gateway`.

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8080 ("Netty started")
./mvnw spotless:apply verify    # format + pełny build
```

Wymaga działających serwisów docelowych — bez nich trafione trasy zwracają 500 (connection refused do celu), a nie 404. W kontenerach gateway startuje z rootowego `compose.yaml` (`docker compose up --build`), ten sam wzorzec multi-stage Dockerfile co pozostałe serwisy; to jeden z tylko dwóch kontenerów z opublikowanym portem na host.

## Konfiguracja

Trasy w `src/main/resources/application.properties`; od labu 6 URI celów to placeholdery `${VAR:default}` — default utrzymuje tryb dev, a zmienna środowiskowa (ustawiana w `compose.yaml`) przekierowuje na DNS-owe nazwy kontenerów:

| Kolejność | Predykat `Path=` | URI celu |
|---|---|---|
| 0 | `/api/categories/*/tests` | `${LABTEST_URL:http://localhost:8082}` |
| 1 | `/api/categories/**` | `${CATEGORY_URL:http://localhost:8081}` |
| 2 | `/api/tests/**` | `${LABTEST_URL:http://localhost:8082}` |

Trasy od najszczegółowszej (`*` = jeden segment, `**` = dowolnie wiele). `/internal/**` celowo bez trasy. Compose ustawia `CATEGORY_URL=http://category:8081` i `LABTEST_URL=http://lab-test:8082`; w labie 7 zastąpi to discovery (`lb://`).

## Model danych

Brak — gateway nie przechowuje żadnych danych.

## API

Nie definiuje własnego API — przekazuje żądania 1:1 (ścieżka bez zmian). Wykonywalne przykłady całego systemu: `request.http` w tym katalogu (wszystko przez :8080).

## Decyzje

- Wariant **WebFlux/Netty**: gateway głównie przepompowuje bajty — nieblokujące I/O obsługuje wiele połączeń garstką wątków; jedyna reaktywna aplikacja w projekcie.
- Wersje Spring Cloud dobiera BOM `spring-cloud-dependencies 2025.1.3` importowany w `dependencyManagement` (parowanie z Boot 4.0.8 — ADR-002, potwierdzone buildem).
