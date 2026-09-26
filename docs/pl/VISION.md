# Medata — wizja produktu

> Wersja polska. Angielski odpowiednik 1:1: [../en/VISION.md](../en/VISION.md)

## 1. Czym jest Medata

Medata (od „Med" + „data") to system informatyczny laboratorium diagnostycznego — **LIS** (Laboratory Information System). Obejmuje drogę od katalogu badań z zakresami referencyjnymi, przez zlecenia i wyniki pacjentów z automatycznym flagowaniem odchyleń, po portal, w którym pacjent przegląda swoje wyniki i ich trendy w czasie.

Projekt powstaje etapami:

1. **Faza CORE** — minimalny szkielet zaliczający 7 laboratoriów kursu „Internet Services Architectures" (Politechnika Gdańska) na 100% punktów.
2. **Faza FUTURE** — rozbudowa w pełnoprawny produkt portfolio według backlogu z sekcji 5.

## 2. Słowniczek pojęć

- **LIS (Laboratory Information System)** — system informatyczny obsługujący pracę laboratorium diagnostycznego: badania, zlecenia, wyniki, próbki.
- **Badanie laboratoryjne (lab test)** — pojedynczy pomiar wykonywany z materiału pacjenta, np. glukoza we krwi.
- **Panel / pakiet badań** — zestaw badań zlecanych razem, np. lipidogram (cholesterol całkowity, HDL, LDL, trójglicerydy).
- **Zakres referencyjny (reference range)** — widełki min–max, w których wynik uznaje się za „w normie"; mogą zależeć od płci i wieku.
- **Flaga L/H** — oznaczenie wyniku poniżej (Low) lub powyżej (High) zakresu referencyjnego.
- **Wartość krytyczna (critical value)** — wynik na tyle skrajny, że może zagrażać życiu; wymaga natychmiastowego powiadomienia lekarza.
- **Diagnosta laboratoryjny** — specjalista uprawniony do zatwierdzania wyników; w Polsce zawód regulowany ustawą.
- **Autoryzacja wyniku** — formalne zatwierdzenie wyniku przez diagnostę, zanim trafi do pacjenta.
- **Delta check** — porównanie wyniku z poprzednim wynikiem tego samego pacjenta; nagły, nienaturalny skok sugeruje błąd (np. zamienione próbki).
- **TAT (turnaround time)** — czas od przyjęcia próbki do wydania wyniku; kluczowa miara wydajności laboratorium.
- **Kontrola jakości (QC)** — regularne pomiary materiału o znanej wartości, sprawdzające, czy aparatura mierzy poprawnie.
- **Wykres Levey-Jenningsa** — standardowy wykres QC: pomiary kontrolne w czasie na tle dopuszczalnych odchyleń.
- **Hemoliza** — rozpad krwinek w próbce (np. przy nieprawidłowym pobraniu); zafałszowuje wyniki, więc próbkę się odrzuca.
- **Punkt pobrań** — miejsce, w którym pobiera się materiał (np. krew) od pacjenta.
- **Analizator** — automat laboratoryjny wykonujący pomiary i zwracający wyniki.
- **FHIR** — nowoczesny standard wymiany danych medycznych przez API (zasoby JSON, np. Observation, DiagnosticReport).
- **HL7 v2** — starszy, wciąż wszechobecny standard komunikatów medycznych, m.in. w komunikacji z analizatorami.
- **LOINC** — międzynarodowy słownik kodów identyfikujących badania laboratoryjne.
- **ICD-10** — międzynarodowa klasyfikacja chorób; kody rozpoznań pojawiające się na zleceniach.

## 3. Użytkownicy systemu (docelowo)

- **Rejestratorka** — przyjmuje pacjenta, rejestruje zlecenie badań.
- **Technik** — wykonuje badanie, wprowadza wynik.
- **Diagnosta** — zatwierdza (autoryzuje) wynik przed wydaniem.
- **Kierownik laboratorium** — raporty, nadzór nad jakością i wydajnością.
- **Administrator** — konfiguracja systemu i kont.
- **Pacjent** — przegląda swoje wyniki w portalu.

W fazie CORE nie ma logowania ani ról — system obsługuje jednego anonimowego użytkownika.

