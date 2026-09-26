# Medata — Product Vision

> English version. Polish 1:1 counterpart: [../pl/VISION.md](../pl/VISION.md)

## 1. What is Medata

Medata (from "Med" + "data") is an information system for a diagnostic laboratory — a **LIS** (Laboratory Information System). It covers the journey from a catalog of lab tests with reference ranges, through patient orders and results with automatic flagging of abnormalities, up to a portal where patients browse their results and trends over time.

The project is built in stages:

1. **CORE phase** — a minimal skeleton passing all 7 laboratories of the "Internet Services Architectures" course (Gdańsk University of Technology) with 100% of points.
2. **FUTURE phase** — expansion into a full-fledged portfolio product following the backlog in section 5.

## 2. Glossary

- **LIS (Laboratory Information System)** — software supporting the work of a diagnostic laboratory: tests, orders, results, samples.
- **Lab test** — a single measurement performed on patient material, e.g. blood glucose.
- **Panel / test package** — a set of tests ordered together, e.g. a lipid panel (total cholesterol, HDL, LDL, triglycerides).
- **Reference range** — the min–max interval within which a result is considered "normal"; may depend on sex and age.
- **L/H flag** — marks a result below (Low) or above (High) the reference range.
- **Critical value** — a result so extreme it may be life-threatening; requires immediately notifying a physician.
- **Laboratory diagnostician** — a specialist authorized to approve results; in Poland a profession regulated by law.
- **Result authorization** — formal approval of a result by a diagnostician before it reaches the patient.
- **Delta check** — comparison of a result with the same patient's previous result; a sudden, unnatural jump suggests an error (e.g. swapped samples).
- **TAT (turnaround time)** — time from sample reception to result release; a key laboratory performance metric.
- **Quality control (QC)** — regular measurements of material with a known value, verifying that instruments measure correctly.
- **Levey-Jennings chart** — the standard QC chart: control measurements over time against allowed deviations.
- **Hemolysis** — breakdown of blood cells in a sample (e.g. due to improper collection); it distorts results, so the sample is rejected.
- **Collection point** — the place where material (e.g. blood) is collected from the patient.
- **Analyzer** — an automated laboratory instrument performing measurements and returning results.
- **FHIR** — the modern standard for exchanging medical data via APIs (JSON resources, e.g. Observation, DiagnosticReport).
- **HL7 v2** — an older, still ubiquitous standard for medical messages, including analyzer communication.
- **LOINC** — the international dictionary of codes identifying laboratory tests.
- **ICD-10** — the international classification of diseases; diagnosis codes appearing on orders.

## 3. System users (target state)

- **Receptionist** — admits the patient, registers the test order.
- **Technician** — performs the test, enters the result.
- **Diagnostician** — approves (authorizes) the result before release.
- **Laboratory manager** — reports, quality and performance oversight.
- **Administrator** — system and account configuration.
- **Patient** — browses their results in the portal.

In the CORE phase there is no login and no roles — the system serves a single anonymous user.

## 4. CORE section — the minimum to pass the labs (57 pts)

Data model throughout all labs: **`TestCategory` (1) → `LabTest` (N)**.

- `TestCategory`: `name`, `requiresFasting` (whether the test requires fasting)
- `LabTest`: `name`, `unit`, `referenceMin`, `referenceMax`, `price`

**CORE phase rule:** no features beyond the lab instructions. The only "luxury": names and the data model rooted in the LIS domain from the start, so expansion requires no rewrite.

