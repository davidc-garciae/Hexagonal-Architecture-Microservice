# PowerUp Microservices Template (Spring Boot, Hexagonal)

| Core                                                                                                                                                                                                                                                                        | Infra                                                                                                                                                                                            | API                                                                                                                                                                                                    |
| --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| ![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring-boot&logoColor=white) ![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white) | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Broker-FF6600?logo=rabbitmq&logoColor=white) | ![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-6BA539?logo=openapiinitiative&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger%20UI-springdoc-85EA2D?logo=swagger&logoColor=black) |

| Code                                                                                                                         | Testing                                                                                                                                                                                                                                                                       | Observability                                                                                                                                                                     |
| ---------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ![MapStruct](https://img.shields.io/badge/MapStruct-1.x-02569B) ![Lombok](https://img.shields.io/badge/Lombok-1.18.x-CA0C00) | ![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?logo=junit5&logoColor=white) ![ArchUnit](https://img.shields.io/badge/ArchUnit-1.x-1F6FEB) ![JaCoCo](https://img.shields.io/badge/JaCoCo-80%25-DC143C) ![Spotless](https://img.shields.io/badge/Spotless-style-0ABF53) | ![Micrometer](https://img.shields.io/badge/Micrometer-Tracing-0075A8) ![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-OTLP-FF6F00?logo=opentelemetry&logoColor=white) |

A production‑ready template to bootstrap Spring Boot microservices and the API Gateway following Hexagonal Architecture. It includes OpenAPI, Observability (JSON logs + OpenTelemetry tracing), Security templates, Testing (unit, WebMvc, ArchUnit), CI workflow, and usage guides.

## What you get

-   Hexagonal structure (domain, application, infrastructure)
-   OpenAPI 3.0 (springdoc) + Gateway docs aggregation
-   Observability: JSON logging (Logback + MDC) and tracing (Micrometer + OTLP)
-   Security templates: header-based auth in services; JWT filter for Gateway
-   Testing: JUnit 5, WebMvc slice, ArchUnit rules, JaCoCo (80% gate)
-   Code style: Spotless (Google Java Format), .editorconfig
-   CI: GitHub Actions workflow (format check, tests, coverage, OpenAPI validate)
-   Diagrams and detailed docs under `docs/`

## Repositories strategy (multi‑repo)

-   Mark this repo as GitHub “Template repository”.
-   Create 6 repositories with “Use this template”:
    -   api-gateway
    -   users-service, restaurants-service, orders-service, traceability-service, messaging-service
-   Branching/versioning (recommended):
    -   default branches: `develop` (protected) and `main` for releases
    -   feature by HU: `feature/HU-xxx-short-desc`
    -   tag on merge of each HU: `v0.1.0-HU-001` (example)

## How to create a new microservice from this template

1. Clone as base

```bash
git clone <template-repo> my-service
cd my-service
```

2. Rename identity

-   Package: `com.pragma.powerup` → `com.pragma.<service>`
-   `.env`: set `SPRING_APPLICATION_NAME`, `PORT`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

3. OpenAPI contract

-   Create/adjust `docs/openapi/<service>.yaml`
-   Validate: `./gradlew openApiValidateAll`

4. Verify

-   Format: `./gradlew spotlessApply`
-   Test + coverage: `./gradlew test jacocoTestReport`
-   Run: `./gradlew bootRun` and open `http://localhost:${PORT}/swagger-ui.html`

## How to create the API Gateway from this template

-   Use `docs/templates/build.gradle.gateway` (WebFlux + Gateway + springdoc-webflux)
-   Swagger aggregation from Gateway: routes `/docs/*` → `/v3/api-docs` of each service
-   Security (snippet): `docs/templates/gateway/security/` (`JwtAuthenticationFilter`, `JwtService`)
-   After JWT validation, inject headers `X-User-Id`, `X-User-Email`, `X-User-Role` to downstream services

## Environment variables (examples)

Microservice `.env`:

```dotenv
SPRING_APPLICATION_NAME=your-service-name
PORT=8081
DB_URL=jdbc:postgresql://localhost:5432/your_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SCHEMA=public
APPDESCRIPTION=PowerUp API
APPVERSION=dev
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_PRAGMA_POWERUP=DEBUG
RABBITMQ_URL=amqps://user:pass@host/vhost
RABBITMQ_EXCHANGE_ORDERS=powerup.orders
RABBITMQ_RK_ORDER_CREATED=orders.created
RABBITMQ_RK_ORDER_STATUS_CHANGED=orders.status.changed
RABBITMQ_QUEUE_TRACING_ORDER_CREATED=tracing.order.created
RABBITMQ_QUEUE_TRACING_ORDER_STATUS_CHANGED=tracing.order.status.changed
RABBITMQ_QUEUE_MESSAGING_ORDER_CREATED=messaging.order.created
RABBITMQ_QUEUE_MESSAGING_ORDER_STATUS_CHANGED=messaging.order.status.changed
MICROSERVICES_USERS_URL=http://localhost:8081
MICROSERVICES_RESTAURANTS_URL=http://localhost:8082
MICROSERVICES_ORDERS_URL=http://localhost:8083
MICROSERVICES_TRACEABILITY_URL=http://localhost:8084
MICROSERVICES_MESSAGING_URL=http://localhost:8085
```

Gateway `.env`:

```dotenv
SPRING_APPLICATION_NAME=api-gateway
PORT=8080
SPRING_SECURITY_JWT_SECRET=change-me
USERS_SERVICE_URL=http://localhost:8081
RESTAURANTS_SERVICE_URL=http://localhost:8082
ORDERS_SERVICE_URL=http://localhost:8083
TRACEABILITY_SERVICE_URL=http://localhost:8084
MESSAGING_SERVICE_URL=http://localhost:8085
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
LOGGING_LEVEL_ROOT=INFO
```

## Project structure (per service)

```
src/main/java/com/pragma/<service>/
  domain/{model, api, spi, usecase}
  application/{dto, mapper, handler}
  infrastructure/
    input/{rest, messaging}
    out/{jpa, feign, messaging, external}
    configuration
    security
```

## Observability

-   Logs JSON: `src/main/resources/logback-spring.xml` (MDC: service, traceId, spanId, requestId, userId, orderId)
-   Tracing: `Micrometer Tracing + OTLP` (env `OTEL_EXPORTER_OTLP_ENDPOINT`)

## Security (template)

-   Microservices: `HeaderAuthenticationFilter`, `SecurityConfiguration`, `RoleConstants`
    -   Read `X-User-*` headers from Gateway; use `@PreAuthorize("hasRole('EMPLOYEE')")`
    -   Example dummy in `SecurityDemoController` (`/api/v1/demo/secure`)
-   Gateway: JWT validation filter and header propagation (snippets in `docs/templates/gateway/security`)

## Testing & Quality

-   Unit/Integration tests: `./gradlew test`
-   Coverage (80% gate): `./gradlew check` (includes `jacocoCoverageVerification`)
-   ArchUnit rules: `src/test/java/.../architecture/HexagonalArchitectureTest.java`
-   Controller slice with security: by default use `@AutoConfigureMockMvc(addFilters = false)`. To test security, load `SecurityConfiguration` and use `.with(csrf())` + `X-User-*` headers (`spring-security-test`).
-   Formatting: `./gradlew spotlessApply`

## CI

-   `.github/workflows/ci.yml`: format check, build, tests, coverage artifact, `openApiValidateAll`

## Diagrams & Docs

-   Architecture diagrams: `docs/diagrams/`
-   Service specs and guidance: `docs/README.md`, `docs/README.microservicios.md`
-   Contribution guide: `docs/CONTRIBUTING.md`

## Microservices catalog (suggested)

-   users-service, restaurants-service, orders-service, traceability-service, messaging-service, api-gateway

## License

MIT (adjust as needed)
