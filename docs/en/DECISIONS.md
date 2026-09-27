# Medata — Decision Log

> English version. Polish 1:1 counterpart: [../pl/DECISIONS.md](../pl/DECISIONS.md)

| Date | Decision | Author | Rationale |
|---|---|---|---|
| 2026-08-19 | Domain: diagnostic laboratory (LIS); base model `TestCategory` (1) → `LabTest` (N) | user (picked from options) | Understandable domain, natural expansion paths, portfolio value |
| 2026-09-26 | Project name: **Medata** (Med + data) | user | Short, reflects the medical-data character |
| 2026-09-26 | Vision scope: internal LIS + patient portal; physician portal deferred to the backlog | user | Balance between impact and solo feasibility |
| 2026-09-26 | First post-lab expansion: orders and results with flagging | user | The heart of the domain — foundation for the remaining features |
| 2026-09-26 | Bilingual PL/EN 1:1 documentation from the start | user | Working comfort + public-repo readiness |
| 2026-09-26 | Documentation = first-class citizen: root `docs/{pl,en}` structure + each module's own docs; the agent maintains it automatically | user | A large project needs documentation to stay workable |
| 2026-09-26 | A commit exactly at the end of each lab, with a clear agent signal; stage-by-stage history for the instructor | user | Presenting successive stages, not a finished product |
| 2026-09-26 | Feature backlog closed — the current vision is enough | user | Weeks of work already; no more idea dumping |
| 2026-09-26 | Conventional Commits in English; tags `lab-1`…`lab-7` | Claude (proposal, open to veto) | Readable history + easy stage demos via tag checkout |
| 2026-09-26 | CORE phase linear on `main`; FUTURE phase on `feat/*` branches | Claude (proposal, open to veto) | Labs build on each other; linear history readable for the instructor |
| 2026-09-26 | Root and module CLAUDE.md in English | Claude (proposal, open to veto) | A tooling artifact like code; the repo is meant to be public |
| 2026-09-26 | Java packages: `com.medata.<module>` | Claude (proposal, open to veto) | Standard reverse-domain convention |
| 2026-09-26 | Growability-conventions package adopted in full, phased activation: README (EN+PL), one-command rule, `.editorconfig`, lab-boundary checklist (now); Spotless + Maven Wrapper (lab 1); Mermaid, ADR, CI (lab 2); OpenAPI (lab 3); module docs template (lab 4); ESLint/Prettier (lab 5); `.env.example` (lab 6); CONTRIBUTING (going public) | user (Claude's proposals) | Maximum ease of development and external onboarding |
| 2026-09-26 | Labs 1–3 code lives in the `catalog/` subdirectory (Maven project, package `com.medata.catalog`), not in the repo root | Claude (proposal, veto-able) | Root = "solution" level (docs, README, compose); from lab 4 services, gateway and frontend will sit side by side |
| 2026-09-26 | Plain commit messages instead of Conventional Commits (`lab-N` tags stay) | user (veto of Claude's proposal) | Simplicity — git history is the user's own work |
| 2026-09-26 | Spring Boot 4.0.8 for labs 2+ (the 4.0 line, not the newest 4.1) | Claude, accepted by the user | Pairs with the stable Spring Cloud `2025.1.x` train needed from lab 4; to be re-verified at lab 4 |
| 2026-09-26 | Lab 1's `Main.java` deleted at the start of lab 2 — the tasks 2–7 demo lives in the `lab-1` tag | Claude, accepted by the user | Lab 2 does not need it; tags exist precisely so every stage stays reproducible |
| 2026-09-27 | Input validation in the service layer (`LabTestService.save`: name/unit not blank, min ≤ max, price ≥ 0), the runner only presents the error | user (request), designed by Claude | Business rules belong in services — lab 3 REST will reuse them |
| 2026-09-27 | `ConsoleRunner` deleted at the start of lab 3 (lives in the `lab-2` tag) | Claude, accepted by the user | A stdin loop makes no sense in a web app; the lab 3 instruction does not require it |
| 2026-09-27 | API shape: tests created only via `POST /categories/{id}/tests`, single-test read/edit flat at `/api/tests/{id}`, collection DTOs as wrapper records | Claude (design) | The hierarchy enforces "an element always belongs to a category"; flat access by a globally unique id is simpler |
| 2026-09-27 | Open Session In View stays at the default `true` | Claude (lab pragmatism) | Lazy-relation mapping in controllers without extra machinery; trade-off described in the lab 3 cheat sheet |
| 2026-09-27 | Polyglot monorepo: a `services/` directory with a service-directory contract (own build, run, docs, CLAUDE.md), future `web/` and `deploy/`, NO shared parent-pom; CI with a per-service matrix | user (direction) + Claude (design) | Ultimately a dozen-plus services in different languages and technologies — the repo structure must not tie everything to one ecosystem |
| 2026-09-27 | Lab 4 split: `services/category` (port 8081, `com.medata.category`) and `services/lab-test` (port 8082, `com.medata.labtest`); the gateway will take 8080; category replica = `id` + `name` only; events only for add/remove (no update — letter of the instruction); `RestClient` communication; fixed UUIDs in seed data | user (accepted Claude's recommendations) | Minimal complexity matching the instruction; the "stale name in the replica" gap is deliberate and recorded |
