# Medata — rejestr decyzji

> Wersja polska. Angielski odpowiednik 1:1: [../en/DECISIONS.md](../en/DECISIONS.md)

| Data | Decyzja | Autor | Uzasadnienie |
|---|---|---|---|
| 2026-08-19 | Domena: laboratorium diagnostyczne (LIS); model bazowy `TestCategory` (1) → `LabTest` (N) | użytkownik (wybór z opcji) | Zrozumiała domena, naturalne kierunki rozbudowy, wartość portfolio |
| 2026-09-26 | Nazwa projektu: **Medata** (Med + data) | użytkownik | Krótka, oddaje charakter danych medycznych |
| 2026-09-26 | Zasięg wizji: wewnętrzny LIS + portal pacjenta; portal lekarza odłożony do backlogu | użytkownik | Balans między efektem a realnością ukończenia solo |
| 2026-09-26 | Pierwsza rozbudowa po labach: zlecenia i wyniki z flagowaniem | użytkownik | Serce domeny — fundament pod resztę feature'ów |
| 2026-09-26 | Dokumentacja dwujęzyczna PL/EN 1:1 od początku | użytkownik | Wygoda pracy + gotowość publicznego repo |
| 2026-09-26 | Dokumentacja = pełnoprawny obywatel: struktura `docs/{pl,en}` w roocie + własne docs każdego modułu; agent pielęgnuje ją automatycznie | użytkownik | Duży projekt wymaga dokumentacji, by dało się nad nim pracować |
| 2026-09-26 | Commit dokładnie na koniec każdego labu, z wyraźnym sygnałem agenta; historia etapami dla prowadzącego | użytkownik | Prezentacja kolejnych etapów, nie gotowego produktu |
| 2026-09-26 | Backlog feature'ów zamknięty — obecna wizja wystarcza | użytkownik | Praca na tygodnie już jest; koniec dosypywania pomysłów |
| 2026-09-26 | Conventional Commits po angielsku; tagi `lab-1`…`lab-7` | Claude (propozycja, do weta) | Czytelna historia + łatwe pokazywanie etapów checkoutem tagu |
| 2026-09-26 | Faza CORE liniowo na `main`; faza FUTURE na gałęziach `feat/*` | Claude (propozycja, do weta) | Laby budują na sobie; liniowa historia czytelna dla prowadzącego |
| 2026-09-26 | CLAUDE.md w roocie i modułach po angielsku | Claude (propozycja, do weta) | Artefakt narzędziowy jak kod; repo docelowo publiczne |
| 2026-09-26 | Pakiety Javy: `com.medata.<module>` | Claude (propozycja, do weta) | Standardowa konwencja odwróconej domeny |
| 2026-09-26 | Pakiet konwencji rozwijalności przyjęty w całości, aktywacja etapami: README (EN+PL), zasada jednej komendy, `.editorconfig`, checklista granicy labu (teraz); Spotless + Maven Wrapper (lab 1); Mermaid, ADR, CI (lab 2); OpenAPI (lab 3); szablon docs modułu (lab 4); ESLint/Prettier (lab 5); `.env.example` (lab 6); CONTRIBUTING (publikacja) | użytkownik (propozycje Claude'a) | Maksymalna łatwość rozwoju i wejścia osób z zewnątrz |
| 2026-09-26 | Kod labów 1–3 w podkatalogu `catalog/` (projekt Maven, pakiet `com.medata.catalog`), nie w roocie repo | Claude (propozycja, do weta) | Root = poziom "solution" (docs, README, compose); od labu 4 obok siebie staną serwisy, gateway i frontend |
| 2026-09-26 | Zwykłe treści commitów zamiast Conventional Commits (tagi `lab-N` zostają) | użytkownik (weto propozycji Claude'a) | Prostota — historia gita to praca użytkownika |
| 2026-09-26 | Spring Boot 4.0.8 na laby 2+ (linia 4.0, nie najnowsza 4.1) | Claude, zaakceptowane przez użytkownika | Parowanie ze stabilnym trainem Spring Cloud `2025.1.x` potrzebnym od labu 4; do ponownej weryfikacji przy labie 4 |
| 2026-09-26 | `Main.java` z labu 1 usunięty na starcie labu 2 — demo zadań 2–7 żyje w tagu `lab-1` | Claude, zaakceptowane przez użytkownika | Lab 2 go nie potrzebuje; tagi istnieją po to, by każdy etap był odtwarzalny |
| 2026-09-27 | Walidacja danych wejściowych w warstwie serwisu (`LabTestService.save`: nazwa/jednostka niepuste, min ≤ max, cena ≥ 0), runner tylko prezentuje błąd | użytkownik (zlecenie), projekt Claude | Reguły biznesowe mają mieszkać w serwisach — skorzysta z nich też REST w labie 3 |
| 2026-09-27 | `ConsoleRunner` usunięty na starcie labu 3 (żyje w tagu `lab-2`) | Claude, zaakceptowane przez użytkownika | W aplikacji webowej pętla stdin traci sens; instrukcja labu 3 go nie wymaga |
| 2026-09-27 | Kształt API: tworzenie badań tylko przez `POST /categories/{id}/tests`, odczyt/edycja płasko `/api/tests/{id}`, DTO kolekcji jako rekordy-opakowania | Claude (projekt) | Hierarchia wymusza „element zawsze w kategorii"; płaski dostęp po globalnie unikalnym id jest prostszy |
| 2026-09-27 | Open Session In View zostaje na domyślnym `true` | Claude (pragmatyzm labowy) | Mapowanie leniwych relacji w kontrolerach bez dodatkowej maszynerii; trade-off opisany w ściądze labu 3 |
| 2026-09-27 | Monorepo poliglotyczne: katalog `services/` z kontraktem katalogu serwisu (własny build, run, docs, CLAUDE.md), przyszłe `web/` i `deploy/`, ŻADNEGO wspólnego parent-pom; CI z matrixem per serwis | użytkownik (kierunek) + Claude (projekt) | Docelowo kilkanaście serwisów w różnych językach i technologiach — struktura repo nie może przywiązać całości do jednego ekosystemu |
| 2026-09-27 | Podział labu 4: `services/category` (port 8081, `com.medata.category`) i `services/lab-test` (port 8082, `com.medata.labtest`); gateway przejmie 8080; replika kategorii = tylko `id` + `name`; zdarzenia tylko add/remove (bez update — litera instrukcji); komunikacja `RestClient`; stałe UUID-y w danych startowych | użytkownik (akceptacja rekomendacji Claude'a) | Minimalna złożoność zgodna z instrukcją; luka „nieaktualna nazwa w replice" świadoma i odnotowana |
