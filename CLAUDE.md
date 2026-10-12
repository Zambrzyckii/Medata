# Medata

Medical Laboratory Information System (LIS) — a portfolio project growing out of the 7 labs of the "Internet Services Architectures" course (instruction PDFs live in the parent directory `/home/bob/Mikro`).

## Start here (read before any work)

- Current state & next step: `docs/en/STATE.md` (PL: `docs/pl/STATE.md`)
- Product vision & lab requirements: `docs/en/VISION.md`
- Working conventions (git, docs, quality, definition of done): `docs/en/CONVENTIONS.md`
- Decision log: `docs/en/DECISIONS.md`
- Architecture & diagrams: `docs/en/ARCHITECTURE.md` (update diagrams with every architecture change); ADRs in `docs/{pl,en}/adr/`
- Public front door: `README.md` / `README.pl.md` — keep Quick start and Requirements always current.
- Per-lab presentation cheat sheets: `docs/{pl,en}/labs/lab-N.md` — a ≤30-line summary of what was built and the likely exam questions; create one at every lab boundary.

## Non-negotiable rules

- Git is driven by the agent under the user's global rules (`~/.claude/rules/git.md`, since 2026-10-12): commit after each finished and verified logical step as `Zambrzyckii`, one imperative English sentence of 3 to 8 words, stage tags `lab-N`, no AI markers anywhere.
- The user writes ALL source code by hand: never create or edit code or config files directly. Show code in chat, one file per batch. Explain EVERY batch construct-by-construct at absolute-beginner level, always with C#/.NET equivalents (ideally a short side-by-side C# snippet) — the user is a professional .NET dev but a complete Java beginner, and must present the labs and understand everything. Documentation files (`docs/`, README, CLAUDE.md) are the exception: the agent edits those directly.
- When a lab is complete, walk the lab-boundary checklist (CONVENTIONS §8), then commit, tag `lab-N`, push and report **"LAB N COMPLETE"**. History must show stages, never a finished product at once.
- Documentation is as important as code: update `docs/` (PL **and** EN, 1:1 structure) and the relevant module docs in the same session as any change. Keep `STATE.md`, `DECISIONS.md`, `CHANGELOG.md` and every `CLAUDE.md` current — this is the agent's duty.
- Conventions activate in phases (CONVENTIONS §5–7): Spotless + Maven Wrapper from lab 1; Mermaid diagrams, ADRs, CI from lab 2; OpenAPI from lab 3; module docs template from lab 4; ESLint/Prettier from lab 5; `.env.example` and full one-command run from lab 6; CONTRIBUTING when going public. A session working on lab N must activate that lab's conventions.
- Code, names, comments, commits: English only. Conversation with the user: Polish.
- The user does not know the medical domain — explain every medical/health-tech concept in plain language before using it.
- CORE phase (labs): no features beyond the lab instructions. The feature backlog is closed — do not propose new features unprompted.

## Structure (polyglot monorepo)

- `docs/{pl,en}/` — project-wide documentation
- `services/<name>/` — one directory per standalone service (currently `category`, `lab-test`, `gateway`, `discovery`, `config`). **Service-directory contract:** each service ships its own build & run (Java: `mvnw`), its own `docs/{pl,en}/`, its own `CLAUDE.md`, and its own multi-stage Dockerfile (since lab 6). No shared parent build — future services may use other languages/stacks.
- `web/catalog` — the Angular frontend (lab 5; same per-directory contract: own build, docs, CLAUDE.md, Dockerfile → NGINX)
- Root `compose.yaml` — the one-command run (`docker compose up --build`): 9 containers (web, gateway, discovery, config, category, lab-test ×2, PostgreSQL ×2 on named volumes), published ports 4200/8080/8761/8888; credentials overridable via `.env` (`.env.example`). `deploy/` stays reserved for future orchestration beyond compose.
- CI: one workflow — a 5-service matrix plus a frontend job, triggered by `services/**` and `web/**` paths
