# Serwis config

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Centralny serwer konfiguracji (Spring Cloud Config Server) na porcie **8888**. Wystawia konfigurację per aplikacja przez HTTP. Serwisy `category` i `lab-test` pobierają stąd przy starcie ustawienia bazy i discovery, zanim powstanie jakikolwiek bean. Pakiet `com.medata.config`, jedna klasa z `@EnableConfigServer`.

## Uruchomienie

```bash
./mvnw spring-boot:run          # :8888
./mvnw spotless:apply verify    # format + pełny build
```

W kontenerach startuje z rootowego `compose.yaml` z healthcheckiem przez `/dev/tcp`, a serwisy na niego czekają (`depends_on: service_healthy`). Co jest serwowane, można sprawdzić w przeglądarce pod `http://localhost:8888/category/default`.

## Konfiguracja

| Właściwość | Wartość | Po co |
|---|---|---|
| `server.port` | `8888` | umowny port Config Servera |
| `spring.profiles.active` | `native` | serwowanie plików z classpath zamiast domyślnego backendu git |
| `spring.cloud.config.server.native.search-locations` | `classpath:/config` | lokalizacja serwowanych plików |

Serwowane pliki leżą w `src/main/resources/config/`. Nazwa pliku równa się `spring.application.name` klienta, stąd `category.properties` i `lab-test.properties`. Obie instancje lab-test pytają o tę samą nazwę i dostają identyczną konfigurację, co trzyma je na jednej wspólnej bazie. Poświadczenia w środku to placeholdery w rodzaju `${CATEGORY_DB_USER:medata}`, rozwiązywane u klienta względem jego środowiska, więc żaden sekret nie mieszka w tym repozytorium.

## Model danych

Brak. Konfiguracja jest zaszyta w obrazie, więc jej zmiana to przebudowa tylko tego małego obrazu. Backend git zamieniłby zmiany konfiguracji w rewidowalne commity i to jest wybór produkcyjny.

## API

`GET /{application}/{profile}`, na przykład `/category/default` albo `/lab-test/default`. Odpowiedź wymienia źródła właściwości i ich wartości.

## Decyzje

- Backend `native` zamiast gita dla prostoty labu.
- Gateway celowo NIE jest klientem configu. Zadanie 5 instrukcji mówi „services", a rozróżnienie względem zadania 2 („gateway and services") jest celowe, więc gateway trzyma `DISCOVERY_URL` w środowisku.
- Klienci importują z przedrostkiem `optional:configserver:`, więc tryb deweloperski działa bez uruchomionego config-serwera.
