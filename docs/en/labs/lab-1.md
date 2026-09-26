# Lab 1 — Java SE: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-1.md](../../pl/labs/lab-1.md)

## What was built

Console application `catalog/` (Maven, Java 25, Lombok): model `TestCategory` (1) → `LabTest` (N) + record `LabTestDto`; all 7 instruction tasks complete (8/8 pts).
Run: `cd catalog && ./mvnw compile && java -cp target/classes com.medata.catalog.Main`.

## Critical sections — likely questions

- **Why not `@Data`?** Bidirectional relation: `toString`/`equals`/`hashCode` of both classes would call each other forever → `StackOverflowError`. Hence targeted annotations with `exclude` on relation fields; also a hash computed from a mutable collection would break `HashSet` (task 3).
- **Builder (task 1):** Lombok's `@Builder` generates the Builder pattern; `@Builder.Default` preserves the `new ArrayList<>()` initializer the builder would otherwise skip.
- **Comparison (task 1):** "hash" = Lombok's `equals`/`hashCode`; "natural ordering" = `Comparable.compareTo` by name. Sorting by another field: `Comparator.comparing(...)`. Strings sort by character codes — hence `TSH` before `Total cholesterol`.
- **Two-way relation (task 2):** the `TestCategory.addLabTest` helper sets both sides at once (adds to the list and sets the back-reference) — without it the object graph would drift apart.
- **Stream API (tasks 3–5):** `flatMap` flattens categories→tests; `Set` deduplicates via `equals`/`hashCode` and has no ordering guarantee; a stream is lazy (nothing runs until a terminal operation) and single-use; `BigDecimal` is compared with `compareTo` because Java has no operator overloading.
- **DTO (task 5):** `record` — immutable, `toString`/`equals` for free; mapping via the static factory `LabTestDto.fromEntity` passed as a method reference to `map(...)`; natural sort by name.
- **Serialization (task 6):** `Serializable` is a marker interface; `ObjectOutputStream.writeObject` stores the whole object graph and handles the relation cycle (tracks identity); `serialVersionUID` guards file-vs-class version compatibility; casting the result needs `@SuppressWarnings("unchecked")` because generics are erased at runtime (type erasure); streams are closed by try-with-resources.
- **ForkJoinPool (task 7, 2 pts):** `parallelStream()` runs on the JVM-wide `commonPool` by default; a custom pool is attached via the `pool.submit(() -> ...parallelStream()...).get()` idiom. Observation: thread names (1 worker vs 3), category interleaving, time ~3000 ms → ~1000 ms. Parallelism is per category (the outer stream), not per test. The pool is closed by try-with-resources (`AutoCloseable` — the "close the pool" requirement); `InterruptedException` is caught inside the lambda with the interrupt flag restored.
