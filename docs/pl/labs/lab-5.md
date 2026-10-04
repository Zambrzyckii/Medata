# Lab 5 — frontend Angular: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-5.md](../../en/labs/lab-5.md)

## Co powstało

`web/catalog` — Angular 22 (standalone, signals, zoneless), **7 routowanych widoków** CRUD: lista/dodanie/edycja/szczegóły kategorii oraz dodanie/edycja/szczegóły badania — komplet zadań (9/9 pkt). Cały ruch przez gateway: względny `/api` + dev-proxy (`proxy.conf.json` → :8080).
Uruchomienie: 3 serwisy backendu + `npm start` → `http://localhost:4200`.

## Sekcje krytyczne — o co mogą zapytać

- **Kolejność tras:** router bierze PIERWSZĄ pasującą od góry — `categories/new` musi stać przed `categories/:categoryId` (i `tests/new` przed `tests/:testId`), inaczej „new" zostanie potraktowane jak id. Ta sama zasada co w trasach gatewaya z labu 4 — jedna lekcja, dwie technologie.
- **Path paramy (zadania 3, 5–7):** `inject(ActivatedRoute).snapshot.paramMap.get('categoryId')`; w widokach badań DWA paramy z jednej mapy. `snapshot` wystarcza, bo komponent powstaje na nowo przy każdej nawigacji; strumień `paramMap` byłby potrzebny przy przejściach „ta sama trasa, inny param".
- **Prepopulowanie (2×2 pkt):** GET encji → `patchValue` na typed reactive form; `patchValue` toleruje podzbiór pól, `setValue` żąda kompletu.
- **Typed forms:** `NonNullableFormBuilder` → `getRawValue()` ma dokładnie kształt payloadu API (interfejsy TS lustrzane do rekordów DTO; pułapka: pole `category`, nie `categoryName`). `input type="number"` → `NumberValueAccessor` → liczby, nie stringi.
- **Czemu dev-proxy zamiast CORS:** zero zmian w backendzie, a w labie 6 tę samą rolę przejmie NGINX; kod zna tylko względny `/api`.
- **Łańcuch diagnostyczny:** 502 z dev-proxy = gateway leży; 404 = proxy niewpięte/brak trasy; 500 = serwis docelowy leży; pusta lista bez błędu = cicha porażka (brak UI błędów — świadomy skrót fazy labowej).
- **Signals + zoneless:** signal mówi frameworkowi „przerysuj" (bez Zone.js); nowy control flow `@for (…; track id)` + `@empty`, `@if (x(); as x)`.
- **Walidacja = dwie warstwy:** front (`Validators.required`/`min`) to UX, prawda należy do backendu (min>max → 400 z `GlobalExceptionHandler` labu 3).
- **Usunięcie kategorii z UI** uruchamia całą architekturę labu 4: gateway → category → zdarzenie → replika → kaskada badań.
