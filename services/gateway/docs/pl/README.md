# Serwis gateway

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Jedyne publiczne wejście do systemu (**:8080**): Spring Cloud Gateway w wariancie reaktywnym (WebFlux, Netty) przekazujący żądania do serwisów `category` (:8081) i `lab-test` (:8082). Zero logiki w Javie — całość to konfiguracja tras w properties. Pakiet `com.medata.gateway`.

## Uruchomienie

```bash
./mvnw spring-boot:run          # tryb dev na :8080 ("Netty started")
./mvnw spotless:apply verify    # format + pełny build
```

Wymaga działającego discovery. Trasa, której cel nie ma znanej instancji, odpowiada 503, trafiona trasa z martwą instancją 500, brak pasującej trasy 404. W kontenerach gateway startuje z rootowego `compose.yaml` (`docker compose up --build`), ten sam wzorzec multi-stage Dockerfile co pozostałe serwisy.

## Konfiguracja

Trasy w `src/main/resources/application.properties`. Od labu 7 URI celów używają schematu `lb://`, który znaczy „weź instancje tej nazwy logicznej z rejestru i balansuj round-robinem". Ta sama konfiguracja działa w dev i w kontenerach, bo różnice środowisk wchłania rejestr:

| Kolejność | Predykat `Path=` | URI celu |
|---|---|---|
| 0 | `/api/categories/*/tests` | `lb://lab-test` |
| 1 | `/api/categories/**` | `lb://category` |
| 2 | `/api/tests/**` | `lb://lab-test` |

Trasy od najszczegółowszej (`*` = jeden segment, `**` = dowolnie wiele). `/internal/**` celowo bez trasy. Adres rejestru przychodzi z `DISCOVERY_URL` (fallback dev `http://localhost:8761/eureka`), a instance id z `INSTANCE_ID`. Przy dwóch instancjach lab-test w compose balansowanie widać w ich logach.

## Model danych

Brak — gateway nie przechowuje żadnych danych.

## API

Nie definiuje własnego API — przekazuje żądania 1:1 (ścieżka bez zmian). Wykonywalne przykłady całego systemu: `request.http` w tym katalogu (wszystko przez :8080).

## Decyzje

- Wariant **WebFlux/Netty**: gateway głównie przepompowuje bajty — nieblokujące I/O obsługuje wiele połączeń garstką wątków; jedyna reaktywna aplikacja w projekcie.
- Wersje Spring Cloud dobiera BOM `spring-cloud-dependencies 2025.1.3` importowany w `dependencyManagement` (parowanie z Boot 4.0.8 — ADR-002, potwierdzone buildem).
- Celowo nie jest klientem config-serwisu. Zadanie 5 labu 7 mówi „services", więc gateway trzyma `DISCOVERY_URL` i `INSTANCE_ID` w środowisku.