## 4. Sekcja CORE — minimum do zaliczenia labów (57 pkt)

Model danych przez wszystkie laby: **`TestCategory` (1) → `LabTest` (N)**.

- `TestCategory`: `name`, `requiresFasting` (czy badanie wykonuje się na czczo)
- `LabTest`: `name`, `unit`, `referenceMin`, `referenceMax`, `price`

**Zasada fazy CORE:** żadnych feature'ów spoza instrukcji labów. Jedyny „luksus": nazwy i model danych od początku osadzone w domenie LIS, żeby rozbudowa nie wymagała przepisywania.

- **Lab 1 (8 pkt) — katalog badań, aplikacja konsolowa Java SE (Maven):** encje z mechanizmami porównywania (hash + porządek naturalny) i reprezentacją tekstową; wzorzec builder; DTO badania z nazwą kategorii zamiast całego obiektu; dane przykładowe tworzone w kodzie na starcie; wydruk zagnieżdżonym forEach + lambdy; trzy pipeline'y Stream API (Set wszystkich badań; filtrowanie + sortowanie; mapowanie do DTO + sortowanie naturalne do List); zapis i odczyt kolekcji z pliku binarnego (serializacja); przetwarzanie równoległe na własnej puli ForkJoinPool z obserwacją różnych rozmiarów puli.
- **Lab 2 (8 pkt) — trwałość danych (Spring Boot, Spring Data JPA, H2 in-memory):** encje JPA (tabele w liczbie mnogiej, snake_case, relacje dwukierunkowe, leniwe pobieranie elementów kategorii, UUID nadawane przez klienta); repozytoria z wyszukiwaniem badań po kategorii; serwisy delegujące do repozytoriów; initializer danych przykładowych; konsolowy runner CRUD (lista komend, lista kategorii, lista badań, dodanie badania do wybranej kategorii, usunięcie badania, zatrzymanie aplikacji).
- **Lab 3 (8 pkt) — REST API (Spring MVC):** osobne DTO do tworzenia/aktualizacji (tylko pola ustawialne), odczytu pojedynczego rekordu i odczytu kolekcji (identyfikator + nazwa przyjazna); kontroler REST dla każdej encji z pełnym CRUD, hierarchicznymi adresami zasobów, poprawnymi metodami i kodami HTTP; usunięcie kategorii usuwa jej badania; badanie zawsze dodawane w kontekście kategorii; rozróżnienie odpowiedzi dla kategorii pustej i nieistniejącej; wszystkie żądania udokumentowane w plikach `request.http`.
- **Lab 4 (8 pkt) — mikroserwisy:** podział na dwie aplikacje z prywatnymi bazami — serwis kategorii i serwis badań (z uproszczoną repliką kategorii dla utrzymania relacji); komunikacja zdarzeniowa REST przy dodaniu/usunięciu kategorii (synchronizacja repliki); Spring Cloud Gateway z regułami routingu do obu serwisów; pliki `request.http` zaktualizowane o port gatewaya.
- **Lab 5 (9 pkt) — frontend (Angular z routingiem, komunikacja przez gateway):** siedem widoków: lista kategorii (z usuwaniem), formularz dodania kategorii, edycja kategorii (formularz prepopulowany, parametr ścieżki), szczegóły kategorii z listą badań (z usuwaniem badania), dodanie badania do kategorii, edycja badania, szczegóły badania.
- **Lab 6 (9 pkt) — konteneryzacja (Docker + Docker Compose):** obraz frontendu na NGINX (build aplikacji + proxy konfigurowane zmiennymi środowiskowymi); obrazy obu serwisów na Eclipse Temurin (konfiguracja przez zmienne środowiskowe, zadeklarowane porty); konfiguracja Docker Compose spinająca wszystkie aplikacje; opcjonalnie (2 pkt) zewnętrzne bazy danych dla obu serwisów.
- **Lab 7 (7 pkt) — deployment zaawansowany (całość w Docker Compose):** serwis discovery, w którym rejestrują się oba serwisy, z dwiema instancjami serwisu badań; load balancing przez gateway (logi dowodzą pracy różnych instancji); zewnętrzne bazy z wolumenami, wspólna baza dla instancji tego samego serwisu; automatyczne tworzenie tabel migracjami przy starcie; centralny serwis konfiguracji (lokalizacja i identyfikator instancji przez zmienne środowiskowe); frontend komunikuje się przez gateway.