- **Lab 1 (8 pts) — test catalog, Java SE console application (Maven):** entities with comparison mechanisms (hash + natural ordering) and text representation; builder pattern; a test DTO with the category name instead of the whole object; sample data created in code at startup; printing via nested forEach + lambdas; three Stream API pipelines (Set of all tests; filtering + sorting; mapping to DTOs + natural-order sorting into a List); writing and reading the collection to/from a binary file (serialization); parallel processing on a custom ForkJoinPool observing different pool sizes.
- **Lab 2 (8 pts) — data persistence (Spring Boot, Spring Data JPA, in-memory H2):** JPA entities (plural table names, snake_case, bidirectional relationships, lazy fetching of a category's elements, client-generated UUIDs); repositories with querying tests by category; services delegating to repositories; sample data initializer; console CRUD runner (list commands, list categories, list tests, add a test to a chosen category, delete a test, stop the application).
- **Lab 3 (8 pts) — REST API (Spring MVC):** separate DTOs for create/update (settable fields only), single-record reads, and collection reads (identifier + user-friendly name); a REST controller per entity with full CRUD, hierarchical resource addresses, proper HTTP methods and response codes; deleting a category deletes its tests; a test is always added in the context of a category; distinct responses for an empty vs. a non-existent category; all requests documented in `request.http` files.
- **Lab 4 (8 pts) — microservices:** a split into two applications with private databases — a category service and a test service (holding a simplified category replica to maintain the relationship); event-based REST communication on category add/delete (replica synchronization); Spring Cloud Gateway with routing rules to both services; `request.http` files updated with the gateway port.
- **Lab 5 (9 pts) — frontend (Angular with routing, communicating through the gateway):** seven views: category list (with deletion), add-category form, edit category (pre-populated form, path parameter), category details with the test list (with test deletion), add a test to a category, edit a test, test details.
- **Lab 6 (9 pts) — containerization (Docker + Docker Compose):** a frontend image on NGINX (application build + proxy configured via environment variables); images of both services on Eclipse Temurin (configuration via environment variables, declared ports); a Docker Compose configuration wiring all applications together; optionally (2 pts) external databases for both services.
- **Lab 7 (7 pts) — advanced deployment (everything in Docker Compose):** a discovery service where both services register, with two instances of the test service; load balancing through the gateway (logs prove different instances handle traffic); external databases with volumes, a shared database for instances of the same service; automatic table creation via migrations at startup; a centralized configuration service (location and instance id set via environment variables); the frontend communicates through the gateway.

## 5. FUTURE section — expansion backlog

### Stage 1 — Orders and results (first after the labs)

- Patient registry (100% fictional, generated data)
- Test order with a lifecycle: registered → material collected → in analysis → result entered → approved → released
- Entering numeric results
- Automatic L/H flagging against the reference range
- Critical values with an alert
- Reference ranges dependent on sex and age
- Patient result history
- Delta check — comparison with the previous result (error detection)
- Two-step flow: the technician enters, the diagnostician approves
- Result correction as a new version — a result never disappears

### Stage 2 — Accounts, roles, security

- Login; roles: receptionist, technician, diagnostician, manager, administrator, patient
- Per-operation permissions
- Audit log (who changed what and when)
- GDPR approach: data minimization, pseudonymization for statistics

### Stage 3 — Patient portal

- Patient account and access to own results
- Translating flags into plain language
- Trend charts over time
- PDF result report
- "Result ready" e-mail notification
- Order history; test catalog with prices and a cart (self-service test purchase)

### Stage 4 — Laboratory operations

- Collection point and sample registration; barcode/QR codes on tubes
- Sample tracking: collected → transport → accepted → rejected (e.g. hemolysis)
- Reagent inventory: stock levels, expiry dates, alerts
- Quality control (QC): control measurements, Levey-Jennings charts
- TAT report
- Test packages (lipid panel, liver panel) and discounts

### Stage 5 — Integrations and standards

- FHIR export (DiagnosticReport, Observation)
- LOINC codes for tests
- An analyzer simulator as a separate service streaming results
- ICD-10 on the order
- Public API / webhooks for clinics
- (conceptually) Polish e-referral (P1)

### Stage 6 — Analytics and differentiators

- Manager dashboard: test volume, revenue, TAT, % of out-of-range results
- Population statistics
- Anomaly detection / load prediction (ML)
- Export of anonymized research data
- Physician/clinic portal (idea deferred by the scope decision)

### Cross-cutting — portfolio quality

Tests, CI/CD, a public online demo, observability (logs, metrics, tracing), 1:1 PL/EN documentation, architecture decision records (ADR).

## 6. Project ground rules

- Bilingual PL/EN documentation in a 1:1 layout
- Code, names, commits, branches — English only
- Medical data always fictional (privacy by design)
- CORE phase kept minimal — expansion only after passing the labs
- A git tag after each lab (lab 4 splits the project into separate applications)
- Every medical concept enters the glossary before it appears in code
- Detailed working conventions (git, documentation, definition of done): [CONVENTIONS.md](CONVENTIONS.md)

## 7. How to use this document in a new session

In a new session, point Claude to this file (`medata/docs/en/VISION.md`) and the lab PDFs in `/home/bob/Mikro`. Start implementation with Lab 1 following section 4 (CORE); section 5 (FUTURE) begins only after all labs are passed.
