# discovery service

> English version. Polish 1:1 counterpart: [../pl/README.md](../pl/README.md)

## Overview

The service registry (Spring Cloud Netflix Eureka Server) on port **8761**. Every backend application registers here under its `spring.application.name`, sends heartbeats and fetches a local copy of the registry. The gateway and the category service resolve other services by logical name through this copy. Package `com.medata.discovery`, zero business logic, the dependency is the product.

## Running

```bash
./mvnw spring-boot:run          # :8761
./mvnw spotless:apply verify    # format + full build
```

In containers it starts from the root `compose.yaml`. The dashboard at `http://localhost:8761` lists the registered instances and is worth showing during grading.

## Configuration

| Property | Value | Why |
|---|---|---|
| `server.port` | `8761` | the conventional Eureka port |
| `eureka.client.register-with-eureka` | `false` | a standalone node does not register with itself |
| `eureka.client.fetch-registry` | `false` | nothing to fetch when there are no peers |

Production setups run several Eureka servers that register with each other (peer replication). The lab needs one standalone node, hence both flags.

## Data model

None. The registry lives in memory and is rebuilt from registrations and heartbeats. A missed-heartbeat window leads to eviction of the instance.

## API

The dashboard at `/` and the REST registry under `/eureka` (registrations, heartbeats, `/eureka/apps` listing). Clients talk to it through the `spring-cloud-starter-netflix-eureka-client` autoconfiguration, never by hand.

## Decisions

- A single standalone node instead of a peer-replicated pair. The lab grades one registry and the clients keep a local cache anyway.
- The port is published to the host in compose. The dashboard is a grading artifact, like Swagger UI in lab 3.
