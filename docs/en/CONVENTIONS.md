# Medata — Working Conventions

> English version. Polish 1:1 counterpart: [../pl/CONVENTIONS.md](../pl/CONVENTIONS.md)

## 1. Work rhythm and commits

- The git repository is driven exclusively by the user: the agent never runs `git commit`, `git checkout -b`, `git merge` or `git push` — it proposes ready-made commands and commit messages. Read operations (`status`, `diff`, `log`) are freely allowed.
- **A commit exactly at the moment a lab is completed.** When all tasks of lab N work, the agent walks through the lab-boundary checklist (section 8) and clearly signals: **"LAB N COMPLETE — time to commit"**, providing the proposed command, the commit message and the `lab-N` tag. This way every stage can be shown to the instructor separately (tag checkout) instead of a finished lab-7 product.
- Between lab boundaries, commits happen at natural points (a coherent, working change) — the agent proposes the moment and the message.
- Commit messages: **Conventional Commits** in English (`feat:`, `fix:`, `docs:`, `refactor:`, `chore:`, `test:`), imperative mood, optional scope — e.g. `feat(catalog): add LabTest entity with builder`.
- Stage tags: `lab-1` … `lab-7` (annotated: `git tag -a lab-1 -m "Lab 1: Java SE"`).
- Branches: the CORE phase runs linearly on `main` (labs build on each other and the history must stay readable for the instructor); the FUTURE phase uses `feat/<name>` branches.

## 2. Documentation — a first-class citizen

- Documentation is **as important as code**. A change is not done until documentation reflects it.
- The agent updates documentation **automatically, without a separate request**, in the same session as the change.
- Always bilingual **PL/EN in a 1:1 layout** (identical heading structure).
- Level of detail: a one-sentence description of a method/endpoint is enough; code is never explained line by line — that is what in-code comments are for (and only where something is non-obvious).
- Goal: a new person should understand almost everything from the documentation alone — from the overall architecture, technologies and how the whole thing works, down to details such as specific endpoints.

### Documentation structure

- Root `docs/pl/` and `docs/en/` — documentation of the project **as a whole**; grouped into topical subfolders as it grows (e.g. `architecture/`, `domain/`, `api/`).
- Every service/module additionally has its **own** `<module>/docs/pl/` and `<module>/docs/en/` — detailed documentation describing only that module (overview, configuration, data model, endpoints).

### Standing documents in root docs

| File | Role | Updated |
|---|---|---|
| `VISION.md` | Product vision, CORE and FUTURE sections | on scope changes |
| `CONVENTIONS.md` | This document — working rules | on convention changes |
| `DECISIONS.md` | Decision log (date, decision, author, rationale) | on every decision |
| `STATE.md` | **Current state** of the project: what exists, what works, next step | after every state-changing session |
| `CHANGELOG.md` | Change history at milestone level (labs, features) | at milestones |
| `ARCHITECTURE.md` | Architecture of the whole with diagrams | created at lab 2, expanded at lab 4 |
| `adr/NNN-*.md` | Architecture Decision Records (ADR) | from lab 2, for major decisions |

## 3. CLAUDE.md — agent instructions

- Root `CLAUDE.md`: project overview, key rules, pointers to documentation.
- Every module: its own `CLAUDE.md` with that module's context.
- Refreshed on every change of conventions, structure or important context — keeping them current is the agent's duty, not the user's.
- Language: English (agent's proposal — a tooling artifact like code, and the repo is meant to be public).

## 4. Code conventions

- Java: packages `com.medata.<module>`, standard Java/Spring conventions, Lombok allowed (explicitly permitted by the lab instructions).
- Angular: the official style guide (kebab-case files, PascalCase classes).
- Names, comments, commits, branches — English only.
- Comments only where the code is non-obvious.
- Formatting style is enforced by the tools from section 7, not by verbal agreement.
- Tests: in the CORE phase only when an instruction requires them; FUTURE-phase rules to be set when that phase starts.

## 5. Project onboarding

- Root `README.md` (EN) and `README.pl.md` (1:1 layout) — the repo's front door: what the project is, **Quick start**, **Requirements** (pinned tool versions), links to `docs/`. Updated on every change to how the project runs.
- **The one-command rule:** the project can always be started with a single command; if starting it takes multiple steps, that is a documentation bug. Fully realized from lab 6 (`docker compose up`).
- Maven Wrapper (`mvnw`) in the repo from lab 1 — builds without a system Maven.
- `CONTRIBUTING.md` (environment setup, tests, PR rules) will be created when the repo goes public.

## 6. Architecture in documentation

- `ARCHITECTURE.md` with **Mermaid** diagrams (text-based diagrams rendered by GitHub): from lab 2 a container diagram and a data-model ERD; from lab 4 a sequence diagram of inter-service events. Rule: **an architecture change without a diagram update = an unfinished change.**
- **ADR** (Architecture Decision Record) from lab 2: `docs/{pl,en}/adr/NNN-title.md` — context → considered options → decision → consequences. `DECISIONS.md` remains the log of smaller decisions and the table of contents for ADRs.
- Module documentation template (from lab 4, once modules exist): Overview → Run → Configuration (environment-variable table) → Data model → API → Local decisions. Every module follows the same scheme.

## 7. Automatically enforced quality

- `.editorconfig` in the root (from now) — identical indentation, encoding and line endings in every IDE.
- Java: Spotless + google-java-format (from lab 1).
- Angular: ESLint + Prettier (from lab 5).
- CI: GitHub Actions building on every push + a badge in the README (from lab 2–3, once the repo lands on GitHub).
- OpenAPI/springdoc (from lab 3): living API documentation at `/swagger-ui`; `request.http` files remain as executable examples (a lab requirement).
- Secrets: never in the repo; the `.env.example` pattern (from lab 6). Medical data in Medata is always fictional.

## 8. Lab-boundary checklist

On completing lab N the agent walks through the list and only then signals:

1. All tasks from the lab N instruction work (verified by running).
2. PL and EN documentation updated, 1:1 structure preserved.
3. `STATE.md`, `CHANGELOG.md`, `DECISIONS.md` refreshed.
4. `CLAUDE.md` (root and modules) up to date.
5. Architecture diagrams up to date (from lab 2).
6. The signal **"LAB N COMPLETE — time to commit"** + the proposed command, commit message and `lab-N` tag.

## 9. Definition of done (every task)

1. The code works — verified by running it or by a test.
2. Documentation updated in PL **and** EN (root and/or module).
3. `STATE.md` reflects the new state; new decisions added to `DECISIONS.md`.
4. `CLAUDE.md` refreshed if conventions, structure or important context changed.
5. If a lab boundary was reached — the checklist from section 8.
