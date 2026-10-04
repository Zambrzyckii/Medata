# Frontend web/catalog

> Wersja polska. Angielski odpowiednik 1:1: [../en/README.md](../en/README.md)

## Przegląd

Frontend katalogu badań: Angular 22 (komponenty standalone, signals, tryb zoneless), 7 routowanych widoków CRUD dla kategorii i badań. Rozmawia **wyłącznie z gatewayem** (:8080) — w dev przez proxy dev-servera. Szkielet z `ng new`; cała logika pisana ręcznie.

## Uruchomienie

```bash
npm start            # dev-server na :4200 (wymaga działających 3 serwisów backendu!)
npm run build        # bundle produkcyjny do dist/catalog
npx ng lint          # ESLint
npm run format       # Prettier
```

W kontenerach (lab 6) frontend jedzie jako obraz NGINX budowany tutejszym multi-stage `Dockerfile` (Node 22 buduje bundle → `nginx:alpine` go serwuje) i startuje z rootowego `compose.yaml` (`docker compose up --build`, port na hoście :4200).

## Konfiguracja

| Plik | Rola |
|---|---|
| `proxy.conf.json` | dev-proxy: `/api` → `http://localhost:8080` (zamiast CORS na gatewayu; w kontenerze tę rolę pełni NGINX) |
| `angular.json` → `serve.options.proxyConfig` | wpięcie proxy w `npm start` |
| `nginx/default.conf.template` | konfiguracja NGINX kontenera: `envsubst` wypełnia ją przy starcie zmiennymi środowiskowymi; `location /api/` → `proxy_pass` na gateway, `try_files … /index.html` = fallback SPA dla deep linków |
| `eslint.config.js`, `.prettierrc` | konwencja jakości labu 5 |

Zmienne środowiskowe kontenera (ustawiane w `compose.yaml`, defaulty zaszyte w obrazie): `GATEWAY_URL` (cel proxy, compose ustawia `http://gateway:8080`) i `NGINX_PORT` (port nasłuchu, `80`; `EXPOSE 80`, na hoście publikowany jako :4200).

## Model danych

`src/app/models.ts` — interfejsy TS lustrzane do rekordów DTO backendu (w tym opakowania list `{categories:[…]}`/`{tests:[…]}` i pole `category` w szczegółach badania). UUID → `string`, `BigDecimal` → `number` (kompromis labowy).

## Widoki (trasy)

| Trasa | Widok (zadanie instrukcji) |
|---|---|
| `/categories` | lista kategorii + usuwanie (1) |
| `/categories/new` | formularz dodania kategorii (2) |
| `/categories/:categoryId/edit` | edycja kategorii, prepopulowana (3) |
| `/categories/:categoryId` | szczegóły kategorii + lista badań + usuwanie badania (4) |
| `/categories/:categoryId/tests/new` | dodanie badania (5) |
| `/categories/:categoryId/tests/:testId/edit` | edycja badania, prepopulowana (6) |
| `/categories/:categoryId/tests/:testId` | szczegóły badania (7) |

Kolejność tras: statyczne segmenty przed dynamicznymi (`new` przed `:id`) — router bierze pierwszą pasującą.

## Decyzje

- Typed reactive forms (`NonNullableFormBuilder`, `patchValue`, `getRawValue()` zgodny z payloadami) — prepopulowanie zadań 3 i 6.
- `Api` (`HttpClient`, `inject()`) z względnym `baseUrl='/api'` — kod wolny od adresów środowisk.
- Znany skrót fazy labowej: brak UI błędów API (cicha porażka przy np. 400) — do FUTURE.
