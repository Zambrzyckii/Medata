# web/catalog — agent context

Angular 22 frontend (standalone, signals, zoneless; file names without `.component` suffixes — v20+ style). Talks ONLY to the gateway via the relative `/api` (dev proxy `proxy.conf.json` → :8080; never hardcode service URLs).

- Run: `npm start` (:4200, needs the 3 backend services up); quality: `npm run build`, `npx ng lint`, `npm run format`. Container: multi-stage `Dockerfile` (Node 22 build → NGINX) + `nginx/default.conf.template` (envsubst at startup: `GATEWAY_URL` proxy target, `NGINX_PORT`; `try_files` SPA fallback), started from the root `compose.yaml` at :4200.
- Routes in `src/app/app.routes.ts`: static segments BEFORE dynamic (`new` before `:id`) at every depth — first match wins.
- `src/app/models.ts` mirrors backend DTO records exactly (list wrappers; test-details field is `category`, not `categoryName`). Check the Java records before changing shapes.
- Forms: typed reactive (`NonNullableFormBuilder`, `patchValue` for pre-population, `getRawValue()` as payload).
- Diagnostic chain: dev-proxy 502 = gateway down; 404 = proxy not wired or no gateway route; 500 = backend service down; silent empty list = API error (no error UI yet — deliberate gap).
- Docs: `docs/{pl,en}/README.md`, keep 1:1. Global rules (user types all code; agent edits docs only): root `/CLAUDE.md`.
