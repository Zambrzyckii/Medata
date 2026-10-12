# Serwis discovery

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Rejestr usług (Spring Cloud Netflix Eureka Server) na porcie **8761**. Każda aplikacja backendu rejestruje się tu pod swoim `spring.application.name`, wysyła heartbeaty i pobiera lokalną kopię rejestru. Gateway i serwis category rozwiązują inne serwisy po nazwie logicznej przez tę kopię. Pakiet `com.medata.discovery`, zero logiki biznesowej, zależność jest produktem.

## Uruchomienie

```bash
./mvnw spring-boot:run          # :8761
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach startuje z rootowego `compose.yaml`. Dashboard pod `http://localhost:8761` pokazuje zarejestrowane instancje i warto go pokazać przy ocenie.

## Konfiguracja

| Właściwość | Wartość | Po co |
|---|---|---|
| `server.port` | `8761` | umowny port Eureki |
| `eureka.client.register-with-eureka` | `false` | samodzielny węzeł nie rejestruje się sam u siebie |
| `eureka.client.fetch-registry` | `false` | nie ma czego pobierać, gdy nie ma innych węzłów |

Produkcyjnie stawia się kilka serwerów Eureki rejestrujących się nawzajem (peer replication). Lab potrzebuje jednego samodzielnego węzła, stąd obie flagi.

## Model danych

Brak. Rejestr żyje w pamięci i odbudowuje się z rejestracji oraz heartbeatów. Brak heartbeatów w oknie czasowym prowadzi do eksmisji instancji.

## API

Dashboard pod `/` oraz rejestr REST pod `/eureka` (rejestracje, heartbeaty, listing `/eureka/apps`). Klienci rozmawiają z nim przez autokonfigurację `spring-cloud-starter-netflix-eureka-client`, nigdy ręcznie.

## Decyzje

- Pojedynczy samodzielny węzeł zamiast pary z replikacją. Lab ocenia jeden rejestr, a klienci i tak trzymają lokalny cache.
- Port opublikowany na host w compose. Dashboard to artefakt do oceny, jak Swagger UI w labie 3.
