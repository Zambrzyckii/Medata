# ADR-001: UUID generowane przez klienta jako klucz główny

- **Status:** przyjęta (lab 2) · EN: [../../en/adr/001-client-generated-uuid.md](../../en/adr/001-client-generated-uuid.md)
- **Kontekst:** instrukcja labu 2 wymaga kluczy UUID nadawanych przez klienta (nie przez JPA ani bazę).
- **Decyzja:** `@Id private UUID id` bez `@GeneratedValue`; wartość nadaje inicjalizator pola `UUID.randomUUID()` (z `@Builder.Default`) w momencie budowy obiektu.
- **Konsekwencje:** `save()` Spring Data nie umie odróżnić encji nowej od istniejącej (id zawsze ustawione) → wybiera merge: dodatkowy SELECT przed INSERT. Akceptowalne w fazie labów; w razie potrzeby wydajnościowej encja może zaimplementować `Persistable.isNew()`.
