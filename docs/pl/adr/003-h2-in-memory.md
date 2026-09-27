# ADR-003: H2 in-memory w fazie labowej

- **Status:** przyjęta (lab 2) · EN: [../../en/adr/003-h2-in-memory.md](../../en/adr/003-h2-in-memory.md)
- **Kontekst:** instrukcja labu 2 wymaga bazy H2 in-memory; faza labowa ma nie wymagać żadnej instalacji.
- **Decyzja:** `jdbc:h2:mem:catalog` + `ddl-auto=create-drop` (schemat generowany z adnotacji encji przy każdym starcie) + initializer danych przykładowych.
- **Konsekwencje:** baza żyje w RAM-ie procesu i znika przy zamknięciu — stąd seed przy każdym starcie i powtarzalny stan. Od labu 6 planowane zewnętrzne bazy w kontenerach (zmiana sterownika i connection stringa; kod JPA bez zmian).
