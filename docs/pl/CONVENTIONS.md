# Medata — konwencje pracy

> Wersja polska. Angielski odpowiednik 1:1: [../en/CONVENTIONS.md](../en/CONVENTIONS.md)

## 1. Rytm pracy i commity

- Repozytorium git prowadzi wyłącznie użytkownik: agent nie wykonuje `git commit`, `git checkout -b`, `git merge` ani `git push` — proponuje gotowe komendy i treści commitów. Odczyt (`status`, `diff`, `log`) jest dozwolony swobodnie.
- **Commit dokładnie w momencie ukończenia labu.** Gdy wszystkie zadania labu N działają, agent przechodzi checklistę granicy labu (sekcja 8) i wyraźnie sygnalizuje: **„LAB N UKOŃCZONY — czas na commit"**, podając proponowaną komendę, treść commita oraz tag `lab-N`. Dzięki temu każdy etap można pokazać prowadzącemu osobno (checkout tagu), zamiast od razu gotowego produktu z labu 7.
- Poza granicami labów commity powstają w naturalnych punktach (spójna, działająca zmiana) — agent proponuje moment i treść.
- Treść commitów: **Conventional Commits** po angielsku (`feat:`, `fix:`, `docs:`, `refactor:`, `chore:`, `test:`), tryb rozkazujący, opcjonalny zakres — np. `feat(catalog): add LabTest entity with builder`.
- Tagi etapów: `lab-1` … `lab-7` (adnotowane: `git tag -a lab-1 -m "Lab 1: Java SE"`).
- Gałęzie: faza CORE liniowo na `main` (laby budują na sobie, historia ma być czytelna dla prowadzącego); faza FUTURE — gałęzie `feat/<nazwa>`.

## 2. Dokumentacja — obywatel pierwszej kategorii

- Dokumentacja jest **równie ważna jak kod**. Zmiana nie jest ukończona, dopóki dokumentacja jej nie odzwierciedla.
- Agent aktualizuje dokumentację **automatycznie, bez osobnego polecenia**, w tej samej sesji co zmiana.
- Zawsze dwujęzycznie **PL/EN w układzie 1:1** (identyczna struktura nagłówków).
- Poziom szczegółowości: jednozdaniowy opis metody/endpointu wystarcza; kodu nie tłumaczymy linia po linii — od tego są komentarze w kodzie (tylko tam, gdzie coś jest nieoczywiste).
- Cel: nowa osoba ma zrozumieć z dokumentacji niemal wszystko — od ogólnej architektury, technologii i zasady działania całości po szczegóły w rodzaju konkretnych endpointów.

### Struktura dokumentacji

- Root `docs/pl/` i `docs/en/` — dokumentacja **ogółu** projektu; w miarę wzrostu grupowana w podkatalogi działowe (np. `architecture/`, `domain/`, `api/`).
- Każdy serwis/moduł ma dodatkowo **własne** `<moduł>/docs/pl/` i `<moduł>/docs/en/` — dokumentację szczegółową opisującą wyłącznie ten moduł (przegląd, konfiguracja, model danych, endpointy).

### Stałe dokumenty w root docs

