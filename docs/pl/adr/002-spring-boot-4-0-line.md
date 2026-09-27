# ADR-002: Spring Boot — linia 4.0

- **Status:** przyjęta (lab 2) · EN: [../../en/adr/002-spring-boot-4-0-line.md](../../en/adr/002-spring-boot-4-0-line.md)
- **Kontekst:** potrzebna jedna wersja Boota na laby 2–7; od labu 4 dochodzi Spring Cloud (gateway, discovery), który wydaje się w trainach parowanych z konkretną linią Boota.
- **Decyzja:** linia 4.0 (start: 4.0.8), świadomie nie najnowsza 4.1.
- **Konsekwencje:** stabilny train Spring Cloud `2025.1.x` jest sparowany z Bootem 4.0 — brak ryzyka niezgodności w labach 4 i 7. Parowanie do ponownej weryfikacji przy labie 4. Java 25 w pełni wspierana.
