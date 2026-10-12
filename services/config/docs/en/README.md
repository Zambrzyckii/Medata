# config service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The centralized configuration server (Spring Cloud Config Server) on port **8888**. It serves per-application configuration over HTTP. The `category` and `lab-test` services fetch their database and discovery settings from here at startup, before any bean is built. Package `com.medata.config`, one class with `@EnableConfigServer`.

## Running

```bash
./mvnw spring-boot:run          # :8888
./mvnw spotless:apply verify    # format + full build
```

In containers it starts from the root `compose.yaml` with a `/dev/tcp` healthcheck, and the services wait for it (`depends_on: service_healthy`). What is being served can be checked in the browser at `http://localhost:8888/category/default`.

## Configuration

| Property | Value | Why |
|---|---|---|
| `server.port` | `8888` | the conventional Config Server port |
| `spring.profiles.active` | `native` | serve files from the classpath instead of the default git backend |
| `spring.cloud.config.server.native.search-locations` | `classpath:/config` | where the served files live |

Served files sit in `src/main/resources/config/`. The file name equals the client's `spring.application.name`, so `category.properties` and `lab-test.properties`. Both lab-test instances ask for the same name and get identical configuration, which keeps them on one shared database. Credentials inside use placeholders such as `${CATEGORY_DB_USER:medata}`, resolved on the client against its own environment, so no secret lives in this repository.

## Data model

None. Configuration is baked into the image, so changing it means rebuilding only this small image. The git backend would turn config changes into reviewable commits and is the production choice.

## API

`GET /{application}/{profile}`, for example `/category/default` or `/lab-test/default`. The response lists the property sources and their values.

## Decisions

- The `native` backend over git for lab simplicity.
- The gateway is deliberately NOT a config client. Task 5 of the instruction says "services", and the distinction against task 2 ("gateway and services") is deliberate, so the gateway keeps `DISCOVERY_URL` in the environment.
- Clients import with the `optional:configserver:` prefix, so development mode works with no config server running.
