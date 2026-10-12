# Lab 7 — deployment zaawansowany: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-7.md](../../en/labs/lab-7.md)

## Co powstało

Dwie nowe miniaplikacje (`discovery` = Eureka :8761, `config` = Config Server :8888, obie bez logiki, jedna adnotacja każda), trasy gatewaya na `lb://`, publisher zdarzeń przez rejestr, **2 równorzędne instancje lab-test** na wspólnej bazie, migracje Flywaya i named volumes — komplet zadań 1–5 (7/7 pkt).
Uruchomienie: `docker compose up --build`, 9 kontenerów. Artefakty do pokazania: dashboard Eureki (`:8761`), serwowana konfiguracja (`:8888/category/default`), logi `docker compose logs -f lab-test-1 lab-test-2`.

## Sekcje krytyczne — o co mogą zapytać

- **Rejestracja (2 pkt):** klient Eureki na classpath rejestruje aplikację pod `spring.application.name` przy starcie, potem heartbeat co 30 s. Brak heartbeatów = eksmisja wpisu. Serwer jest węzłem samodzielnym (`register-with-eureka=false`, `fetch-registry=false`), bo nie ma kolegów do replikacji.
- **Instance id przez env (wymóg zadania 5):** `eureka.instance.instance-id=${INSTANCE_ID:...}`, compose ustawia `lab-test-1`/`lab-test-2`. Po tych nazwach w logach dowodzisz load balancingu.
- **Load balancing (2 pkt):** `lb://lab-test` znaczy „weź instancje tej nazwy z lokalnej kopii rejestru i wybieraj round-robinem". Client-side LB (książka telefoniczna u dzwoniącego), w przeciwieństwie do server-side (centrala typu NGINX albo K8s Service). Dowód: dwa strzały w `/api/tests`, SQL-e lądują naprzemiennie w logach obu instancji.
- **Pułapka, która nas ugryzła:** transport samej Eureki bierze z kontekstu dowolny `RestClient.Builder`. Gdy jedynym był nasz `@LoadBalanced`, heartbeaty poszły przez load balancer i umarły. Recepta z dokumentacji to dwa beany, zwykły `@Primary` plus `@LoadBalanced` wybierany kwalifikatorem (adnotacja jest meta-`@Qualifier`).
- **Wolumeny + wspólna baza (1 pkt):** dwie instancje, jedna baza, dane przeżywają `down` i `up`. Obraz `postgres:18` montuje dane w `/var/lib/postgresql`, nie w `…/data`.
- **Migracje (1 pkt):** Flyway, wersjonowane skrypty `V1__init.sql` i `V2__seed.sql`, księga `flyway_schema_history`, `ddl-auto=validate`. Na wspólnej bazie blokada Flywaya sprawia, że migruje dokładnie jedna instancja, druga czeka i odpuszcza. Seedy w SQL mają stałe UUID-y, więc nic się nie duplikuje. Haczyk Boot 4: goły `flyway-core` nie działa, potrzebny `spring-boot-starter-flyway`.
- **Centralna konfiguracja (1 pkt):** Config Server w profilu `native` serwuje `config/<nazwa-aplikacji>.properties` pod `GET /{app}/{profil}`. Klienci mają jedną linię `spring.config.import=optional:configserver:${CONFIG_URL:...}`. Warstwy: env nad config-serwerem nad lokalnym plikiem. `optional:` utrzymuje tryb dev bez serwera. Gateway celowo nie jest klientem, bo zadanie 5 mówi „services".
- **Zimny start:** klienci odświeżają kopię rejestru co 30 s, więc tuż po starcie wołający może nie znać celu. W compose skróciliśmy interwał do 5 s jedną zmienną środowiskową.