## 5. Sekcja FUTURE — backlog rozbudowy

### Etap 1 — Zlecenia i wyniki (pierwszy po labach)

- Rejestr pacjentów (dane w 100% fikcyjne, generowane)
- Zlecenie badań z cyklem życia: zarejestrowane → materiał pobrany → w analizie → wynik wprowadzony → zatwierdzony → wydany
- Wprowadzanie wyników liczbowych
- Automatyczne flagowanie L/H względem zakresu referencyjnego
- Wartości krytyczne z alertem
- Zakresy referencyjne zależne od płci i wieku
- Historia wyników pacjenta
- Delta check — porównanie z poprzednim wynikiem (wykrywanie pomyłek)
- Dwuetapowość: technik wprowadza, diagnosta zatwierdza
- Korekta wyniku jako nowa wersja — wynik nigdy nie znika

### Etap 2 — Konta, role, bezpieczeństwo

- Logowanie; role: rejestratorka, technik, diagnosta, kierownik, administrator, pacjent
- Uprawnienia per operacja
- Dziennik audytu (kto, co, kiedy zmienił)
- Podejście RODO: minimalizacja danych, pseudonimizacja do statystyk

### Etap 3 — Portal pacjenta

- Konto pacjenta i podgląd własnych wyników
- Tłumaczenie flag „po ludzku"
- Wykresy trendów w czasie
- Raport wyników w PDF
- Powiadomienie e-mail „wynik gotowy"
- Historia zleceń; katalog badań z cenami i koszyk (samodzielny zakup badań)

### Etap 4 — Operacje laboratorium

- Punkt pobrań i rejestracja próbek; kody kreskowe/QR probówek
- Śledzenie próbki: pobrana → transport → przyjęta → odrzucona (np. hemoliza)
- Magazyn odczynników: stany, daty ważności, alerty
- Kontrola jakości (QC): pomiary kontrolne, wykresy Levey-Jenningsa
- Raport TAT
- Pakiety badań (lipidogram, panel wątrobowy) i rabaty

### Etap 5 — Integracje i standardy

- Eksport FHIR (DiagnosticReport, Observation)
- Kody LOINC dla badań
- Symulator analizatora jako osobny serwis strumieniujący wyniki
- ICD-10 na zleceniu
- Publiczne API / webhooki dla przychodni
- (koncepcyjnie) e-skierowanie P1

### Etap 6 — Analityka i wyróżniki

- Dashboard kierownika: wolumen badań, przychody, TAT, % wyników poza normą
- Statystyki populacyjne
- Wykrywanie anomalii / predykcja obciążenia (ML)
- Eksport zanonimizowanych danych badawczych
- Portal lekarza/przychodni (odłożony pomysł z decyzji o zasięgu)

### Przekrojowo — jakość portfolio

Testy, CI/CD, publiczne demo online, observability (logi, metryki, tracing), dokumentacja PL/EN 1:1, rejestr decyzji architektonicznych (ADR).

## 6. Zasady projektu

- Dokumentacja dwujęzyczna PL/EN w układzie 1:1
- Kod, nazwy, commity, branche — wyłącznie po angielsku
- Dane medyczne zawsze fikcyjne (privacy by design)
- Faza CORE minimalna — rozbudowa dopiero po zaliczeniu labów
- Git tag po każdym labie (lab 4 rozcina projekt na osobne aplikacje)
- Każde pojęcie medyczne trafia do słowniczka, zanim pojawi się w kodzie
- Szczegółowe zasady pracy (git, dokumentacja, definition of done): [CONVENTIONS.md](CONVENTIONS.md)

## 7. Jak używać tego dokumentu w nowej sesji

W nowej sesji wskaż Claude'owi ten plik (`medata/docs/pl/VISION.md`) oraz PDF-y labów w `/home/bob/Mikro`. Implementację zaczynaj od Labu 1 według sekcji 4 (CORE); sekcja 5 (FUTURE) wchodzi dopiero po zaliczeniu wszystkich labów.
