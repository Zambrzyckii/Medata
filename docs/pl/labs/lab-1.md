# Lab 1 — Java SE: ściąga do prezentacji

> Wersja polska. Angielski odpowiednik 1:1: [../../en/labs/lab-1.md](../../en/labs/lab-1.md)

## Co powstało

Aplikacja konsolowa `catalog/` (Maven, Java 25, Lombok): model `TestCategory` (1) → `LabTest` (N) + rekord `LabTestDto`; komplet 7 zadań instrukcji (8/8 pkt).
Uruchomienie: `cd catalog && ./mvnw compile && java -cp target/classes com.medata.catalog.Main`.

## Sekcje krytyczne — o co mogą zapytać

- **Czemu nie `@Data`?** Relacja dwustronna: `toString`/`equals`/`hashCode` obu klas wołałyby się nawzajem bez końca → `StackOverflowError`. Stąd celowane adnotacje z `exclude` na polach relacji; dodatkowo hash liczony z mutowalnej kolekcji psułby `HashSet` (zad. 3).
- **Builder (zad. 1):** `@Builder` Lomboka generuje wzorzec Builder; `@Builder.Default` zachowuje inicjalizator `new ArrayList<>()`, który builder inaczej by pominął.
- **Porównywanie (zad. 1):** „hash" = `equals`/`hashCode` z Lomboka; „natural ordering" = `Comparable.compareTo` po nazwie. Sort po innym polu: `Comparator.comparing(...)`. Stringi sortują się po kodach znaków — stąd `TSH` przed `Total cholesterol`.
- **Relacja dwustronna (zad. 2):** helper `TestCategory.addLabTest` ustawia obie strony naraz (dodaje do listy i ustawia wskazanie zwrotne) — bez niego graf obiektów by się rozjechał.
- **Stream API (zad. 3–5):** `flatMap` spłaszcza kategorie→badania; `Set` deduplikuje po `equals`/`hashCode` i nie gwarantuje kolejności; strumień jest leniwy (liczy dopiero operacja terminalna) i jednorazowy; `BigDecimal` porównujemy `compareTo`, bo Java nie ma przeciążania operatorów.
- **DTO (zad. 5):** `record` — niemutowalny, `toString`/`equals` z automatu; mapowanie statyczną fabryką `LabTestDto.fromEntity` podaną jako method reference do `map(...)`; sort naturalny po nazwie.
- **Serializacja (zad. 6):** `Serializable` to interfejs-znacznik; `ObjectOutputStream.writeObject` zapisuje cały graf obiektów i radzi sobie z cyklem relacji (śledzi tożsamość); `serialVersionUID` pilnuje zgodności pliku z wersją klasy; rzutowanie wyniku wymaga `@SuppressWarnings("unchecked")`, bo generyki znikają w runtime (type erasure); strumienie zamyka try-with-resources.
- **ForkJoinPool (zad. 7, 2 pkt):** `parallelStream()` domyślnie działa na wspólnej puli JVM (`commonPool`); własną pulę podpina się idiomem `pool.submit(() -> ...parallelStream()...).get()`. Obserwacja: nazwy wątków (1 worker vs 3), przeplatanie kategorii, czas ~3000 ms → ~1000 ms. Równoległość jest per kategoria (zewnętrzny strumień), nie per badanie. Pulę zamyka try-with-resources (`AutoCloseable` — wymóg „close the pool"); `InterruptedException` łapany w lambdzie z przywróceniem flagi przerwania.
