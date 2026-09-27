# Lab 3 — Spring MVC REST: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-3.md](../../en/labs/lab-3.md)

## Co powstało

REST API katalogu: 6 DTO (2 encje × create/update, read, list), 2 kontrolery `@RestController`, globalny handler błędów, kaskada usuwania, `catalog/request.http` (17 żądań, w tym przypadki błędne), springdoc/Swagger UI — komplet 3 zadań (8/8 pkt).
Uruchomienie: `cd catalog && ./mvnw spring-boot:run`; Swagger: `http://localhost:8080/swagger-ui.html`.

## Sekcje krytyczne — o co mogą zapytać

- **Czemu DTO create/update nie ma id ani kategorii:** id generuje aplikacja (`UUID.randomUUID()` w encji — „klient" z labu 2 to nasza aplikacja względem bazy, nie klient HTTP), a kategoria wynika z adresu `/categories/{id}/tests` — dokładnie jak w instrukcji.
- **DTO kolekcji** to osobne rekordy-opakowania (`{"categories":[{id,name}]}`) — tylko id + przyjazna nazwa, zgodnie z wymaganiem.
- **Hierarchia adresów wymusza reguły:** badanie tworzy się WYŁĄCZNIE przez `POST /api/categories/{id}/tests` (endpoint `POST /api/tests` nie istnieje) — „element zawsze w kategorii" gwarantuje kształt API. Odczyt/edycja pojedynczego badania płasko: `/api/tests/{id}`.
- **Pusta vs nieistniejąca kategoria:** najpierw `findById` kategorii — brak → `404`; jest, ale bez badań → `200` + pusta lista. Idiom: `Optional.map(...).orElse(ResponseEntity.notFound().build())`.
- **Kody:** GET `200/404`, POST `201` (+ nagłówek `Location`), PUT/DELETE `204/404`, walidacja `400`.
- **Kaskada:** `cascade = CascadeType.REMOVE` + `orphanRemoval = true` na `@OneToMany` — Hibernate usuwa badania osobnymi DELETE-ami z poziomu aplikacji (nie constraintem `ON DELETE CASCADE` w bazie — widać w logach SQL).
- **`GlobalExceptionHandler` (`@RestControllerAdvice`):** walidacja z warstwy serwisu (lab 2) chroni REST bez zmian — `IllegalArgumentException` → `400` + `{"message":...}`.
- **Open Session In View:** mapowanie `getCategory().getName()` w kontrolerze działa, bo Boot domyślnie trzyma sesję JPA otwartą przez całe żądanie; w runnerze konsolowym (lab 2) skończyłoby się `LazyInitializationException`. Trade-off: wygoda vs dłużej trzymane połączenie i maskowanie N+1.
- **springdoc** generuje OpenAPI/Swagger UI z samych sygnatur kontrolerów (odpowiednik Swashbuckle); `request.http` odpalane przez VS Code REST Client (HTTP Client w IntelliJ wymaga Ultimate).
