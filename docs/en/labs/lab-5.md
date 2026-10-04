# Lab 5 — Angular frontend: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-5.md](../../pl/labs/lab-5.md)

## What was built

`web/catalog` — Angular 22 (standalone, signals, zoneless), **7 routed CRUD views**: category list/add/edit/details plus test add/edit/details — all tasks complete (9/9 pts). All traffic through the gateway: a relative `/api` + dev proxy (`proxy.conf.json` → :8080).
Running: the 3 backend services + `npm start` → `http://localhost:4200`.

## Critical sections — likely questions

- **Route order:** the router takes the FIRST match top-down — `categories/new` must precede `categories/:categoryId` (and `tests/new` precede `tests/:testId`), or "new" gets treated as an id. The same rule as the lab-4 gateway routes — one lesson, two technologies.
- **Path params (tasks 3, 5–7):** `inject(ActivatedRoute).snapshot.paramMap.get('categoryId')`; test views read TWO params from one map. `snapshot` suffices because the component is recreated on every navigation; the `paramMap` stream would matter for "same route, different param" transitions.
- **Pre-population (2×2 pts):** GET the entity → `patchValue` on a typed reactive form; `patchValue` tolerates a subset of fields, `setValue` demands all.
- **Typed forms:** `NonNullableFormBuilder` → `getRawValue()` has exactly the API payload shape (TS interfaces mirror the DTO records; trap: the field is `category`, not `categoryName`). `input type="number"` → `NumberValueAccessor` → numbers, not strings.
- **Why a dev proxy instead of CORS:** zero backend changes, and in lab 6 NGINX takes the same role; the code only knows the relative `/api`.
- **Diagnostic chain:** 502 from the dev proxy = gateway down; 404 = proxy not wired / no route; 500 = target service down; a silently empty list = silent failure (no error UI — a deliberate lab-phase shortcut).
- **Signals + zoneless:** a signal tells the framework "repaint" (no Zone.js); new control flow `@for (…; track id)` + `@empty`, `@if (x(); as x)`.
- **Validation = two layers:** the front (`Validators.required`/`min`) is UX, the truth belongs to the backend (min>max → 400 from lab 3's `GlobalExceptionHandler`).
- **Deleting a category from the UI** exercises the whole lab-4 architecture: gateway → category → event → replica → test cascade.
