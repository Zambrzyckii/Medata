# ADR-002: Spring Boot — the 4.0 line

- **Status:** accepted (lab 2) · PL: [../../pl/adr/002-spring-boot-4-0-line.md](../../pl/adr/002-spring-boot-4-0-line.md)
- **Context:** one Boot version is needed for labs 2–7; from lab 4 Spring Cloud joins (gateway, discovery), which ships in release trains paired with a specific Boot line.
- **Decision:** the 4.0 line (starting at 4.0.8), deliberately not the newest 4.1.
- **Consequences:** the stable Spring Cloud `2025.1.x` train is paired with Boot 4.0 — no compatibility risk in labs 4 and 7. Pairing VERIFIED at lab 4 (2026-10-04): the gateway builds and runs on Boot 4.0.8 + the `spring-cloud-dependencies 2025.1.3` BOM. Java 25 fully supported.