| Plik | Rola | Aktualizacja |
|---|---|---|
| `VISION.md` | Wizja produktu, sekcje CORE i FUTURE | przy zmianach zakresu |
| `CONVENTIONS.md` | Ten dokument — zasady pracy | przy zmianach konwencji |
| `DECISIONS.md` | Rejestr decyzji (data, decyzja, autor, uzasadnienie) | przy każdej decyzji |
| `STATE.md` | **Aktualny stan** projektu: co istnieje, co działa, następny krok | po każdej sesji zmieniającej stan |
| `CHANGELOG.md` | Historia zmian na poziomie kamieni milowych (laby, feature'y) | przy kamieniach milowych |
| `ARCHITECTURE.md` | Architektura całości z diagramami | powstanie przy labie 2, rozbudowa przy labie 4 |
| `adr/NNN-*.md` | Rekordy decyzji architektonicznych (ADR) | od labu 2, przy dużych decyzjach |

## 3. CLAUDE.md — instrukcje dla agentów

- Root `CLAUDE.md`: przegląd projektu, kluczowe zasady, wskaźniki do dokumentacji.
- Każdy moduł: własny `CLAUDE.md` z kontekstem tego modułu.
- Odświeżane przy każdej zmianie konwencji, struktury lub istotnego kontekstu — dbałość o ich aktualność jest obowiązkiem agenta, nie użytkownika.
- Język: angielski (propozycja agenta — artefakt narzędziowy jak kod, repo docelowo publiczne).

## 4. Konwencje kodu

- Java: pakiety `com.medata.<module>`, standardowe konwencje Java/Spring, Lombok dozwolony (dopuszczony wprost w instrukcji labów).
- Angular: oficjalny style guide (kebab-case plików, PascalCase klas).
- Nazwy, komentarze, commity, gałęzie — wyłącznie po angielsku.
- Komentarze tylko przy nieoczywistym kodzie.
- Styl formatowania egzekwują narzędzia z sekcji 7, nie umowa słowna.
- Testy: w fazie CORE tylko jeśli wymagane instrukcją; zasady dla fazy FUTURE do ustalenia przy jej starcie.

## 5. Wejście do projektu (onboarding)

- Root `README.md` (EN) i `README.pl.md` (układ 1:1) — drzwi wejściowe repo: czym jest projekt, **Quick start**, **Requirements** (przypięte wersje narzędzi), linki do `docs/`. Aktualizowane przy każdej zmianie sposobu uruchamiania.
- **Zasada jednej komendy:** projekt zawsze da się uruchomić jedną komendą; jeśli uruchomienie wymaga wielu kroków, to bug dokumentacji. Pełnia od labu 6 (`docker compose up`).
- Maven Wrapper (`mvnw`) w repo od labu 1 — build bez systemowego Mavena.
- `CONTRIBUTING.md` (setup środowiska, testy, zasady PR) powstanie przy publikacji repo.

## 6. Architektura w dokumentacji

- `ARCHITECTURE.md` z diagramami **Mermaid** (tekstowe diagramy renderowane przez GitHuba): od labu 2 diagram kontenerów i ERD modelu danych; od labu 4 diagram sekwencji zdarzeń między serwisami. Zasada: **zmiana architektury bez aktualizacji diagramu = zmiana nieukończona.**
- **ADR** (Architecture Decision Record) od labu 2: `docs/{pl,en}/adr/NNN-tytul.md` — kontekst → rozważone opcje → decyzja → konsekwencje. `DECISIONS.md` pozostaje rejestrem drobniejszych decyzji i spisem treści ADR-ów.
- Szablon dokumentacji modułu (od labu 4, gdy powstaną moduły): Przegląd → Uruchomienie → Konfiguracja (tabela zmiennych środowiskowych) → Model danych → API → Decyzje lokalne. Każdy moduł według tego samego schematu.

## 7. Jakość egzekwowana automatycznie

- `.editorconfig` w roocie (od teraz) — wcięcia, kodowanie, końce linii jednakowe w każdym IDE.
- Java: Spotless + google-java-format (od labu 1).
- Angular: ESLint + Prettier (od labu 5).
- CI: GitHub Actions z buildem na każdy push + badge w README (od labu 2–3, gdy repo trafi na GitHuba).
- OpenAPI/springdoc (od labu 3): żywa dokumentacja API pod `/swagger-ui`; pliki `request.http` pozostają jako wykonywalne przykłady (wymóg labów).
- Sekrety: nigdy w repo; wzorzec `.env.example` (od labu 6). Dane medyczne w Medacie zawsze fikcyjne.

## 8. Checklista granicy labu

Przy ukończeniu labu N agent przechodzi listę i dopiero potem daje sygnał:

1. Wszystkie zadania z instrukcji labu N działają (zweryfikowane uruchomieniem).
2. Dokumentacja PL i EN zaktualizowana, struktura 1:1 zachowana.
3. `STATE.md`, `CHANGELOG.md`, `DECISIONS.md` odświeżone.
4. `CLAUDE.md` (root i modułów) aktualne.
5. Diagramy architektury aktualne (od labu 2).
6. Sygnał **„LAB N UKOŃCZONY — czas na commit"** + proponowana komenda, treść commita i tag `lab-N`.

## 9. Definition of done (każde zadanie)

1. Kod działa — zweryfikowane uruchomieniem lub testem.
2. Dokumentacja zaktualizowana w PL **i** EN (root i/lub moduł).
3. `STATE.md` odzwierciedla nowy stan; nowe decyzje dopisane do `DECISIONS.md`.
4. `CLAUDE.md` odświeżone, jeśli zmieniły się konwencje, struktura lub ważny kontekst.
5. Jeśli osiągnięto granicę labu — checklista z sekcji 8.
