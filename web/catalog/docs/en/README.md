# web/catalog frontend

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The test-catalog frontend: Angular 22 (standalone components, signals, zoneless mode), 7 routed CRUD views for categories and tests. Talks **only to the gateway** (:8080) — in dev through the dev-server proxy. Skeleton from `ng new`; all logic hand-written.

## Running

```bash
npm start            # dev server on :4200 (requires the 3 backend services running!)
npm run build        # production bundle into dist/catalog
npx ng lint          # ESLint
npm run format       # Prettier
```

## Configuration

| File | Role |
|---|---|
| `proxy.conf.json` | dev proxy: `/api` → `http://localhost:8080` (instead of CORS on the gateway; NGINX takes this role in lab 6) |
| `angular.json` → `serve.options.proxyConfig` | wires the proxy into `npm start` |
| `eslint.config.js`, `.prettierrc` | lab 5 quality convention |

## Data model

`src/app/models.ts` — TS interfaces mirroring the backend DTO records (including the list wrappers `{categories:[…]}`/`{tests:[…]}` and the `category` field in test details). UUID → `string`, `BigDecimal` → `number` (a lab-grade compromise).

## Views (routes)

| Route | View (instruction task) |
|---|---|
| `/categories` | category list + removal (1) |
| `/categories/new` | add-category form (2) |
| `/categories/:categoryId/edit` | category edit, pre-populated (3) |
| `/categories/:categoryId` | category details + test list + test removal (4) |
| `/categories/:categoryId/tests/new` | add test (5) |
| `/categories/:categoryId/tests/:testId/edit` | test edit, pre-populated (6) |
| `/categories/:categoryId/tests/:testId` | test details (7) |

Route order: static segments before dynamic ones (`new` before `:id`) — the router takes the first match.

## Decisions

- Typed reactive forms (`NonNullableFormBuilder`, `patchValue`, `getRawValue()` matching the payloads) — the pre-population of tasks 3 and 6.
- `Api` (`HttpClient`, `inject()`) with a relative `baseUrl='/api'` — code free of environment addresses.
- A known lab-phase shortcut: no API-error UI (silent failure on e.g. 400) — FUTURE.
