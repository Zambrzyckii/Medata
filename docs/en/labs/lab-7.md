# Lab 7 — advanced deployment: presentation cheat sheet

> English version. Polish 1:1 counterpart: [../../pl/labs/lab-7.md](../../pl/labs/lab-7.md)

## What was built

Two new mini applications (`discovery` = Eureka :8761, `config` = Config Server :8888, both logic-free, one annotation each), gateway routes on `lb://`, the event publisher through the registry, **2 peer lab-test instances** on one shared database, Flyway migrations and named volumes — the full task set 1–5 (7/7 pts).
Running: `docker compose up --build`, 9 containers. Artifacts to show: the Eureka dashboard (`:8761`), the served configuration (`:8888/category/default`), the logs `docker compose logs -f lab-test-1 lab-test-2`.

## Critical sections — likely questions

- **Registration (2 pts):** the Eureka client on the classpath registers the app under `spring.application.name` at startup, then heartbeats every 30 s. No heartbeats means eviction. The server is a standalone node (`register-with-eureka=false`, `fetch-registry=false`) because it has no peers to replicate with.
- **Instance id via env (task 5 requirement):** `eureka.instance.instance-id=${INSTANCE_ID:...}`, compose sets `lab-test-1`/`lab-test-2`. These names in the logs are the load-balancing proof.
- **Load balancing (2 pts):** `lb://lab-test` means "take the instances of this name from the local registry copy and pick round-robin". Client-side LB (the caller holds the phone book), as opposed to server-side (an exchange such as NGINX or a K8s Service). Proof: two hits on `/api/tests` land their SQL alternately in the two instance logs.
- **The trap that bit us:** the Eureka transport itself takes any `RestClient.Builder` from the context. When the only one was our `@LoadBalanced` bean, heartbeats went through the load balancer and died. The documented remedy is two beans, a plain `@Primary` one plus the `@LoadBalanced` one picked by qualifier (the annotation is meta-`@Qualifier`).
- **Volumes + shared database (1 pt):** two instances, one database, data survives `down` and `up`. The `postgres:18` image mounts its data at `/var/lib/postgresql`, not at `…/data`.
- **Migrations (1 pt):** Flyway, versioned scripts `V1__init.sql` and `V2__seed.sql`, the `flyway_schema_history` ledger, `ddl-auto=validate`. On a shared database the Flyway lock lets exactly one instance migrate while the other waits and skips. SQL seeds carry fixed UUIDs, so nothing ever duplicates. Boot 4 catch: bare `flyway-core` does nothing, you need `spring-boot-starter-flyway`.
- **Central configuration (1 pt):** the Config Server in the `native` profile serves `config/<application-name>.properties` under `GET /{app}/{profile}`. Clients carry one line, `spring.config.import=optional:configserver:${CONFIG_URL:...}`. Layering: env above the config server above the local file. `optional:` keeps dev mode alive without the server. The gateway is deliberately not a client because task 5 says "services".
- **Cold start:** clients refresh their registry copy every 30 s, so right after boot a caller may not know its target. In compose we shortened the interval to 5 s with a single environment variable.
