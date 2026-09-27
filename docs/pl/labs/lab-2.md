# Lab 2 — Spring Boot & Spring Data JPA: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-2.md](../../en/labs/lab-2.md)

## Co powstało

`catalog` przebudowany na Spring Boot 4.0.8: encje JPA na H2 in-memory, repozytoria, serwisy z walidacją, initializer danych i konsolowy runner CRUD — komplet 5 zadań (8/8 pkt).
Uruchomienie: `cd catalog && ./mvnw spring-boot:run` (komendy: help, categories, tests, add, delete, stop).

## Sekcje krytyczne — o co mogą zapytać

- **DI / IoC (sedno labu):** kontener Springa sam tworzy obiekty (beany) i wstrzykuje zależności. `@SpringBootApplication` włącza component scanning pakietu — klasy z `@Component`/`@Service`/`@Repository` rejestrują się same. Wstrzykiwanie przez konstruktor: pole `final` + `@RequiredArgsConstructor` (Lombok generuje konstruktor); jeden konstruktor = Spring wstrzykuje bez `@Autowired`.
- **`@Component` vs `@Service` vs `@Repository`:** technicznie to samo (bean); różnica semantyczna — nazwa warstwy. `@Repository` dodatkowo tłumaczy wyjątki bazodanowe na hierarchię Springa.
- **Repozytoria bez implementacji:** Spring Data generuje proxy z interfejsu `extends JpaRepository<Encja, UUID>` (gotowy CRUD). `findAllByCategory` to *derived query* — zapytanie wyprowadzone z nazwy metody.
- **Po co serwisy, skoro delegują:** architektura warstwowa (runner → serwis → repozytorium); logika biznesowa ma dom — dowód: walidacja w `LabTestService.save` (nazwa/jednostka niepuste, min ≤ max, cena ≥ 0) rzuca `IllegalArgumentException`, runner łapie i wypisuje komunikat.
- **UUID od klienta (ADR-001):** `@Id` bez `@GeneratedValue` + `UUID.randomUUID()` w polu. Skutek: `save()` nie wie, czy encja nowa → merge → SELECT przed INSERT (widać w logach SQL).
- **Tabele i kolumny:** liczba mnoga jawnie przez `@Table(name = "lab_tests")`; snake_case za darmo — domyślna strategia nazewnicza tłumaczy `requiresFasting` → `requires_fasting` (zero `@Column`).
- **Lazy:** `@OneToMany` domyślnie LAZY, ale `@ManyToOne` domyślnie EAGER — nadpisane jawnie. Dotknięcie leniwego proxy po zamknięciu sesji = `LazyInitializationException`; dlatego wydruki nie sięgają po `category`.
- **Dwa runnery, kolejność:** `SampleDataInitializer` `@Order(1)` seeduje przez serwisy, `ConsoleRunner` `@Order(2)` rusza po nim. `stop` kończy pętlę → kontener sam grzecznie gasi aplikację (bez `System.exit`).
- **H2 (ADR-003):** baza SQL w procesie JVM (jak SQLite `:memory:`); `ddl-auto=create-drop` generuje schemat z encji przy starcie; dane żyją do zamknięcia — stąd seed przy każdym starcie.
