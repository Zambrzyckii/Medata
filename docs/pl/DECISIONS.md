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
