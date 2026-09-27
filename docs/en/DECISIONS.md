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
